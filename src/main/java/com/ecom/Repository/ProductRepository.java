package com.ecom.Repository;

import com.ecom.Models.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    
    Product findByProductId(String productId);
    
    Page<Product> findByUserUserId(String userId, Pageable paging);

    Page<Product> findByNameLike(String name, Pageable paging);
}

