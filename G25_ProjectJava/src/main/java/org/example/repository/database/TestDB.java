package org.example.repository.database;

import java.sql.*;

public class TestDB {
    private String url = "jdbc:mysql://localhost:3306/int103db";
    private String username  = "root";
    private String password = "";


    public void testPrepeareStatement() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url,username,password);

            // Create Prepared Statement - ข้อดีคือเอาไว้ซ่อนข้อมูลเมื่อส่งขึ้น internet
            PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO customers (id,name,email) VALUES (?,?,?)");
            preparedStatement1.setString(1,"f1x");
            preparedStatement1.setString(2,"fee");
            preparedStatement1.setString(3,"f1@gmail.com");

            int i  = preparedStatement1.executeUpdate();
            System.out.println(i);
            // Select
            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM customers");
            ResultSet results = preparedStatement2.executeQuery();

            while (results.next()){
                System.out.println(results.getString(1) +" , "+ results.getString(2) +" , "+results.getString(3));
            }


        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        catch (SQLException e) {
            System.out.println(e);;
        }
    }

    public void testNormalStatement(){
        try {
            // Loading driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Loaded");
            // Connect to Database
            Connection connection = DriverManager.getConnection(url,username,password);
            System.out.println("Database Connected");
            // Create Statement
            Statement statement = connection.createStatement();
            // Execute Statement Query
            System.out.println("=== Select All ===");
            ResultSet results = statement.executeQuery("Select * FROM customers");
            while (results.next()){
                System.out.println(results.getString(1) +" , "+ results.getString(2) +" , "+results.getString(3));
            }
            // Execute Statement Insert
            int i = statement.executeUpdate("INSERT INTO customers (id,name,email) VALUES ('C000k','fei','feija@gmail.com')");

            // Execute Statement Update
            statement.executeUpdate("UPDATE customers SET email = 'kuy' WHERE id = 'C000f'");

            // Execute Statement Delete
            statement.executeUpdate("DELETE FROM customers WHERE id = 'C0001'");

        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        catch (SQLException e) {
            System.out.println(e);;
        }
    }
}
