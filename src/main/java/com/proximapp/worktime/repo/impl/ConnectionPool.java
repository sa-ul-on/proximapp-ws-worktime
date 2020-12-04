package com.proximapp.worktime.repo.impl;

import com.proximapp.worktime.WorktimeApplication;
import org.springframework.boot.SpringApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionPool {

    public static Connection createConnection() throws SQLException {

        Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/proximapp", "postgres", "admin");

        if (conn != null) {
            System.out.println("Connesso");
        } else {
            System.out.println("Failed");
        }
        return conn;
    }

    public static Connection getConnection() {
        Connection connection = null;

        try {
            connection = createConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;

    }
}



