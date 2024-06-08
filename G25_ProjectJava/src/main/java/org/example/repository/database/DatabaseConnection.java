package org.example.repository.database;

import java.sql.*;

public class DatabaseConnection {
    public static Connection conenct(){
        String url = "jdbc:mysql://localhost:3306/int103db";
        String username  = "root";
        String password = "";
        Connection conn = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url,username,password);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
        return conn;
    }
}
