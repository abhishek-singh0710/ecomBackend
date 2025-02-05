package com.ecommerceProject.springboot_ecommerceProject.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;

    @NotBlank
    @Size(min = 3, message = "Product Name Must Contain Atleast 3 Characters")
    private String productName;

    @Size(min = 6, message = "Product Description Must Contain Atleast 6 Characters")
    private String description;
    private String image;
    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User user;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    // Using A List Since On The Cart Items Side It Is @ManyToOne So Many CartItems To One Product
    private List<CartItem> products = new ArrayList<>();
}
