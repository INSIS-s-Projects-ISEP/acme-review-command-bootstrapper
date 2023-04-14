package com.isep.reviewcommandbootstrapper.messaging;

import java.io.IOException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.isep.reviewcommandbootstrapper.dto.mapper.ProductMapper;
import com.isep.reviewcommandbootstrapper.dto.message.ProductMessage;
import com.isep.reviewcommandbootstrapper.model.Product;
import com.isep.reviewcommandbootstrapper.repository.ProductRepository;
import com.rabbitmq.client.Channel;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class ProductConsumer {
    
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @RabbitListener(queues = {"#{productCreatedQueue.name}"}, ackMode = "MANUAL")
    public void productCreated (ProductMessage productMessage, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException{
        
        log.info("Product received: " + productMessage.getSku());

        log.info("OOHAK: " + productMessage);
        
        Product product = productMapper.toEntity(productMessage);
        log.info("OOHAK: " + product);
        
        productRepository.save(product);
        channel.basicAck(tag, false);
        
        log.info("Product created: " + product.getSku());
    }

    @RabbitListener(queues = {"#{productDeletedQueue.name}"}, ackMode = "MANUAL")
    public void productDeleted(ProductMessage productMessage, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException{
        log.info("Product received: " + productMessage.getSku());

        Product product = productMapper.toEntity(productMessage);
        productRepository.deleteBySku(product.getSku());
        channel.basicAck(tag, false);
        
        log.info("Product deleted: " + product.getSku());
    }

}
