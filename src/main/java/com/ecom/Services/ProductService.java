package com.ecom.Services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import com.ecom.Forms.UpdateProductForm;
import com.ecom.Handlers.ExceptionDetails;
import com.ecom.Models.Product;
import com.ecom.Models.ProductRegister;
import com.ecom.Models.User;
import com.ecom.Models.DTOs.ProductDTO;
import com.ecom.Repository.ProductRepository;
import com.ecom.Repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ProductService {
    
    ProductRepository productRepository;
    UserRepository userRepository;

    public ProductService(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> addProduct(Product product) {
        System.out.println("Entered function");

        GetUserByTokenService getUser = new GetUserByTokenService(this.userRepository);
        User seller = getUser.run();

        if(seller == null) {
            ExceptionDetails exception = new ExceptionDetails("Bad Request", 400, 
                    "User does not exist", "Somehow the user is not valid :|", new Date());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
        }

        product.setUser(seller);

        try {
            System.out.println("Before save");
            productRepository.save(product);
            System.out.println("After save");
    
            ProductRegister productToRegist = new ProductRegister(product);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForObject("http://localhost:8080/product", productToRegist, 
                    ProductRegister.class);

            ProductDTO productDTO = new ProductDTO(seller, product);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
        }
        catch(Exception e) {
            ExceptionDetails exception = new ExceptionDetails("Bad Gateway", 502, 
                    "Could not communicate with Registering API.", 
                    "Try again or wait until server is up again.", new Date());

            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(exception);
        }
    }

    public ResponseEntity<?> getProducts(Pageable paging) {
        Page<Product> allProducts = productRepository.findAll(paging);
        Page<ProductDTO> pageWithProducts = ProductDTO.convertPage(allProducts);

        return ResponseEntity.ok().body(pageWithProducts.getContent());
    }

    public ResponseEntity<?> getProductByUser(String userName, Pageable paging) {
        Optional<User> foundUserOptional = userRepository.findByUserName(userName);

        if(!foundUserOptional.isPresent()) {
            
            ExceptionDetails exception = new ExceptionDetails("Bad Request", 400, 
            "User does not exist", "Please search for items of an existing user", new Date());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
        }

        Page<Product> listOfProductsOfUser = productRepository.findByUserUserId(
            foundUserOptional.get().getUserId(), paging);

        Page<ProductDTO> pageWithProducts = ProductDTO.convertPageByUser(listOfProductsOfUser, 
                foundUserOptional.get());
        
        return ResponseEntity.ok().body(pageWithProducts.getContent());
    }

    public ResponseEntity<?> removeProduct(String productId, String userName) {
        
        Product foundProduct = productRepository.findByProductId(productId);

        if(foundProduct == null) {
            ExceptionDetails exception = new ExceptionDetails("Not Found", 404, 
            "Product with ID does not exist.", "Please use the correct ID or check if product exists", 
            new Date());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception);
        }

        if(foundProduct.getUser().getUserName() != userName) {
            ExceptionDetails exception = new ExceptionDetails("Unauthourized", 401, 
                "You cannot remove another user's product.", 
                "If you're trying to remove a product of yours check the correct product ID.", 
                new Date());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception);
        }

        productRepository.deleteById(productId);
        
        ExceptionDetails exception = new ExceptionDetails("OK", 200, "Product successfully removed.", 
                foundProduct.getName() + " has been permanently removed from the database.", 
                new Date());

        return ResponseEntity.ok().body(exception);
    }

    public ResponseEntity<?> getProductsLikeByName(Pageable paging, String productName) {

        Page<Product> listOfProductsFound = productRepository.findByNameLike("%"+productName+"%", paging);
        Page<ProductDTO> pageWithProducts = ProductDTO.convertPage(listOfProductsFound);
            
        return ResponseEntity.ok().body(pageWithProducts.getContent());
    }

    public ResponseEntity<?> updateProduct(String productId, UpdateProductForm form) {

        GetUserByTokenService getUser = new GetUserByTokenService(this.userRepository);
        User user = getUser.run();

        Product foundProduct = productRepository.findByProductId(productId);
        
        if(foundProduct==null) {
            ExceptionDetails exception = new ExceptionDetails("Not Found", 404, 
            "The product you're trying to update has incorrect ID.", 
            "Check if the ID is correct or if the product exists.", new Date());
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception);
        }

        if(foundProduct.getUser().getUserName() != user.getUserName()) {
            ExceptionDetails exception = new ExceptionDetails("Bad Request", 400, 
                "You cannot update the product of another user.", 
                "Chech if the product ID corresponds to one of your products.", new Date());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
        }

        if(form.getValues().get("productId") != null) {
            ExceptionDetails exception = new ExceptionDetails("Bad Request", 400, 
                "You cannot update the product's ID.", 
                "If you want to update a field other than ID, remove 'productId' and add the required field.", 
                new Date());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
        }
        
        ProductDTO productDTO = new ProductDTO(user, foundProduct);
        for(String field : form.getValues().keySet()) {
            productDTO.tryToUpdate(field, form.getValues().get(field));
        }            
        foundProduct.setName(productDTO.getProduct().get("name"));
        foundProduct.setPrice(new BigDecimal(productDTO.getProduct().get("price")));
        foundProduct.setDescription(productDTO.getProduct().get("description"));

        try {
            ProductRegister productToUpdateInRegist = new ProductRegister(foundProduct);
            RestTemplate restTemplate = new RestTemplate();

            restTemplate.put("http://localhost:8080/product//updateProduct/" + foundProduct.getProductId(), 
                    productToUpdateInRegist, ProductRegister.class);      
                    
            productRepository.save(foundProduct);

            return ResponseEntity.ok().body(productDTO);      
        }
        catch(Exception e) {
            ExceptionDetails exception = new ExceptionDetails("Bad Gateway", 502, 
                    "Could not communicate with Registering API.", 
                    "Either the server is down or the ID is incorrect.", new Date());

            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(exception);
        }
    }
}
