package com.ecommerceProject.springboot_ecommerceProject.project.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

    private Long cartItemId;
    private CartDTO cart;
    private ProductDTO productDTO;
    private Integer quantity;
    private Double discount;
    private Double productPrice;
    private String image;
}
