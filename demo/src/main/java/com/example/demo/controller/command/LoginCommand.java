package com.example.demo.controller.command;

import com.example.demo.service.impl.UserServiceImpl;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class LoginCommand implements BaseCommand{
    @Override
    public String execute(HttpServletRequest request) {
        if (!validateLoginForm(request)) {
            request.setAttribute("errorLoginPassMessage", "Please enter both login and password.");
            return "/jsp/login.jsp";
        }
        String login = request.getParameter("username");
        String password = request.getParameter("password");
        UserService userService = new UserServiceImpl();//todo
        String page;
        if (userService.authenticate(login, password)) {
            request.getSession().setAttribute("user", login);
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute("session_login", login);
            page = "/jsp/main.jsp";
        } else {
            request.setAttribute("errorLoginPassMessage","Incorrect login or password");
            page = "/jsp/login.jsp";
        }
        return page;
    }
    private boolean validateLoginForm(HttpServletRequest request) {
        String login = request.getParameter("username");
        String password = request.getParameter("password");
        return login != null && !login.isEmpty() && password != null && !password.isEmpty();
    }

}
