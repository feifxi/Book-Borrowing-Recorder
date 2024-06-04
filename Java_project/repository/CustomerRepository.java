package repository;

import java.util.stream.Stream;
import domain.Customer;

public interface CustomerRepository {
    public Customer get(String customerId);
    public Customer create(String name, String email);
    public Boolean update(Customer customer,String name, String email);
    public Boolean remove(Customer customer);
    public Stream<Customer> stream(); 
}
