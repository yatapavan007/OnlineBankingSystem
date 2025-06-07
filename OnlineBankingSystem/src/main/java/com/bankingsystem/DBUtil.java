package com.bankingsystem;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {

    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String DB_USER = "system";      // Replace with your Oracle username
    private static final String DB_PASSWORD = "12345";  // Replace with your Oracle password

    public static Connection getConnection() throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
