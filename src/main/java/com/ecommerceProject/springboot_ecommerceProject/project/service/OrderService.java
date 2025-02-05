package com.ecommerceProject.springboot_ecommerceProject.project.service;

import com.ecommerceProject.springboot_ecommerceProject.project.payload.OrderDTO;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public interface OrderService {

    @Transactional
    OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage);

    List<OrderDTO> getAllOrders(@NotBlank @Size @Email String userEmail);
}
