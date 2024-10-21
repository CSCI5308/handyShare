package com.g02.handyShare.Category.Entity;

import com.g02.handyShare.Product.Entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Table(name="newlyAdded")
public class NewlyAdded {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id") // Foreign key to Product
    private Product product;  // This associates Trending with Product
}
