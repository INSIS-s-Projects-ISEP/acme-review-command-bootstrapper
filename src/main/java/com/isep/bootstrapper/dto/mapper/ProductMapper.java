package com.isep.bootstrapper.dto.mapper;

import org.springframework.stereotype.Component;

import com.isep.bootstrapper.dto.message.ProductMessage;
import com.isep.bootstrapper.model.Product;

@Component
public class ProductMapper {
        
    public Product toEntity(ProductMessage productMessage){
        return new Product(
            productMessage.getProductId(),
            productMessage.getSku()
        );
    }
    
}
