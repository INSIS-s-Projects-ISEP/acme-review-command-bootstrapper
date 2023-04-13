package com.isep.reviewcommandbootstrapper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isep.reviewcommandbootstrapper.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
}