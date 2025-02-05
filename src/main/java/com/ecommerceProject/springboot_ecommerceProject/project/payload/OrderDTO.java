package com.ecommerceProject.springboot_ecommerceProject.project.payload;

import com.ecommerceProject.springboot_ecommerceProject.project.model.Address;
import com.ecommerceProject.springboot_ecommerceProject.project.model.OrderItem;
import com.ecommerceProject.springboot_ecommerceProject.project.model.Payment;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long orderId;
    private String email;
    private List<OrderItemDTO> orderItems;
    private LocalDate orderDate;
    private PaymentDTO payment;
    private Double totalAmount;
    private String orderStatus;
    private Long addressId;
}
