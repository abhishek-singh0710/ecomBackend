package com.ecommerceProject.springboot_ecommerceProject.project.service;


import com.ecommerceProject.springboot_ecommerceProject.project.exceptions.APIException;
import com.ecommerceProject.springboot_ecommerceProject.project.exceptions.ResourceNotFoundException;
import com.ecommerceProject.springboot_ecommerceProject.project.model.Cart;
import com.ecommerceProject.springboot_ecommerceProject.project.model.Category;
import com.ecommerceProject.springboot_ecommerceProject.project.model.Product;
import com.ecommerceProject.springboot_ecommerceProject.project.payload.CartDTO;
import com.ecommerceProject.springboot_ecommerceProject.project.payload.ProductDTO;
import com.ecommerceProject.springboot_ecommerceProject.project.payload.ProductResponse;
import com.ecommerceProject.springboot_ecommerceProject.project.repositories.CartRepository;
import com.ecommerceProject.springboot_ecommerceProject.project.repositories.CategoryRepository;
import com.ecommerceProject.springboot_ecommerceProject.project.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @Value("${image.base.url}")
    private String imageBaseUrl;

    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category", "cateogryId", categoryId));

        boolean isProductNotPresent = true;
        List<Product> products = category.getProducts();
        for (Product value : products) {
            if (value.getProductName().equals(productDTO.getProductName())) {
                isProductNotPresent = false;
                break;
            }
        }
        if(isProductNotPresent) {

            Product product = modelMapper.map(productDTO, Product.class);

            product.setImage("default.png");
            product.setCategory(category);
            double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
            product.setSpecialPrice(specialPrice);
            Product savedProduct = productRepository.save(product);
            return modelMapper.map(savedProduct, ProductDTO.class);
        }
        else {
            throw new APIException("Product Already Exists");
        }
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword, String category) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        // Filtering login Right Now Specifications Are Null No Filter Applied
        Specification<Product> spec = Specification.where(null);

        if(keyword!=null && !keyword.isEmpty()) {
            spec = spec.and((root, query,criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")), "%"+keyword.toLowerCase()+"%"));
        }

        if(category!=null && !category.isEmpty()) {
            spec = spec.and((root, query,criteriaBuilder) ->
                    criteriaBuilder.like(root.get("category").get("categoryName"), category));
        }

        // Now The ProductRepository Will Also Extend The JpaSpecificationExecutor Which Is An Interface To Allow The Execution Of A Specification
        // Based On The JPA Criteria API
        Page<Product> productPage = productRepository.findAll(spec, pageDetails);
        List<Product> products = productPage.getContent();

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> {
                    ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
                    productDTO.setImage(constructImageUrl(product.getImage()));
                    return productDTO;
                })
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);

        productResponse.setPageSize(productPage.getSize());
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());
        productResponse.setTotalElements(productPage.getTotalElements());
        return productResponse;
    }

    private String constructImageUrl(String imageName) {
        return imageBaseUrl.endsWith("/")? (imageBaseUrl+imageName) : (imageBaseUrl+"/"+imageName);
    }


    @Override
    public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        Page<Product> productPage = productRepository.findByCategoryOrderByPriceAsc(category, pageDetails);
        List<Product> products = productPage.getContent();
        if(products.isEmpty()) {
            throw new APIException("No Products Found");
        }
        ProductResponse productResponse = new ProductResponse();
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        productResponse.setContent(productDTOS);

        productResponse.setPageSize(productPage.getSize());
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());
        productResponse.setTotalElements(productPage.getTotalElements());
        return productResponse;
    }

    @Override
    public ProductResponse searchProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase("%" + keyword+ "%", pageDetails);

        List<Product> products = productPage.getContent();
        if(products.isEmpty()) {
            throw new APIException("No Products Found");
        }
        ProductResponse productResponse = new ProductResponse();
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));

        existingProduct.setProductName(product.getProductName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setDiscount(product.getDiscount());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setQuantity(product.getQuantity());
        // For Updating The Image Will Have A Different Separate API

        double newSpecialPrice = existingProduct.getPrice() - ((existingProduct.getDiscount()*0.01) * existingProduct.getPrice());
        existingProduct.setSpecialPrice(newSpecialPrice);

        Product savedProduct = productRepository.save(existingProduct);


        List<Cart> carts = cartRepository.findCartsByProductId(productId);
        // Mapping Each Cart To Cart DTO And Each CartItem To ProductDTO
        List<CartDTO> cartDTOs = carts.stream().map( cart -> {

            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

            List<ProductDTO> products = cart.getCartItems().stream()
                    .map( ci -> modelMapper.map(ci.getProduct(), ProductDTO.class))
                    .collect(Collectors.toList());

            cartDTO.setProducts(products);

            return cartDTO;
        }).collect(Collectors.toList());

        cartDTOs.forEach( cart -> cartService.updateProductInCarts(cart.getCartId(), productId));


        ProductDTO updatedProductDTO = modelMapper.map(savedProduct, ProductDTO.class);
        return updatedProductDTO;
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));

        // Also Delete The Product From The Cart
        List<Cart> carts = cartRepository.findCartsByProductId(productId);
        carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(), productId));

        productRepository.deleteById(productId);
        ProductDTO deletedProductDTO = modelMapper.map(product, ProductDTO.class);
        return deletedProductDTO;
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        // Get The Product From The Database
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));

        // Upload Image To Server /image Directory
        // Get The File Name Of The Uploaded Image
        String fileName = fileService.uploadImage(path, image);

        // Updating The New File Name To The Product
        // The Images In The Root Directory In The Server
        productFromDB.setImage(fileName);

        // Save The Updated Product
        Product updatedProduct = productRepository.save(productFromDB);

        // Return The ProductDTO After Mapping The Product To The ProductDTO
        ProductDTO updatedProductDTO = modelMapper.map(updatedProduct, ProductDTO.class);
        return updatedProductDTO;

    }

}
