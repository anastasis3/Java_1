package com.example.demo.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {
    private static final Lock lock = new ReentrantLock();
    private static final Logger logger = LogManager.getLogger(ConnectionPool.class);
    private static final int CONNECTION_POOL_CAPACITY = 10;
    private static ConnectionPool instance;

    private final BlockingQueue<Connection> freeConnections = new LinkedBlockingQueue<>(CONNECTION_POOL_CAPACITY);
    private final BlockingQueue<Connection> usedConnections = new LinkedBlockingQueue<>(CONNECTION_POOL_CAPACITY);

    // Загрузка драйвера PostgreSQL
    static {
        try {
            Class.forName("org.postgresql.Driver");
            logger.debug("PostgreSQL driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            logger.error("Failed to load PostgreSQL driver.", e);
            throw new ExceptionInInitializerError("Failed to load PostgreSQL driver.");
        }
    }

    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    private ConnectionPool() {
        loadProperties();

        for (int i = 0; i < CONNECTION_POOL_CAPACITY; i++) {
            try {
                Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                freeConnections.add(connection);
                logger.debug("Connection #{} created and added to the pool.", i);
            } catch (SQLException e) {
                logger.error("Failed to create connection #{}.", i, e);
                closePool(); // Закрываем уже созданные соединения
                throw new IllegalStateException("Failed to initialize connection pool.");
            }
        }

        if (freeConnections.isEmpty()) {
            logger.fatal("Failed to initialize connection pool. No connections available.");
            throw new IllegalStateException("Failed to initialize connection pool.");
        }
    }

    public static ConnectionPool getInstance() {
        if (instance == null) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new ConnectionPool();
                    logger.debug("Connection pool instance created.");
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = freeConnections.take();
            if (connection.isValid(2)) {
                usedConnections.put(connection);
                logger.debug("Connection taken from the pool.");
            } else {
                logger.warn("Invalid connection detected. Attempting to replace it.");
                connection = createNewConnection();
                usedConnections.put(connection);
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("Failed to get connection from the pool.", e);
            Thread.currentThread().interrupt();
        }
        return connection;
    }

    public void releaseConnection(Connection connection) {
        try {
            if (!usedConnections.remove(connection)) {
                logger.warn("Connection not found in used connections queue.");
            }
            if (connection.isValid(2)) {
                freeConnections.put(connection);
                logger.debug("Connection returned to the pool.");
            } else {
                logger.warn("Invalid connection discarded.");
            }
        } catch (InterruptedException | SQLException e) {
            logger.error("Failed to release connection to the pool.", e);
        }
    }

    public void closePool() {
        for (Connection connection : freeConnections) {
            try {
                connection.close();
                logger.debug("Connection closed.");
            } catch (SQLException e) {
                logger.error("Failed to close connection.", e);
            }
        }
        freeConnections.clear();
        usedConnections.clear();
    }

    private void loadProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db/db.properties")) {
            if (input == null) {
                throw new IOException("Database properties file not found.");
            }
            props.load(input);
            dbUrl = props.getProperty("db.url");
            dbUser = props.getProperty("db.username");
            dbPassword = props.getProperty("db.password");
        } catch (IOException e) {
            logger.fatal("Failed to load properties.", e);
            throw new ExceptionInInitializerError("Failed to load properties.");
        }
    }

    private Connection createNewConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }
}