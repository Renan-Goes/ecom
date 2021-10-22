package com.ecom.Models.DTOs;

import java.math.BigDecimal;
import java.util.List;

import com.ecom.Models.Wishlist;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class WishlistDTO {

    private BigDecimal totalPrice;
    private Long numberOfProducts;
    private List<ProductDTO> productsBought;
    private String buyerName;
    
    public WishlistDTO(Wishlist wishlist, String buyerName) {
        this.buyerName = buyerName;
        this.totalPrice = wishlist.getTotalPrice();
        this.numberOfProducts = wishlist.getNumberOfProducts();
        this.productsBought = ProductDTO.convertList(wishlist.getListOfProductsToBuy());
    }
}
