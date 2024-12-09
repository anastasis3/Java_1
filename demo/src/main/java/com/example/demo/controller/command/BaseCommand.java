package com.example.demo.controller.command;

import jakarta.servlet.http.HttpServletRequest;

public interface BaseCommand {
    public abstract String execute(HttpServletRequest request);

}
