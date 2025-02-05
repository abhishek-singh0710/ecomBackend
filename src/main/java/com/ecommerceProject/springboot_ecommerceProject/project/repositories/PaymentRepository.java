package com.ecommerceProject.springboot_ecommerceProject.project.repositories;

import com.ecommerceProject.springboot_ecommerceProject.project.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
