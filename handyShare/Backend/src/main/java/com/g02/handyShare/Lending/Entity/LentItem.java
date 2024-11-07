package com.g02.handyShare.Lending.Entity;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.g02.handyShare.User.Entity.User;

@Entity
@Data
public class LentItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Category is mandatory")
    private String category;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotNull(message = "Price is mandatory")
    @Min(value = 0, message = "Price must be non-negative")
    private Double price;

    @NotBlank(message = "City is mandatory")
    private String city;

    @NotBlank(message = "State is mandatory")
    private String state;

    @NotBlank(message = "Pincode is mandatory")
    private String pincode;

    @NotBlank(message = "Address is mandatory")
    private String address;

    private String imageName;

    @NotBlank(message = "Availability is mandatory")
    private String availability;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Add other necessary fields, getters, and setters
}
