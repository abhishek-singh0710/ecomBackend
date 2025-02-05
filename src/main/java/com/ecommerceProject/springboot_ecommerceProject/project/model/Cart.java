package com.ecommerceProject.springboot_ecommerceProject.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "carts")
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @ToString.Exclude
    @OneToOne // One User Will Have One Cart
    @JoinColumn(name = "user_id")
    private User user;

    @ToString.Exclude
    @OneToMany(mappedBy = "cart", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    // Since Mapped By So It Will Not Have CartItem_Id In It's The CartItems Table Will Have The Cart_Id
    private List<CartItem> cartItems = new ArrayList<>();

    private Double totalPrice = 0.0;
}
