package com.ecommerceProject.springboot_ecommerceProject.project.service;

import com.ecommerceProject.springboot_ecommerceProject.project.exceptions.APIException;
import com.ecommerceProject.springboot_ecommerceProject.project.exceptions.ResourceNotFoundException;
import com.ecommerceProject.springboot_ecommerceProject.project.model.Cart;
import com.ecommerceProject.springboot_ecommerceProject.project.model.CartItem;
import com.ecommerceProject.springboot_ecommerceProject.project.model.Product;
import com.ecommerceProject.springboot_ecommerceProject.project.model.User;
import com.ecommerceProject.springboot_ecommerceProject.project.payload.CartDTO;
import com.ecommerceProject.springboot_ecommerceProject.project.payload.ProductDTO;
import com.ecommerceProject.springboot_ecommerceProject.project.repositories.CartItemRepository;
import com.ecommerceProject.springboot_ecommerceProject.project.repositories.CartRepository;
import com.ecommerceProject.springboot_ecommerceProject.project.repositories.ProductRepository;
import com.ecommerceProject.springboot_ecommerceProject.project.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    // Auth Util Is A Class Which Will Help Me Work With The Currently Logged In User
    private AuthUtil authUtil;

//    @Transactional
//    @Override
//    public CartDTO addProductToCart(Long productId, Integer quantity) {
//        // If The User Does Not Have A Cart Then Create A New Cart Otherwise Find The Existing Cart
//        Cart cart = createCart();
//
//        // Retrieve The Product Details
//        Product product = productRepository.findById(productId)
//                .orElseThrow( () -> new ResourceNotFoundException("Product", "ProductId", productId));
//
//        // Perform Validations Check If Product Already In Cart, Check If The Product Is Available
//        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);
//
//        if(cartItem != null) {
//            throw new APIException("Product "+product.getProductName()+" Already Exists In The Cart");
//        }
//        if(product.getQuantity() == 0) {
//            throw new APIException("Product "+product.getProductName()+" Is Not Available");
//        }
//        if(product.getQuantity() < quantity) {
//            throw new APIException("Please Make An Order Of The Product "+product.getProductName()+" Less Than Or Equal To The Quantity "+product.getQuantity());
//        }
//
//        // Create Cart Item
//        CartItem newCartItem = new CartItem();
//
//        // Save The Cart Item
//        newCartItem.setProduct(product);
//        newCartItem.setCart(cart);
////        int finalQuantity = quantity;
////        if(cartItem!=null) {
////            finalQuantity = finalQuantity + cartItem.getQuantity();
////            long id = cartItem.getCartItemId();
////            System.out.println("Previous Cart Items Id " + id);
////            cartItemRepository.deleteById(id);
////        }
//        newCartItem.setQuantity(quantity);
//        newCartItem.setDiscount(product.getDiscount());
//        newCartItem.setProductPrice(product.getSpecialPrice());
//
//        cartItemRepository.save(newCartItem);
//
//        // Not Reducing The Stock Here Reducing It When The Order Will Be Placed
//        product.setQuantity(product.getQuantity());
//
//        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
//
//        cartRepository.save(cart);
//
//        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
//
//        List<CartItem> cartItems = cart.getCartItems();
//
//        Stream<ProductDTO> productStream = cartItems.stream().map(item -> {
//            ProductDTO prodDto = modelMapper.map(item.getProduct(), ProductDTO.class);
//            prodDto.setQuantity(item.getQuantity());
//            return prodDto;
//        });
//
//        cartDTO.setProducts(productStream.toList());
//
//        return cartDTO;
//        // Add The Cart Item To The Cart And Return The Updated Cart
//        // Will Reduce The Quantity Of Product In The Stocks After The Order Has Been Placed
//    }

//@Transactional
//@Override
//public CartDTO addProductToCart(Long productId, Integer quantity) {
//    Cart cart = createCart();  // This method now correctly handles duplicates
//
//    Product product = productRepository.findById(productId)
//            .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));
//
//    CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);
//
////    if (cartItem != null) {
//////        System.out.println("found a cartItem");
////        throw new APIException("Product " + product.getProductName() + " Already Exists In The Cart");
////    }
//
//    if (product.getQuantity() == 0) {
//        throw new APIException("Product " + product.getProductName() + " Is Not Available");
//    }
//
//    if (product.getQuantity() < quantity) {
//        throw new APIException("Please Make An Order Of The Product " + product.getProductName() +
//                " Less Than Or Equal To The Quantity " + product.getQuantity());
//    }
//
//    // Create Cart Item
//    CartItem newCartItem = new CartItem();
//    newCartItem.setProduct(product);
//    newCartItem.setCart(cart);
//    int finalQuantity = quantity;
//        if(cartItem!=null) {
//            finalQuantity = finalQuantity + cartItem.getQuantity();
//            long id = cartItem.getCartItemId();
//            System.out.println("Previous Cart Items Id " + id);
//            cartItemRepository.deleteById(id);
//        }
//    newCartItem.setQuantity(finalQuantity);
//    newCartItem.setDiscount(product.getDiscount());
//    newCartItem.setProductPrice(product.getSpecialPrice());
//
//    cartItemRepository.save(newCartItem);
//
//    cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
//    cartRepository.save(cart);
//
//    CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
//    List<CartItem> cartItems = cart.getCartItems();
//
//    cartDTO.setProducts(cartItems.stream().map(item -> {
//        ProductDTO prodDto = modelMapper.map(item.getProduct(), ProductDTO.class);
//        prodDto.setQuantity(item.getQuantity());
//        return prodDto;
//    }).toList());
//
//    return cartDTO;
//}

//@Transactional
//@Override
//public CartDTO addProductToCart(Long productId, Integer quantity) {
//    Cart cart = createCart();  // Ensures the user has a cart
//
//    Product product = productRepository.findById(productId)
//            .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));
//
//    CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);
//
//    if (product.getQuantity() == 0) {
//        throw new APIException("Product " + product.getProductName() + " Is Not Available");
//    }
//
//    if (product.getQuantity() < quantity) {
//        throw new APIException("Please Make An Order Of The Product " + product.getProductName() +
//                " Less Than Or Equal To The Quantity " + product.getQuantity());
//    }
//
//    int finalQuantity = quantity;
//
//    if (cartItem != null) {
//        // Update the existing cart item instead of deleting it
//        finalQuantity += cartItem.getQuantity();
//        cartItem.setQuantity(finalQuantity);
//        cartItemRepository.save(cartItem); // Save the updated quantity
//    } else {
//        // Create new Cart Item
//        CartItem newCartItem = new CartItem();
//        newCartItem.setProduct(product);
//        newCartItem.setCart(cart);
//        newCartItem.setQuantity(finalQuantity);
//        newCartItem.setDiscount(product.getDiscount());
//        newCartItem.setProductPrice(product.getSpecialPrice());
//
//        cartItemRepository.save(newCartItem);
//    }
//
//    cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
//    cartRepository.save(cart);
//
//    CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
//    List<CartItem> cartItems = cart.getCartItems();
//
//    cartDTO.setProducts(cartItems.stream().map(item -> {
//        ProductDTO prodDto = modelMapper.map(item.getProduct(), ProductDTO.class);
//        prodDto.setQuantity(item.getQuantity());
//        return prodDto;
//    }).toList());
//
//    return cartDTO;
//}
//
//
//private Cart createCart() {
//    User user = authUtil.loggedInUser();  // Get the logged-in user
//
//    // Find the cart for this user
//    Cart existingCart = cartRepository.findCartByUserId(user.getUserId());
//
//    if (existingCart != null) {
//        System.out.println("Found an existing cart for user: " + user.getUserId());
//        return existingCart;
//    }
//
//    // Create a new cart only if no existing cart is found
//    Cart newCart = new Cart();
//    newCart.setTotalPrice(0.00);
//    newCart.setUser(user);
//
//    return cartRepository.save(newCart);  // Save and return the new cart
//}

@Transactional
@Override
public CartDTO addProductToCart(Long productId, Integer quantity) {
    Cart cart = createCart();  // Ensures the user has a cart

    Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));

    CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);

    if (product.getQuantity() == 0) {
        throw new APIException("Product " + product.getProductName() + " is not available");
    }

    if (product.getQuantity() < quantity) {
        throw new APIException("Please order product " + product.getProductName() +
                " with quantity ≤ " + product.getQuantity());
    }

    int finalQuantity = quantity;

//    if (cartItem != null) {
//        finalQuantity += cartItem.getQuantity();
//        cartItem.setQuantity(finalQuantity);
//        cartItemRepository.save(cartItem);
//    } else {
        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(finalQuantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());
        newCartItem.setImage(product.getImage());

        cartItemRepository.save(newCartItem);
//    }

    cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
    cartRepository.save(cart);

    return convertToCartDTO(cart);
}

    private CartDTO convertToCartDTO(Cart cart) {
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        cartDTO.setProducts(cart.getCartItems().stream().map(item -> {
            ProductDTO prodDto = modelMapper.map(item.getProduct(), ProductDTO.class);
            prodDto.setQuantity(item.getQuantity());
            return prodDto;
        }).toList());
        return cartDTO;
    }

    private Cart createCart() {
        User user = authUtil.loggedInUser();  // Get the logged-in user

        Cart existingCart = cartRepository.findCartByUserId(user.getUserId());

        if (existingCart != null) {
            return existingCart; // Return existing cart
        }

        // Create a new cart if not found
        Cart newCart = new Cart();
        newCart.setTotalPrice(0.00);
        newCart.setUser(user);

        return cartRepository.save(newCart);  // Save and return the new cart
    }



    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();

        if(carts.isEmpty()) {
            throw new APIException("No Cart Exists");
        }

        // Converting The List<Cart> Into List<CartDTO>
        // Also Doing This For Every Product As Well
        List<CartDTO> cartDTOs = carts.stream().map( cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

            cart.getCartItems().forEach( c -> c.getProduct().setQuantity(c.getQuantity()));

            // Doing This So That It Shows The Quantity Of The Cart Items Not The Total Quantity Of The Product In The Stock
            // So Changing The Quantity To The Cart Item Quantity
            List<ProductDTO> products = cart.getCartItems().stream().map(cartItem -> {
                ProductDTO productDTO = modelMapper.map(cartItem.getProduct(), ProductDTO.class);
                productDTO.setQuantity(cartItem.getQuantity());
                return productDTO;
            }).collect(Collectors.toList());

            cartDTO.setProducts(products);

            return cartDTO;
        }).collect(Collectors.toList());

        return cartDTOs;
    }

    @Override
    public CartDTO getCart(String email, Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(email, cartId);
        if(cart == null) {
            throw new ResourceNotFoundException("Cart", "CartId", cartId);
        }
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        // Updating The Quantity Here Iterating Through Each CartItem In The Cart Object
        // And Then Setting The Quantity Of Each Corresponding Product Object To Ensure That The Product Objects In The Cart
        // Have Their Quantities Properly Set
        cart.getCartItems().forEach( c -> c.getProduct().setQuantity(c.getQuantity()));

        List<ProductDTO> products = cart.getCartItems().stream()
                .map( ci -> modelMapper.map(ci.getProduct(), ProductDTO.class))
            .collect(Collectors.toList());

        cartDTO.setProducts(products);
        return cartDTO;
    }

    // Makes Sure That The Method Runs In A Transactional Context Either It Will Run Completely Or It Will Be Rolled Back
    @Transactional
    @Override
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {
        String emailId = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByEmail(emailId);
        Long cartId = userCart.getCartId();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow( () -> new ResourceNotFoundException("Cart", "CartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow( () -> new ResourceNotFoundException("Product", "ProductId", productId));

        if(product.getQuantity() == 0) {
            throw new APIException(product.getProductName()+" Is Not Available");
        }

//        if(product.getQuantity() < quantity) {
//            throw new APIException("Please Make An Order Of The Product "+product.getProductName()+ " Less Than Or Equal To The Quantity "+quantity);
//        }

        // Finding The CartItem Of That User's Cart And Of That Specific Product
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

        // Can Also Add The Item In The Cart If Not Present
        if(cartItem == null) {
            throw new APIException("Product "+product.getProductName()+ " Is Not Available In The Cart");
        }

        if(product.getQuantity() < (quantity + cartItem.getQuantity())) {
            throw new APIException("Please Make An Order Of The Product "+product.getProductName()+ " Less Than Or Equal To The Quantity "+quantity);
        }

        int newQuantity = cartItem.getQuantity() + quantity;

        if(newQuantity <= 0) {
            deleteProductFromCart(cartId, productId);
        }
        else {

            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setDiscount(product.getDiscount());
            cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice() * quantity));
        }

        cartRepository.save(cart);

        CartItem updatedItem = cartItemRepository.save(cartItem);
        // If The Quantity Of The Updated Cart Item Is Zero Then Deleting That Cart Item
        if(updatedItem.getQuantity() == 0) {
            cartItemRepository.deleteById(updatedItem.getCartItemId());
        }

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDTO> productDTOStream = cartItems.stream().map( item -> {
            ProductDTO prod = modelMapper.map(item.getProduct(), ProductDTO.class);
            prod.setQuantity(item.getQuantity());
            return prod;
        });

        cartDTO.setProducts(productDTOStream.toList());

        return cartDTO;
    }

    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow( () -> new ResourceNotFoundException("Cart", "CartId", cartId));

        // Product Just Shows Any Product Or Item Such As A Book, A Mobile Phone, A Laptop, A Cloth, etc.
        // Cart Item Is That Entity In The Shopping Cart Which Contains The Information About Any Specific Product, Its Quantity, The Total Price,etc.
        // So Just Removing The Product From The Cart Item That Is, Deleting That Specific Product's Corresponding Cart Item In The Shopping Cart
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

        if(cartItem == null) {
            throw new ResourceNotFoundException("Product", "ProductId", productId);
        }

        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice()*cartItem.getQuantity()));

        Product product = cartItem.getProduct();
        // Removing The Product From The Cart So Returning The Product Quantity To The Stock Inventory
        // But No Need To Actually Do This Since While Adding The Product To The Cart Not Reducing The Quantity Of The Stock
//        product.setQuantity(product.getQuantity() + cartItem.getQuantity());

        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);

        return "Product " + cartItem.getProduct().getProductName() + " Removed From The Cart";
    }


    @Override
    public void updateProductInCarts(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow( () -> new ResourceNotFoundException("Cart", "CartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow( () -> new ResourceNotFoundException("Product", "ProductId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

        if(cartItem == null) {
            throw new APIException("Product "+ product.getProductName()+ " Is Not Available In The Cart!");
        }

        // This Function Is Being Called By The Update Product Which Just Updates The Product Details The Quantity Is Not Being Updated
        // So Do Not Worry About The Total Price Being Same Or Not
        double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());
        cartItem.setProductPrice(product.getSpecialPrice());
        cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItem = cartItemRepository.save(cartItem);

    }
}
