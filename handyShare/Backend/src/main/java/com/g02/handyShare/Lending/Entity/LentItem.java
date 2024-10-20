package com.g02.handyShare.Lending.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class LentItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String availability;
    private String category;
    private String city;
    private String state;
    private String pincode;
    private String address;
    private String imageName;
    
    // Add other necessary fields, getters, and setters
}
