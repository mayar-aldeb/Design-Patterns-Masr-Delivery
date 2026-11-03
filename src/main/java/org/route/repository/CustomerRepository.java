package org.route.repository;


import lombok.RequiredArgsConstructor;
import org.route.model.Address;
import org.route.model.Customer;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CustomerRepository {

   private final Map<Integer, Customer> customers=new HashMap<>();

   public List<Customer> findAll()
    {
        return customers.values().stream().toList();
    }

    public Optional<Customer> findById(int id)
    {
        return Optional.ofNullable(customers.get(id));
    }

    public void save(Customer customer)
    {
        customers.put(customer.getId(), customer);
    }

    public List<String> findDistricts() {
        return customers.values().stream()
                .flatMap(customer -> customer.getAddresses().stream())
                .map(Address::getDistrict)
                .distinct()
                .toList();
    }





}
