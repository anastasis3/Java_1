package com.example.demo.dao.impl;

import com.example.demo.dao.UserDao;
import com.example.demo.connection.ConnectionPool;
import com.example.demo.dao.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class UserDaoImpl implements UserDao {

    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    @Override
    public boolean registerUser(String username, String password, String email, String phone, String role) {
        String insertUserQuery = "INSERT INTO users (username, password, email, phone, role, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        LocalDateTime createdAt = LocalDateTime.now();

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertUserQuery)) {
            statement.setString(1, username);
            statement.setString(2, hashedPassword);
            statement.setString(3, email);
            statement.setString(4, phone);
            statement.setString(5, role);
            statement.setObject(6, createdAt);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User getUserByUsername(String username) {
        String selectUserQuery = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(selectUserQuery)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return extractUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public String getHashedPassword(String username) {
        String query = "SELECT password FROM users WHERE username = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        int userId = resultSet.getInt("user_id");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        String email = resultSet.getString("email");
        String phone = resultSet.getString("phone");
        String role = resultSet.getString("role");
        LocalDateTime createdAt = resultSet.getObject("created_at", LocalDateTime.class);
        return new User(userId, username, password, email, phone, role, createdAt);
    }
}