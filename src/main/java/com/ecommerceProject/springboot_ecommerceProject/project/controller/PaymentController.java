package com.ecommerceProject.springboot_ecommerceProject.project.controller;

import com.ecommerceProject.springboot_ecommerceProject.project.payload.OrderRequestDTO;
import com.ecommerceProject.springboot_ecommerceProject.project.payload.OrderResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${razorpay_key_id}")
    String razorpayId;

    @Value("${razorpay_key_secret}")
    String razorpaySecret;

    @Value("${razorpay_currency}")
    String currency;

    @GetMapping("/{amount}")
    public ResponseEntity<OrderResponseDTO> payment(@PathVariable String amount) throws RazorpayException {
        RazorpayClient razorpayClient = new RazorpayClient(razorpayId, razorpaySecret);
        JSONObject jsonObject = new JSONObject();
        int amt = Integer.parseInt(amount);
        jsonObject.put("amount", amt);
        jsonObject.put("currency", currency);
        jsonObject.put("receipt", "order_receipt_100");

        Order order = razorpayClient.orders.create(jsonObject);

        System.out.println("order= "+order);

        String orderId = order.get("id");

        System.out.println("Called OrderId="+orderId);
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO(order.get("id"), order.get("amount"), order.get("currency"));
        return new ResponseEntity<OrderResponseDTO>(orderResponseDTO, HttpStatus.OK);
    }
}
