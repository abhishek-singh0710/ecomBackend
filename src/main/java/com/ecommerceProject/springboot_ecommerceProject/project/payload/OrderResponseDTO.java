package com.ecommerceProject.springboot_ecommerceProject.project.payload;

public class OrderResponseDTO {

    public String orderId;
    public int amount;
    public String currency;

    public OrderResponseDTO(String orderId, int amount, String currency) {
        this.orderId = orderId;
        this.amount = amount;
        this.currency = currency;
    }
}
