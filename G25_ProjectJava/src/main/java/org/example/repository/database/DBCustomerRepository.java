package org.example.repository.database;

import org.example.domain.Book;
import org.example.domain.Customer;
import org.example.repository.CustomerRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DBCustomerRepository implements CustomerRepository {
    private List<Customer> repo;
    private long nextCustomerId;

    public DBCustomerRepository(){
        String sql = "Select * FROM customers";
        try (
            Connection conn = DatabaseConnection.connect();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet results = preparedStatement.executeQuery();
            ){
            repo = new ArrayList<>();
            int count = 0;
            while (results.next()){
                count++;
                String id = results.getString(1);
                String name = results.getString(2);
                String email = results.getString(3);
                repo.add(new Customer(id,name,email));
            }
            if (count == 0){        // ไม่มีข้อมูลใน Database
                nextCustomerId = 1;
            }
            else {
                Customer lastCustomer = repo.get(count-1);
                String numberStr = lastCustomer.getId().replace("C","");
                nextCustomerId = (Integer.parseInt(numberStr)) +1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Customer get(String customerId) {
        return repo.stream().filter(c -> c.getId().equals(customerId)).findFirst().orElse(null);
    }

    @Override
    public Customer create(String name, String email) {
        long checkDuplicateName = repo.stream().filter(c -> c.getName().equals(name)).count();
        if (checkDuplicateName > 0) return null;
        String id = String.format("C%04d", nextCustomerId);
        Customer customer = new Customer(id, name, email);
        repo.add(customer);
        nextCustomerId++;
        String sql = "INSERT INTO customers (id,name,email) VALUES (?,?,?)";
        try (
            Connection conn = DatabaseConnection.connect();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ){
            preparedStatement.setString(1,id);
            preparedStatement.setString(2,name);
            preparedStatement.setString(3,email);
            int result = preparedStatement.executeUpdate();
            if (result == 0) return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customer;
    }

    @Override
    public Boolean update(Customer customer, String name, String email) {
        if (customer == null) return false;
        customer.setName(name);
        customer.setEmail(email);
        String sql = "UPDATE customers SET name=?,email=? WHERE id=?";
        try (
            Connection conn = DatabaseConnection.connect();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ){
            preparedStatement.setString(3,customer.getId());
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,email);
            int result = preparedStatement.executeUpdate();
            if (result == 0) return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public Boolean remove(Customer customer) {
        boolean removeStatus = repo.remove(customer);
        if (removeStatus){
            String sql = "DELETE FROM customers WHERE id=?";
            try (
                Connection conn = DatabaseConnection.connect();
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                ){
                preparedStatement.setString(1,customer.getId());
                int result = preparedStatement.executeUpdate();
                if (result == 0) return false;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
        return removeStatus;
    }

    @Override
    public Stream<Customer> stream() {
        return repo.stream();
    }
}
