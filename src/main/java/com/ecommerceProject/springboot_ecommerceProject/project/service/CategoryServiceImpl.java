package com.ecommerceProject.springboot_ecommerceProject.project.service;

import com.ecommerceProject.springboot_ecommerceProject.project.exceptions.APIException;
import com.ecommerceProject.springboot_ecommerceProject.project.exceptions.ResourceNotFoundException;
import com.ecommerceProject.springboot_ecommerceProject.project.model.Category;
import com.ecommerceProject.springboot_ecommerceProject.project.payload.CategoryDTO;
import com.ecommerceProject.springboot_ecommerceProject.project.payload.CategoryResponse;
import com.ecommerceProject.springboot_ecommerceProject.project.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{


    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
        List<Category> allCategories = categoryPage.getContent();

        if(allCategories.isEmpty()) {
            throw new APIException("No Categories Found");
        }
// So Here Using This Syntax But For The Entire List Of Category Items
        List<CategoryDTO> categoryDTOS = allCategories.stream()
                .map(category-> modelMapper.map(category, CategoryDTO.class))
                .toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);

        // Setting The Pagination Metadata
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);

        Category categoryFromDB = categoryRepository.findByCategoryName(category.getCategoryName());
        if(categoryFromDB!=null) {
            throw new APIException("Category With The Name "+category.getCategoryName()+" already Exists!");
        }
        Category savedCategory = categoryRepository.save(category);
        CategoryDTO savedCategoryDTO = modelMapper.map(savedCategory, CategoryDTO.class);
        return savedCategoryDTO;
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                // So This Will Actually Print Category Not Found With CategoryId: 1
                    .orElseThrow(()-> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        CategoryDTO toDeleteCategoryDTO = modelMapper.map(category, CategoryDTO.class);
        categoryRepository.delete(category);
        return toDeleteCategoryDTO;
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
        // Since The FindFirst() Function Returns Optional Object

        //        Optional<Category> optionalCategory = categories.stream()
        //                .filter(c -> c.getCategoryId()==(categoryId))
        //                .findFirst();

        Optional<Category> savedCategoryOptional = categoryRepository.findById(categoryId);
        Category savedCategory = savedCategoryOptional
                        .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));
        Category toSaveCategory = modelMapper.map(categoryDTO, Category.class);
        toSaveCategory.setCategoryId(categoryId);
        savedCategory = categoryRepository.save(toSaveCategory);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }
}
