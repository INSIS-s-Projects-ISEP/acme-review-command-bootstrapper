package com.isep.reviewcommandbootstrapper.aggregate;

import java.time.LocalDate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.isep.reviewcommandbootstrapper.enumarate.ApprovalStatus;
import com.isep.reviewcommandbootstrapper.event.ReviewCreatedEvent;
import com.isep.reviewcommandbootstrapper.event.ReviewDeletedEvent;
import com.isep.reviewcommandbootstrapper.event.ReviewUpdatedEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Aggregate
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewAggregate {
    
    @AggregateIdentifier
    private Long reviewId;
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;
    private String reviewText;
    private LocalDate publishingDate = LocalDate.now();
    private String funFact;
    private String sku;
    private String user;
    private Double rate = 0.0;

    @CommandHandler
    public ReviewAggregate(ReviewCreatedEvent event) {
        AggregateLifecycle.apply(event);
    }
    
    @EventSourcingHandler
    public void on(ReviewCreatedEvent event){
        this.reviewId = event.getReviewId();
        this.approvalStatus = event.getApprovalStatus();
        this.reviewText = event.getReviewText();
        this.publishingDate = event.getPublishingDate();
        this.funFact = event.getFunFact();
        this.sku = event.getSku();
        this.user = event.getUser();
        this.rate = event.getRate();
    }
    
    @CommandHandler
    public void handle(ReviewUpdatedEvent event){
        AggregateLifecycle.apply(event);
    }
    
    @EventSourcingHandler
    public void on(ReviewUpdatedEvent event){
        this.reviewId = event.getReviewId();
        this.approvalStatus = event.getApprovalStatus();
        this.reviewText = event.getReviewText();
        this.publishingDate = event.getPublishingDate();
        this.funFact = event.getFunFact();
        this.sku = event.getSku();
        this.user = event.getUser();
        this.rate = event.getRate();
    }

    @CommandHandler
    public void handle(ReviewDeletedEvent event){
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(ReviewDeletedEvent event){
        AggregateLifecycle.markDeleted();
    }
    
}
