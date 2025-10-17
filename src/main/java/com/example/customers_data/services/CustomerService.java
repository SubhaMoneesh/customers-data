package com.example.customers_data.services;

import com.example.customers_data.models.Customer;
import com.example.customers_data.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    public CustomerRepository customerRepo;

    public List<Customer> findAll() {
        return customerRepo.findAll();
    }

    public Customer save(Customer customer) {
        return customerRepo.save(customer);
    }
}
