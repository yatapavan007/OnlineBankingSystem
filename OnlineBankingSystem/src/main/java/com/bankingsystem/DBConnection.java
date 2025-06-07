package com.bankingsystem;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String username = "system";
        String password = "12345";
        return DriverManager.getConnection(url, username, password);
    }
}
