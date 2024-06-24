package org.example.repository.file;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

import org.example.domain.Customer;
import org.example.repository.CustomerRepository;

public class FileCustomerRepository implements CustomerRepository{
    private final String filename = FilePATH.path + "\\G25CustomerFile.txt";;
    private Map<String,Customer> repo;
    private long nextCustomerId;

    public FileCustomerRepository(){
        File file = new File(filename);
        if (file.exists()){
            try (
                    InputStream fis = new FileInputStream(filename);
                    InputStream bi = new BufferedInputStream(fis);
                    ObjectInputStream oi = new ObjectInputStream(bi);
            ) {
                nextCustomerId = oi.readLong();
                repo = (Map<String,Customer>)oi.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            nextCustomerId = 1;
            this.repo = new HashMap<>();
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
        try (
                OutputStream fo = new FileOutputStream(filename);
                OutputStream bo = new BufferedOutputStream(fo);
                ObjectOutputStream oo = new ObjectOutputStream(bo);
        ) {
            oo.writeLong(nextCustomerId);
            oo.writeObject(repo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customer;
    }

    @Override
    public Boolean update(Customer customer, String name, String email) {
        if (customer == null) return false;
        customer.setName(name);
        customer.setEmail(email);
        try (
                OutputStream fo = new FileOutputStream(filename);
                OutputStream bo = new BufferedOutputStream(fo);
                ObjectOutputStream oo = new ObjectOutputStream(bo);
        ) {
            oo.writeLong(nextCustomerId);
            oo.writeObject(repo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Boolean remove(Customer customer) {
        boolean removeStatus = repo.remove(customer.getId(),customer);
        if (!removeStatus) return false;
        try (
                OutputStream fo = new FileOutputStream(filename);
                OutputStream bo = new BufferedOutputStream(fo);
                ObjectOutputStream oo = new ObjectOutputStream(bo);
        ) {
            oo.writeLong(nextCustomerId);
            oo.writeObject(repo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return removeStatus;
    }

    @Override
    public Stream<Customer> stream() {
        return repo.values().stream();
    }

}