package org.example.repository.database;

import java.sql.*;

public class DatabaseConnection {
    public static Connection connect(){
        String url = "jdbc:mysql://localhost:3306/g25db";   // Set URL Here!
        String username  = "root";
        String password = "mysql";       // Set Password Here!!
        Connection conn = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url,username,password);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
        return conn;
    }

    public static boolean isConnecting(){
        try (Connection conn = connect();){
            return conn != null;
        } catch (SQLException e) {
            return false;
        }
    }
}
