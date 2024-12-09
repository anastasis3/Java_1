package com.example.demo.controller;

import java.io.*;
import java.sql.*;

import com.example.demo.connection.PropertyReaderUtil;
import com.example.demo.controller.command.BaseCommand;
import com.example.demo.controller.command.CommandType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;


@WebServlet(name = "Servlet", value = "/controller")
public class Controller extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(Controller.class);
    private String dbUrl = "jdbc:postgresql://localhost:5432/postgres"; 
    private String dbUser = "postgres";
    private String dbPassword = "";

    private String message;


    public void init() {
        message = "Hello World!";
        logger.info("Servlet initialized with message: {}", message);
    }



    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");
        if ("register".equals(action)) {
            request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/jsp/main.jsp").forward(request, response);
        }
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");
        if ("register".equals(action)) {
            registerUser(request, response);
        } else if ("login".equals(action)) {
            loginUser(request, response);
        } else {
            processRequest(request, response);
        }
    }


    private void registerUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        // Проверка на наличие необходимых данных
        if (username == null || password == null || email == null || phone == null) {
            response.sendRedirect("register.jsp?error=missing_data");
            return;
        }

        // Хеширование пароля
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String query = "INSERT INTO users (user_id, username, password, email, phone, created_at) VALUES (?, ?, ?, ?, ?, ?)";

        // Устанавливаем роль по умолчанию, например, "user"
        String role = "user";

        Timestamp createdAt = new Timestamp(System.currentTimeMillis());

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            int userid = 2;
            statement.setInt(1, userid);
            statement.setString(2, username);
            statement.setString(3, hashedPassword);
            statement.setString(4, email);
            statement.setString(5, phone);
            statement.setTimestamp(6, createdAt);
            statement.executeUpdate();

            // Устанавливаем пользователя в сессию
            request.getSession().setAttribute("user", username);
            response.sendRedirect("/jsp/main.jsp"); // Перенаправление на главную страницу
        } catch (SQLException e) {
            logger.error("Error registering user", e);
            response.sendRedirect("register.jsp?error=registration_failed");
        }
    }


    private void loginUser(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!validateLoginForm(request)) {
            request.setAttribute("errorLoginPassMessage", "Пожалуйста, введите логин и пароль.");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            return;
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (authenticate(username, password)) {
            request.getSession().setAttribute("user", username);
            response.sendRedirect("/jsp/main.jsp"); // Успешный вход
        } else {
            request.setAttribute("errorLoginPassMessage", "Неверный логин или пароль");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        }
    }

    private boolean authenticate(String username, String password) {
        String query = "SELECT password FROM users WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("password");
                return BCrypt.checkpw(password, hashedPassword);
            }
        } catch (SQLException e) {
            logger.error("Error authenticating user", e);
        }
        return false;
    }


    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        String commandStr = request.getParameter("command");
        BaseCommand command = CommandType.defineCommand(commandStr);
        String page = command.execute(request);
        request.getRequestDispatcher(page).forward(request, response);
    }

    private Connection getConnection() throws SQLException {
        String dbUrl = PropertyReaderUtil.getProperty("db.url");
        String dbUser = PropertyReaderUtil.getProperty("db.username");
        String dbPassword = PropertyReaderUtil.getProperty("db.password");
        //DriverManager.registerDriver(new org.postgresql.Driver());
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        logger.debug("Attempting to connect to database with URL: {}, User: {}", dbUrl, dbUser);
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }


    private boolean validateLoginForm(HttpServletRequest request) {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        return login != null && !login.isEmpty() && password != null && !password.isEmpty();
    }
    private void handleGetOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT order_id,order_date," +
                                                                "delivery_date, delivery_address, " +
                                                                "status, total_price, rating, review FROM orders")) {

            out.println("<h1>Orders</h1>");
            out.println("<table border='1'>");
            out.println("<tr><th>Order ID</th><th>User ID</th><th>Status</th><th>Delivery Date</th><th>Total Price</th></tr>");
            while (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                int userId = resultSet.getInt("user_id");
                String status = resultSet.getString("status");
                String deliveryDate = resultSet.getString("delivery_date");
                double totalPrice = resultSet.getDouble("total_price");

                out.println("<tr>");
                out.println("<td>" + orderId + "</td>");
                out.println("<td>" + userId + "</td>");
                out.println("<td>" + status + "</td>");
                out.println("<td>" + deliveryDate + "</td>");
                out.println("<td>" + totalPrice + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<p>Error retrieving data: " + e.getMessage() + "</p>");
        }
    }


//    private String getData() {
//        String username = "Unknown User";
//        String query = "SELECT current_user";
//
//        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
//             PreparedStatement statement = connection.prepareStatement(query);
//             ResultSet resultSet = statement.executeQuery()) {
//
//            if (resultSet.next()) {
//                username = resultSet.getString(1);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return username;
//    }
//
//    public void destroy() {
//        logger.warn("Servlet is being destroyed");
//    }
}

