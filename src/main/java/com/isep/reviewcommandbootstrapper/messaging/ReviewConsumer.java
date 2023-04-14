package com.isep.reviewcommandbootstrapper.messaging;

import java.io.IOException;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.isep.reviewcommandbootstrapper.dto.mapper.ReviewMapper;
import com.isep.reviewcommandbootstrapper.event.ReviewCreatedEvent;
import com.isep.reviewcommandbootstrapper.event.ReviewDeletedEvent;
import com.isep.reviewcommandbootstrapper.event.ReviewUpdatedEvent;
import com.isep.reviewcommandbootstrapper.model.Review;
import com.isep.reviewcommandbootstrapper.repository.ReviewRepository;
import com.rabbitmq.client.Channel;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class ReviewConsumer {

    private final CommandGateway commandGateway;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @RabbitListener(queues = "#{reviewCreatedQueue.name}", ackMode = "MANUAL")
    public void reviewCreated(ReviewCreatedEvent reviewCreatedEvent, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException{
        
        log.info("Review received: " + reviewCreatedEvent.getReviewId());
        try {
            commandGateway.send(reviewCreatedEvent);
            log.info("Review created: " + reviewCreatedEvent.getReviewId());
            channel.basicAck(tag, false);
        }
        catch (Exception e) {
            log.error("Fail to create review: " + reviewCreatedEvent.getReviewId());
            channel.basicNack(tag, false, true);
        }
    }

    public void reviewUpdated(ReviewUpdatedEvent reviewUpdatedEvent, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException{
        
        log.info("Review received: " + reviewUpdatedEvent.getReviewId());
        try {
            commandGateway.send(reviewUpdatedEvent);
            log.info("Review updated: " + reviewUpdatedEvent.getReviewId());
            channel.basicAck(tag, false);
        }
        catch (Exception e) {
            log.error("Fail to update review: " + reviewUpdatedEvent.getReviewId());
            channel.basicNack(tag, false, true);
        }
    }

    public void reviewDeleted(Long reviewId, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException{

        log.info("Review deleted received: " + reviewId);
        try {
            Review review = reviewRepository.findById(reviewId).orElseThrow();
            ReviewDeletedEvent reviewDeletedEvent = new ReviewDeletedEvent(review.getReviewId());
            commandGateway.send(reviewDeletedEvent);
            log.info("Review deleted: " + reviewId);
            channel.basicAck(tag, false);
        }
        catch (Exception e) {
            log.error("Fail to delete review: " + reviewId);
            channel.basicNack(tag, false, true);
        }
    }
    
}
