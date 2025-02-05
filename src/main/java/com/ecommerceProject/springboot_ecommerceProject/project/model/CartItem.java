package com.ecommerceProject.springboot_ecommerceProject.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Table(name = "cart_items")
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @ToString.Exclude
    @ManyToOne  // Since There Can Be Many Cart Items In One Cart
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    // The Name Of The Join Column Will Be Product_ID
    // This Table Is The Owner Of The Relationship Since Here The Join Column Is Specified
    // So This Table Will Store The Corresponding Cart Information This Table Will Store The Cart_Id Foreign Key
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;
    private double discount;
    private double productPrice;

    private String image;
}
