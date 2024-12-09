package com.example.demo.service.impl;

import com.example.demo.dao.UserDao;
import com.example.demo.dao.impl.UserDaoImpl;
import com.example.demo.dao.model.User;
import com.example.demo.service.UserService;
import org.mindrot.jbcrypt.BCrypt;

public class UserServiceImpl implements UserService {

    private final UserDao userDao = new UserDaoImpl();

    @Override
    public boolean registerUser(String username, String password, String email, String phone, String role) {
        return userDao.registerUser(username, password, email, phone, role);
    }

    @Override
    public boolean authenticate(String username, String password) {
        String hashedPassword = userDao.getHashedPassword(username);
        return hashedPassword != null && BCrypt.checkpw(password, hashedPassword);
    }

    @Override
    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }
}