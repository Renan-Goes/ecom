package com.ecom.Models.DTOs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ecom.Models.Product;
import com.ecom.Models.User;

import org.springframework.data.domain.Page;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ProductDTO {
    
    private Map<String, String> product = new HashMap<String, String>();
  
    public ProductDTO(User user, Product product) {
        this.product.put("productId", product.getProductId());    
        this.product.put("user", user.getUserName());
        this.product.put("name", product.getName());
        this.product.put("price", product.getPrice().toString());
        this.product.put("description", product.getDescription());
    }

    public static List<ProductDTO> convertList(List<Product> products) {
        return products.stream().map(product -> new ProductDTO(product.getUser(), product)).collect(Collectors.toList());
    }

    public static Page<ProductDTO> convertPageByUser(Page<Product> pageWithProducts, User user) {
        return pageWithProducts.map(product -> new ProductDTO(user, product));
    }

    public static Page<ProductDTO> convertPage(Page<Product> pageWithProducts) {
        return pageWithProducts.map(product -> new ProductDTO(product.getUser(), product));
    }

    public void tryToUpdate(String field, String value) {
        try {
            this.product.put(field, value);
        }
        catch(Exception e) {
        }
    }
    
}
