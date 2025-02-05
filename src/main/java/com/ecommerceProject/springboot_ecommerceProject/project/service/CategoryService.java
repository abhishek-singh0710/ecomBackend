package com.ecommerceProject.springboot_ecommerceProject.project.service;

import com.ecommerceProject.springboot_ecommerceProject.project.model.Category;
import com.ecommerceProject.springboot_ecommerceProject.project.payload.CategoryDTO;
import com.ecommerceProject.springboot_ecommerceProject.project.payload.CategoryResponse;

// Using An Interface To Promote Loose Coupling And Modularity
public interface CategoryService {

    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO deleteCategory(Long categoryId);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);
}
