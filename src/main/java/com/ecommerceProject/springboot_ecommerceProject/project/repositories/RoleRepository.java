package com.ecommerceProject.springboot_ecommerceProject.project.repositories;

import com.ecommerceProject.springboot_ecommerceProject.project.model.AppRole;
import com.ecommerceProject.springboot_ecommerceProject.project.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}

