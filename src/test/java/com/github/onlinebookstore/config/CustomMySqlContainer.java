package com.github.onlinebookstore.config;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

public class CustomMySqlContainer extends MySQLContainer<CustomMySqlContainer> {
    private static final String DB_IMAGE = "mysql:8";

    private static CustomMySqlContainer mysqlContainer;

    private CustomMySqlContainer() {
        super(DB_IMAGE);
        this.waitingFor(Wait.forHealthcheck());
    }

    public static synchronized CustomMySqlContainer getInstance() {
        if (mysqlContainer == null) {
            mysqlContainer = new CustomMySqlContainer();
        }
        return mysqlContainer;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("TEST_DB_URL", mysqlContainer.getJdbcUrl());
        System.setProperty("TEST_DB_USERNAME", mysqlContainer.getUsername());
        System.setProperty("TEST_DB_PASSWORD", mysqlContainer.getPassword());
    }

    @Override
    public void stop() {
    }
}
