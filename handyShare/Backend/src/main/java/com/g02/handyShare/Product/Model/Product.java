package com.g02.handyShare.Product.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="Product name is required.")
    private String name;

    @NotBlank(message="Description is required.")
    private String description;

    @NotBlank(message="Category is required.")
    private String category;

    @NotNull(message="Rental Price is required.")
    private Double rentalPrice;

    @NotNull(message="Availability is required.")
    private Boolean available;
}
