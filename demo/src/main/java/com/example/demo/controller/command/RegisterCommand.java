package com.example.demo.controller.command;

import com.example.demo.service.UserService;
import com.example.demo.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class RegisterCommand implements BaseCommand{
    private UserService userService = new UserServiceImpl();
    @Override
    public String execute(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        // Проверка на наличие необходимых данных
        if (username == null || password == null || email == null || phone == null) {
            //   response.sendRedirect("register.jsp?error=missing_data");
            return"/jsp/register.jsp?error=missing_data";
        }
        if(userService.registerUser(username, password, email, phone, "client")) {
            // Устанавливаем пользователя в сессию
            request.getSession().setAttribute("user", username);
            // response.sendRedirect("/jsp/main.jsp"); // Перенаправление на главную страницу
            return "/jsp/main.jsp";
        } else {
            // logger.error("Error registering user", e);
            // response.sendRedirect("register.jsp?error=registration_failed");
            return "/jsp/register.jsp?error=registration_failed";
        }
    }
}
