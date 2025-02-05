package com.ecommerceProject.springboot_ecommerceProject.project.controller;

import com.ecommerceProject.springboot_ecommerceProject.project.model.Order;
import com.ecommerceProject.springboot_ecommerceProject.project.model.User;
import com.ecommerceProject.springboot_ecommerceProject.project.payload.OrderDTO;
import com.ecommerceProject.springboot_ecommerceProject.project.payload.OrderRequestDTO;
import com.ecommerceProject.springboot_ecommerceProject.project.service.OrderService;
import com.ecommerceProject.springboot_ecommerceProject.project.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private OrderService orderService;

    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(@PathVariable String paymentMethod, @RequestBody OrderRequestDTO orderRequestDTO) {
        String emailId = authUtil.loggedInEmail();

        OrderDTO order = orderService.placeOrder(
                emailId,
                orderRequestDTO.getAddressId(),
                paymentMethod,
                orderRequestDTO.getPgName(),
                orderRequestDTO.getPgPaymentId(),
                orderRequestDTO.getPgStatus(),
                orderRequestDTO.getPgResponseMessage()
        );

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/order/getAllOrders")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        User user = authUtil.loggedInUser();

        List<OrderDTO> orders = orderService.getAllOrders(user.getEmail());

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
