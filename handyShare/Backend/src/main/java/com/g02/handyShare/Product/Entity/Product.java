package com.g02.handyShare.Product.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

import com.g02.handyShare.User.Entity.User;

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

    private String productImage;

    @NotNull(message = "Rental Price is required.")
    @Min(value = 0, message = "Rental Price should be positive.")
    private Double rentalPrice;
 
    @ManyToOne
    @JoinColumn(name="lender", referencedColumnName = "id")
    private User lender; 

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

    @Column(nullable = true)
    private Boolean available = true;
}