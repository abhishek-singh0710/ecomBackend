package com.ecommerceProject.springboot_ecommerceProject.project.repositories;

import com.ecommerceProject.springboot_ecommerceProject.project.model.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// From JPA I Get Two Interfaces The CrudRepository And The JpaRepository
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByCategoryName(@NotBlank @Size(min=5, message="Category Name Must Contain Atleast 5 Characters") String categoryName);
}
