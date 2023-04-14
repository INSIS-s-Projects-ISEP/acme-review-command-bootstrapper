package com.isep.reviewcommandbootstrapper.dto.mapper;

import org.springframework.stereotype.Component;

import com.isep.reviewcommandbootstrapper.dto.message.ProductMessage;
import com.isep.reviewcommandbootstrapper.model.Product;

@Component
public class ProductMapper {
        
    public Product toEntity(ProductMessage productMessage){
        return new Product(
            productMessage.getProductId(),
            productMessage.getSku()
        );
    }
    
}
