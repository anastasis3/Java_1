package com.example.demo.controller.command;

import jakarta.servlet.http.HttpServletRequest;

public class LogoutCommand implements BaseCommand{
    @Override
    public String execute(HttpServletRequest request) {
        request.getSession().invalidate();
        return "/jsp/login.jsp";
    }
}
