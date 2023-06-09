package com.isep.bootstrapper.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RabbitmqConfig {

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
        Jackson2JsonMessageConverter jackson2JsonMessageConverter) {

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
        return rabbitTemplate;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> applicationListener(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

    // Review Created
    @Bean
    public FanoutExchange reviewCreatedExchange() {
        return new FanoutExchange("review.review-created");
    }
    
    @Bean
    public Queue reviewCreatedQueue() {
        return new Queue("review.review-created.review-command-bootstrapper");
    }

    @Bean
    public Binding bindingReviewCreatedtoReviewCreated(FanoutExchange reviewCreatedExchange,
            Queue reviewCreatedQueue) {
        return BindingBuilder.bind(reviewCreatedQueue).to(reviewCreatedExchange);
    }

    // Review Updated
    @Bean
    public FanoutExchange reviewUpdatedExchange() {
        return new FanoutExchange("review.review-updated");
    }
    
    @Bean
    public Queue reviewUpdatedQueue() {
        return new Queue("review.review-updated.review-command-bootstrapper");
    }

    @Bean
    public Binding bindingReviewUpdatedtoReviewUpdated(FanoutExchange reviewUpdatedExchange,
            Queue reviewUpdatedQueue) {
        return BindingBuilder.bind(reviewUpdatedQueue).to(reviewUpdatedExchange);
    }

    // Review Deleted
    @Bean
    public FanoutExchange reviewDeletedExchange() {
        return new FanoutExchange("review.review-deleted");
    }
    
    @Bean
    public Queue reviewDeletedQueue() {
        return new Queue("review.review-deleted.review-command-bootstrapper");
    }

    @Bean
    public Binding bindingReviewDeletedtoReviewDeleted(FanoutExchange reviewDeletedExchange,
            Queue reviewDeletedQueue) {
        return BindingBuilder.bind(reviewDeletedQueue).to(reviewDeletedExchange);
    }

    // SAGA
    // Fanout Exchange and Queues to receive created temporary votes
    @Bean
    public FanoutExchange temporaryVoteCreatedExchange() {
        return new FanoutExchange("temporary-vote.temporary-vote-created");
    }

    @Bean
    public Queue temporaryVoteCreatedQueue() {
        return new Queue("temporary-vote.temporary-vote-created.review-command-bootstrapper");
    }

    @Bean
    public Binding bindingTemporaryVoteCreatedToTemporaryVoteCreated(FanoutExchange temporaryVoteCreatedExchange,
            Queue temporaryVoteCreatedQueue) {
        return BindingBuilder.bind(temporaryVoteCreatedQueue).to(temporaryVoteCreatedExchange);
    }

    // Direct exchange and a queue to receive review created for a temporary vote
    @Bean
    public FanoutExchange reviewCreatedForTemporaryVoteExchange() {
        return new FanoutExchange("review.review-created-for-temporary-vote");
    }

    @Bean
    public Queue reviewCreatedForTemporaryVoteQueue() {
        return new Queue("review.review-created-for-temporary-vote.review-command-bootstrapper");
    }

    @Bean
    public Binding bindingReviewCreatedForTemporaryVoteToReviewCreatedForTemporaryVote(FanoutExchange reviewCreatedForTemporaryVoteExchange,
            Queue reviewCreatedForTemporaryVoteQueue) {
        return BindingBuilder.bind(reviewCreatedForTemporaryVoteQueue).to(reviewCreatedForTemporaryVoteExchange);
    }

    // Bootstrapper
    // Product
    @Bean
    public FanoutExchange rpcProductExchange(){
        return new FanoutExchange("rpc.product.review-command-bootstrapper");
    }

    @Bean
    public Queue rpcProductQueue(){
        return new Queue("rpc.product.review-command-bootstrapper");
    }

    @Bean
    public Binding bindRpcProduct(FanoutExchange rpcProductExchange, Queue rpcProductQueue){
        return BindingBuilder.bind(rpcProductQueue).to(rpcProductExchange);
    }

    // Review
    @Bean
    public FanoutExchange rpcReviewExchange(){
        return new FanoutExchange("rpc.review.review-command-bootstrapper");
    }

    @Bean
    public Queue rpcReviewQueue(){
        return new Queue("rpc.review.review-command-bootstrapper");
    }

    @Bean
    public Binding bindRpcReview(FanoutExchange rpcReviewExchange, Queue rpcReviewQueue){
        return BindingBuilder.bind(rpcReviewQueue).to(rpcReviewExchange);
    }
}