package org.example.repository.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.example.domain.Customer;
import org.example.repository.CustomerRepository;

public class InMemoryCustomerRepository implements CustomerRepository{
    private List<Customer> repo;
    private long nextCustomerId = 1;

    public InMemoryCustomerRepository(){this.repo = new ArrayList<>();}

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
        return customer;
    }

    @Override
    public Boolean update(Customer customer, String name, String email) {
        if (customer == null) return false;
        customer.setName(name);
        customer.setEmail(email);
        return true;
    }

    @Override
    public Boolean remove(Customer customer) {
        return repo.remove(customer);
    }

    @Override
    public Stream<Customer> stream() {
        return repo.stream();
    }

}

