package com.example.customers_data.repositories;

import com.example.customers_data.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findAllByOrderByIdAsc();

    @Query("""
    SELECT c FROM Customer c
    WHERE (:name IS NULL OR :name = '' OR c.name = :name)
      AND (:phone IS NULL OR :phone = '' OR c.phone = :phone)
      AND (:email IS NULL OR :email = '' OR c.email = :email)
      AND (:address IS NULL OR :address = '' OR c.address = :address)
    """)
    List<Customer> findByDynamicFields(
            @Param("name") String name,
            @Param("phone") String phone,
            @Param("email") String email,
            @Param("address") String address
    );
}
