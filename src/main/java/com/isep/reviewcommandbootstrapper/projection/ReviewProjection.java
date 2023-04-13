package com.isep.reviewcommandbootstrapper.projection;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import com.isep.reviewcommandbootstrapper.event.ReviewCreatedEvent;
import com.isep.reviewcommandbootstrapper.event.ReviewDeletedEvent;
import com.isep.reviewcommandbootstrapper.event.ReviewUpdatedEvent;
import com.isep.reviewcommandbootstrapper.model.Product;
import com.isep.reviewcommandbootstrapper.model.Review;
import com.isep.reviewcommandbootstrapper.repository.ProductRepository;
import com.isep.reviewcommandbootstrapper.repository.ReviewRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ReviewProjection {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    
    @EventHandler
    public void on(ReviewCreatedEvent event){
        Product product = productRepository.findBySku(event.getSku()).orElseThrow();
        reviewRepository.save(new Review(
            event.getReviewId(),
            event.getApprovalStatus(),
            event.getReviewText(),
            event.getPublishingDate(),
            event.getFunFact(),
            product,
            event.getUser(),
            event.getRate()
            ));
        }
    
    @EventHandler
    public void on(ReviewUpdatedEvent event){

        Product product = productRepository.findBySku(event.getSku()).orElseThrow();
        Review review = reviewRepository.findById(event.getReviewId()).orElseThrow();

        review.setReviewId(event.getReviewId());
        review.setApprovalStatus(event.getApprovalStatus());
        review.setReviewText(event.getReviewText());
        review.setPublishingDate(event.getPublishingDate());
        review.setFunFact(event.getFunFact());
        review.setProduct(product);
        review.setUser(event.getUser());
        review.setRate(event.getRate());
        
        reviewRepository.save(review);

    }
    
    @EventHandler
    public void on(ReviewDeletedEvent event){
        reviewRepository.deleteById(event.getReviewId());
    }
    
}
