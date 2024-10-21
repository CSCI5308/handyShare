package com.g02.handyShare.Product.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

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

    private String description;

    @NotBlank(message="Category is required.")
    private String category;

    @NotNull(message = "Rental Price is required.")  // Use @NotNull for Double
    @Min(value = 0, message = "Rental Price should be positive.")
    private Double rentalPrice;

    @NotNull(message="Availability is required.")
    private Boolean available;

    private Long userId;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
    this.createdDate = LocalDateTime.now();
}

}
