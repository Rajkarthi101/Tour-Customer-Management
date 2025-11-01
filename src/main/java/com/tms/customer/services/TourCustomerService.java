package com.tms.customer.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tms.customer.models.TourCustomer;
import com.tms.customer.proxy.DataPullProxy;
import com.tms.customer.repository.TourCustomerRepository;
import com.tms.customer.exceptions.CustomerNotFoundException;
import com.tms.customer.exceptions.DuplicateBookingException;
import com.tms.customer.exceptions.InvalidBookingDate;

@Service
public class TourCustomerService {
    
    @Autowired
    private TourCustomerRepository tourCustomerRepository;

    @Autowired
    private DataPullProxy dataPullProxy;

    public List<TourCustomer> getAllCustomers() {
        return tourCustomerRepository.findAll();
    }

    public TourCustomer getCustomerById(int id) throws CustomerNotFoundException {
        TourCustomer tourCustomer = tourCustomerRepository.findById(id).orElse(null);
        if (tourCustomer == null) {
            throw new CustomerNotFoundException("Customer not found with id: " + id);
        }
        return tourCustomer;
    }

    // add new customer
    public TourCustomer addCustomer(TourCustomer tourCustomer) throws DuplicateBookingException, InvalidBookingDate {

        TourCustomer existingCustomer = tourCustomerRepository.findByEmailAndTourPackage(tourCustomer.getEmail(), tourCustomer.getTourPackage());
        if (existingCustomer != null) {
            throw new DuplicateBookingException("Customer "+ tourCustomer.getEmail() +" is already in the tour package "+ tourCustomer.getTourPackage());
        }

        try {
            if (tourCustomer.getBookingDate().compareTo(new Date(System.currentTimeMillis())) < 0) {
                throw new InvalidBookingDate("Booking date must be greater than or equal to today");
            }
        } catch (Exception e) {
            throw new InvalidBookingDate("Booking date must be greater than or equal to today");
        }
        return tourCustomerRepository.save(tourCustomer);
    }

    // update customer
    public TourCustomer updateCustomer(int id, TourCustomer tourCustomer) throws CustomerNotFoundException, InvalidBookingDate, DuplicateBookingException {
        TourCustomer existingCustomer = tourCustomerRepository.findById(id).orElse(null);
        if (existingCustomer != null) {

            try {
                if (tourCustomer.getBookingDate().compareTo(new Date(System.currentTimeMillis())) < 0) {
                    throw new InvalidBookingDate("Booking date must be greater than or equal to today");
                }
            } catch (Exception e) {
                throw new InvalidBookingDate("Booking date must be greater than or equal to today");
            }

            TourCustomer existingCustomer2 = tourCustomerRepository.findByEmailAndTourPackage(tourCustomer.getEmail(), tourCustomer.getTourPackage());
            if (existingCustomer2 != null && existingCustomer2.getId() != id) {
                throw new DuplicateBookingException("Customer "+ tourCustomer.getEmail() +" is already in the tour package "+ tourCustomer.getTourPackage());
            }
            
            existingCustomer.setName(tourCustomer.getName());
            existingCustomer.setEmail(tourCustomer.getEmail());
            existingCustomer.setTourPackage(tourCustomer.getTourPackage());
            existingCustomer.setBookingDate(tourCustomer.getBookingDate());
            return tourCustomerRepository.save(existingCustomer);
        }
        throw new CustomerNotFoundException("Customer not found with id: " + id);
    }

    // delete customer
    public void deleteCustomer(int id) {
        tourCustomerRepository.deleteById(id);
    }

    public List<String> fetchDataFromExternalAPI() throws Exception {
        
        // Fetch data from external api, ensure it fits alll our validation and 
        // constraint and save it in our database
        // Properly handle database failure
        // Use feign client for fetching the data
        List<String> errors = new ArrayList<String>();
        try {
            List<TourCustomer> customers = dataPullProxy.fetchDataFromExternalAPI();
            if (customers != null) {
                for (TourCustomer customer : customers) {

                    // Check if customer already exists
                    try{
                        TourCustomer existingCustomer = tourCustomerRepository.findByEmailAndTourPackage(customer.getEmail(), customer.getTourPackage());
                        if (existingCustomer != null) {
                            throw new DuplicateBookingException("Customer " + customer.getEmail() + " is already in the tour package " + customer.getTourPackage());
                        }

                        // Apply validations
                        try {
                            if (customer.getBookingDate().compareTo(new Date(System.currentTimeMillis())) < 0) {
                                throw new InvalidBookingDate("Booking date must be greater than or equal to today");
                            }
                        } catch (Exception e) {
                            throw new InvalidBookingDate("Booking date must be greater than or equal to today");
                        }
                        customer.setId(0);
                        tourCustomerRepository.save(customer);
                    }catch (DuplicateBookingException e) {
                        System.out.println("Customer " + customer.getEmail() + " not saved because of duplicate booking");
                        System.out.println(e.getMessage());
                        errors.add("Customer " + customer.getEmail() + " not saved because of duplicate booking");
                    }catch (InvalidBookingDate e) {
                        System.out.println("Customer " + customer.getEmail() + " not saved because of invalid booking date");
                        System.out.println(e.getMessage());
                        errors.add("Customer " + customer.getEmail() + " not saved because of invalid booking date");
                    }catch (Exception e) {
                        System.out.println("Customer " + customer.getEmail() + " not saved because of exceptions");
                        System.out.println(e.getMessage());
                        errors.add("Customer " + customer.getEmail() + " not saved because of exceptions");
                    }
                    
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to fetch data from external API");
            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        }
        return errors;
    }

}
