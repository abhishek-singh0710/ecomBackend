package com.ecommerceProject.springboot_ecommerceProject.project.service;


import com.ecommerceProject.springboot_ecommerceProject.project.exceptions.APIException;
import com.ecommerceProject.springboot_ecommerceProject.project.exceptions.ResourceNotFoundException;
import com.ecommerceProject.springboot_ecommerceProject.project.model.*;
import com.ecommerceProject.springboot_ecommerceProject.project.payload.OrderDTO;
import com.ecommerceProject.springboot_ecommerceProject.project.payload.OrderItemDTO;
import com.ecommerceProject.springboot_ecommerceProject.project.repositories.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    CartService cartService;

    @Autowired
    ModelMapper modelMapper;

    @Value("${image.base.url}")
    private String imageBaseUrl;

    @Override
    // Either Be Executed Fully Or Not
    @Transactional
    public OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage) {
        // Validations, Create The Order, Create The Payment And Save It, Save The Order, Saving Payment First To Save The Payment Information In Order
        // Then Create The Order Items From The Cart Items, Save The Order Items, Then Change The Stock, Then Convert The Order Items Into Order Items DTO
        // And The Order Into OrderDTO


        // Right Now Only One Cart Per User, But If Multiple Carts Per User Then I Can Use FindCartByEmailAndId
        Cart cart = cartRepository.findCartByEmail(emailId);
        System.out.println("from place order "+ cart);
        if(cart == null) {
            throw new ResourceNotFoundException("Cart", "EmailId", emailId);
        }

        Address address = addressRepository.findById(addressId)
                .orElseThrow( () -> new ResourceNotFoundException("Address", "AddressId", addressId));

        Order order = new Order();
        order.setEmail(emailId);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("Order Accepted!");
        order.setAddress(address);

        Payment payment = new Payment(paymentMethod, pgPaymentId, pgStatus, pgResponseMessage,pgName);
        payment.setOrder(order);
        payment = paymentRepository.save(payment);

        order.setPayment(payment);

        Order savedOrder = orderRepository.save(order);

        // Also Need To Set The Order Items What Items Does This Order Have So Using The Cart Items To Create The Order Items
        List<CartItem> cartItems = cart.getCartItems();
        if(cartItems.isEmpty()) {
            throw new APIException("Cart Is Empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItem cartItem: cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrderedProductPrice(cartItem.getProductPrice());
            orderItem.setOrder(savedOrder);
            orderItem.setImage(cartItem.getImage());
            orderItems.add(orderItem);
        }

        orderItems = orderItemRepository.saveAll(orderItems);

        cart.getCartItems().forEach(item -> {
            int quantity = item.getQuantity();
            Product product = item.getProduct();

            product.setQuantity(product.getQuantity() - quantity);

            productRepository.save(product);

            // Since After Placing The Order I Am Removing The Product From The Cart
            cartService.deleteProductFromCart(cart.getCartId(), item.getProduct().getProductId());
        });

        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
        orderItems.forEach(item -> orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class)));

        orderDTO.setAddressId(addressId);

        return orderDTO;
    }

    @Override
    public List<OrderDTO> getAllOrders(@NotBlank @Size @Email String userEmail) {
        List<Order> ordersList = orderRepository.findByEmail(userEmail);

        List<OrderDTO> ordersDTOList = ordersList.stream()
                .map((order) -> {

                    List<OrderItemDTO> orderItemsDTO = order.getOrderItems().stream()
                            .map((orderItem) -> {
                                OrderItemDTO orderItemDTO = modelMapper.map(orderItem, OrderItemDTO.class);
                                orderItemDTO.setImage(constructImageUrl(orderItem.getImage()));
                                return orderItemDTO;
                            }).toList();

                    OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
                    orderDTO.setOrderItems(orderItemsDTO);
                    return orderDTO;
                }).toList();

        return ordersDTOList;
    }

    private String constructImageUrl(String imageName) {
        return imageBaseUrl.endsWith("/")? (imageBaseUrl+imageName) : (imageBaseUrl+"/"+imageName);
    }
}
