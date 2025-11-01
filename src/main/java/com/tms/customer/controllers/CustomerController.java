package com.tms.customer.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tms.customer.exceptions.CustomerNotFoundException;
import com.tms.customer.exceptions.DuplicateBookingException;
import com.tms.customer.exceptions.InvalidBookingDate;
import com.tms.customer.models.TourCustomer;
import com.tms.customer.services.TourCustomerService;
import com.tms.customer.utils.JWTUtils;

import jakarta.validation.Valid;

@RestController
public class CustomerController {
    
    @Autowired
    private TourCustomerService tourCustomerService;

    @GetMapping("/auth/customers")
    public List<TourCustomer> getAllCustomers() {
        return tourCustomerService.getAllCustomers();
    }

    @GetMapping("/auth/customers/{id}")
    public TourCustomer getCustomerById(@PathVariable int id) throws CustomerNotFoundException {
        return tourCustomerService.getCustomerById(id);
    }

    @PostMapping("/customers")
    public ResponseEntity<?> addCustomer(@Valid @RequestBody TourCustomer tourCustomer) throws DuplicateBookingException, InvalidBookingDate {
        tourCustomerService.addCustomer(tourCustomer);
        System.out.println("Customer added successfully");
        String token = JWTUtils.generateToken(tourCustomer.getEmail());
        System.out.println("Token generated successfully");
        return new ResponseEntity<>(token , HttpStatus.CREATED);
    }

    @PutMapping("/auth/customers/{id}")
    public TourCustomer updateCustomer(@PathVariable int id, @Valid @RequestBody TourCustomer tourCustomer) throws CustomerNotFoundException, InvalidBookingDate, DuplicateBookingException {
        return tourCustomerService.updateCustomer(id, tourCustomer);
    }

    @DeleteMapping("/auth/customers/{id}")
    public void deleteCustomer(@PathVariable int id) throws CustomerNotFoundException {
        tourCustomerService.deleteCustomer(id);
    }

    @GetMapping("/customers/data-pull")
    public ResponseEntity<?> fetchDataFromExternalAPI() {
        try {
            List<String> errors = tourCustomerService.fetchDataFromExternalAPI();
            if (errors.isEmpty()) {
                return new ResponseEntity<>("Data fetched successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            System.out.println("Failed to fetch data from external API");
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
