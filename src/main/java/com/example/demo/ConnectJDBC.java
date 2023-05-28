package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectJDBC {
    public Connection getConnection() {
        String hostname = "localhost:33066";
        String databaseName = "OOP";
        String databaseUser = "root";
        String databasePassword = "adminADMIN@";
        String url = "jdbc:mysql://" + hostname + "/" + databaseName + "?allowMultiQueries=true";
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, databaseUser, databasePassword);
            System.out.println("passed");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
