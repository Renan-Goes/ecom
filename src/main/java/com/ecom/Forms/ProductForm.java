package com.ecom.Forms;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ecom.Models.Product;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductForm {
    
    @NotEmpty
    @NotBlank
    @Size(min=1, max=50)
    private String name;

    @NotNull
    @DecimalMin(value="0.01", message="The product should have positive, non-zero value")
    private BigDecimal price;

    @NotBlank
    @Size(min=0, max=1000)
    private String description;

    public ProductForm(String name, BigDecimal price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public Product convert() {
        return new Product(name, price, description);
    }
}
