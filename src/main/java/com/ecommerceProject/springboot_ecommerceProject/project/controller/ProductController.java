package com.ecommerceProject.springboot_ecommerceProject.project.controller;

import com.ecommerceProject.springboot_ecommerceProject.project.config.AppConstants;
import com.ecommerceProject.springboot_ecommerceProject.project.model.Product;
import com.ecommerceProject.springboot_ecommerceProject.project.payload.ProductDTO;
import com.ecommerceProject.springboot_ecommerceProject.project.payload.ProductResponse;
import com.ecommerceProject.springboot_ecommerceProject.project.service.ProductService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid  @RequestBody ProductDTO productDTO,
                                                 @PathVariable Long categoryId) {
        ProductDTO addedProductDTO = productService.addProduct(productDTO, categoryId);
        return new ResponseEntity<ProductDTO>(addedProductDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(@RequestParam(name = "keyword", required = false) String keyword,
                                                          @RequestParam(name = "category", required = false) String category,
                                                          @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required=false) Integer pageNumber,
                                                          @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required=false) Integer pageSize,
                                                          @RequestParam(name="sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY) String sortBy,
                                                          @RequestParam(name="sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder) {
        ProductResponse productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder, keyword, category);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId, @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required=false) Integer pageNumber,
                                                                 @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required=false) Integer pageSize,
                                                                 @RequestParam(name="sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY) String sortBy,
                                                                 @RequestParam(name="sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder) {
        ProductResponse productResponse = productService.searchByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword, @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required=false) Integer pageNumber,
                                                                @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required=false) Integer pageSize,
                                                                @RequestParam(name="sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY) String sortBy,
                                                                @RequestParam(name="sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder) {
        ProductResponse productResponse = productService.searchProductsByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.FOUND);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long productId) {
        ProductDTO updatedProductDTO = productService.updateProduct(productId, productDTO);
        return new ResponseEntity<ProductDTO>(updatedProductDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId) {
        ProductDTO deletedProductDTO = productService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProductDTO, HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam("Image") MultipartFile image) throws IOException {
        ProductDTO updatedProductDTO = productService.updateProductImage(productId, image);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }
}
