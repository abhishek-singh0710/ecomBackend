package com.ecommerceProject.springboot_ecommerceProject.project.controller;

import com.ecommerceProject.springboot_ecommerceProject.project.config.AppConstants;
import com.ecommerceProject.springboot_ecommerceProject.project.model.Category;
import com.ecommerceProject.springboot_ecommerceProject.project.payload.CategoryDTO;
import com.ecommerceProject.springboot_ecommerceProject.project.payload.CategoryResponse;
import com.ecommerceProject.springboot_ecommerceProject.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private List<Category> categories = new ArrayList<>();

    @Autowired
    private CategoryService categoryService;

//    @GetMapping("/echo")
////    public ResponseEntity<String> echoMessage(@RequestParam(name = "message", defaultValue="Hello World") String message) {
//    public ResponseEntity<String> echoMessage(@RequestParam(name = "message", required = false) String message) {
//        return new ResponseEntity<String>("Echoed Message "+ message, HttpStatus.OK);
//    }

    @RequestMapping(value= "/public/categories", method = RequestMethod.GET)
    public ResponseEntity<CategoryResponse> getAllCategories(@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required=false) Integer pageNumber,
                                                             @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required=false) Integer pageSize,
                                                             @RequestParam(name="sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY) String sortBy,
                                                             @RequestParam(name="sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder) {
        CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);
//        return "Category Added Successfully";
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {
            CategoryDTO deletedCategoryDTO = categoryService.deleteCategory(categoryId);
            // Multiple Ways
//            return new ResponseEntity<>(status, HttpStatus.OK);
//            return ResponseEntity.ok(status);
            return ResponseEntity.status(HttpStatus.OK).body(deletedCategoryDTO);
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO, @PathVariable Long categoryId) {
            CategoryDTO updatedCategoryDTO = categoryService.updateCategory(categoryDTO, categoryId);
            return new ResponseEntity<>(updatedCategoryDTO, HttpStatus.OK);
    }
}
