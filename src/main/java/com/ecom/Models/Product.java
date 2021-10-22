package com.ecom.Models;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name="PRODUCTS")
@Getter @Setter @NoArgsConstructor
public class Product {
    
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy="uuid")
	@Column(name="PRODUCT_ID")
	private String productId;

    @Column
    private String name;
    @Column
    private BigDecimal price;
    @Column
    private String description;
    
    @ManyToOne()
    @JoinColumn(name="USER_ID", nullable=false, updatable=false)
    private User user;

    public Product(String name, BigDecimal price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }
}
