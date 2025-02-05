package com.ecommerceProject.springboot_ecommerceProject.project.repositories;

import com.ecommerceProject.springboot_ecommerceProject.project.model.Order;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {


    List<Order> findByEmail(@NotBlank @Size @Email String userEmail);
}
