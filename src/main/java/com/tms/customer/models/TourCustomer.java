package com.tms.customer.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Table(name = "tour_customer", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"email", "tour_package"})
})
@Data
public class TourCustomer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @Column(name = "email")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @Column(name = "tour_package")
    @NotEmpty(message = "Tour package cannot be empty")
    private String tourPackage;

    @Column(name = "booking_date")
    @FutureOrPresent(message = "Booking date must be in the future or present")
    private Date bookingDate;
}
