package org.example.repository.database;

import org.example.domain.Customer;
import org.example.repository.CustomerRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class DBCustomerRepository implements CustomerRepository {
    private Map<String,Customer> repo;
    private long nextCustomerId;

    public DBCustomerRepository(){
        String sqlAll = "SELECT * FROM customers";
        String sqlNextID = "SELECT MAX(id) FROM customers";
        try (
                Connection conn = DatabaseConnection.connect();
                PreparedStatement psAll = conn.prepareStatement(sqlAll);
                PreparedStatement psNextId = conn.prepareStatement(sqlNextID);
                ResultSet rsAll = psAll.executeQuery();
                ResultSet rsNextId = psNextId.executeQuery();
        ) {
            repo = new HashMap<>();
            // get nextId
            rsNextId.next();
            String lastId = rsNextId.getString(1);
            if (lastId != null){
                String idStr = lastId.replace("C","");
                nextCustomerId = (Integer.parseInt(idStr)) + 1;
                while (rsAll.next()){
                    String id = rsAll.getString(1);
                    String name = rsAll.getString(2);
                    String email = rsAll.getString(3);
                    repo.put(id,new Customer(id,name,email));
                }
            }
            else nextCustomerId = 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Customer get(String customerId) {
        return repo.get(customerId);
    }
    @Override
    public Customer create(String name, String email) {
        long checkDuplicateName = repo.values().stream().filter(c -> c.getName().equals(name)).count();
        if (checkDuplicateName > 0) return null;
        String id = String.format("C%04d", nextCustomerId);
        Customer customer = new Customer(id, name, email);
        repo.put(id,customer);
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
        boolean removeStatus = repo.remove(customer.getId(),customer);
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
        return repo.values().stream();
    }
}
