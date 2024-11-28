package com.g02.handyShare.bookings.entity;

import java.time.LocalDateTime;
import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.User.Entity.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Bookings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user who is borrowing the product (unidirectional many-to-one)
    @ManyToOne
    @JoinColumn(name = "borrower", referencedColumnName = "id", nullable = false)
    private User borrower;

    // The product being borrowed (unidirectional many-to-one, allows reuse of the product for multiple borrows)
    @ManyToOne
    @JoinColumn(name = "product", referencedColumnName = "id", nullable = false)
    private Product product;

    // Duration in days
    private int duration;


    // Store the start date and time
    @Column(name = "timerStart")
    private LocalDateTime timerStart;

    // Store the end date and time
    @Column(name = "timerEnd")
    private LocalDateTime timerEnd;
 
   

    @Column(name = "returnDateTime")

    private LocalDateTime returnDateTime;

    // Penalty amount if applicable
    private Double penalty ;

    private String returnImage;

    private LocalDateTime returnByBorrowerTime;

    private Double totalPayment = 0.0;


  

    
}
