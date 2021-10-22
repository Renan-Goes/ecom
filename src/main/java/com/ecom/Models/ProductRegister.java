package com.ecom.Models;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ProductRegister {

    @NotEmpty @NotBlank
    private String productId;

    @NotEmpty @NotBlank
    private String user;

    @NotEmpty @NotBlank
    private String price;
    
    @NotEmpty @NotBlank
    private String name;

    public ProductRegister(Product product) {
        this.productId = product.getProductId();
        this.user = product.getUser().getUserName();
        this.price = product.getPrice().toString();
        this.name = product.getName();
    }
    
    public Product convert() {
        return new Product(this.productId, new BigDecimal(this.price), this.name);
    }
}
