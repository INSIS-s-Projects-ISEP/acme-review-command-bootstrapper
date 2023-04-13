package com.isep.reviewcommandbootstrapper.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Product {
    
    @Id
    private Long productID;
    private String sku;

    public Product(String sku) {
        this.sku = sku;
    }

}
