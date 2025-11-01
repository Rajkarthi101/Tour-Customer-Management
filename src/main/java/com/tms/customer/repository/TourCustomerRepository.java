package com.tms.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tms.customer.models.TourCustomer;

public interface TourCustomerRepository extends JpaRepository<TourCustomer, Integer> {
    
    TourCustomer findByEmailAndTourPackage(String email, String tourPackage);
}
