package com.ecom.Models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class Wishlist {

    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private String wishlistId;

    @OneToMany(cascade=CascadeType.ALL)
    private List<Product> listOfProductsToBuy = new ArrayList<>();

    @Column
    private BigDecimal totalPrice;
    
    @Column
    private Long numberOfProducts;

    public Wishlist(List<Product> listOfProductsToBuy) {
        this.listOfProductsToBuy = listOfProductsToBuy;
        this.totalPrice = listOfProductsToBuy.stream().map(product -> product.getPrice()).
                reduce(BigDecimal.ZERO, BigDecimal::add);
        
        this.numberOfProducts = Long.valueOf(listOfProductsToBuy.size());
    }
}
