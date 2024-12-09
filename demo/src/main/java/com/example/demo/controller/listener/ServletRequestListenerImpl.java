package com.example.demo.controller.listener;

import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.logging.Level;
@WebServlet
public class ServletRequestListenerImpl implements ServletRequestListener {

    static Logger logger = LogManager.getLogger();

    public void requestInitialized(ServletRequestEvent sre) {

    }

    public void requestDestroyed(ServletRequestEvent sre) {
        logger.info("Request destroyed");
    }
}
