package com.example.customers_data.services;

import com.example.customers_data.models.Customer;
import com.example.customers_data.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    @Autowired
    public CustomerRepository customerRepo;

    public List<Customer> findAll() {
        return customerRepo.findAllByOrderByIdAsc();
    }

    public Customer save(Customer customer) {
        return customerRepo.save(customer);
    }

    public List<Customer> findByDynamicFields(Customer customer) {
        return customerRepo.findByDynamicFields(customer.getName(), customer.getPhone(), customer.getEmail(), customer.getAddress());
    }

    public void delete(Customer customer) {
        customerRepo.delete(customer);
    }

    public boolean existsById(Long id) {
        return customerRepo.existsById(id);
    }
}
