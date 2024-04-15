package com.tuum.bankingapp.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Used to configure RabbitMQ connection, RabbitTemplate, MessageConverter, Queue, Exchange and Binding.
 */
@Configuration
public class RabbitMQConfig {
    // Values are taken from application.properties file
    @Value("${rabbitmq.host}")
    private String rabbitHost;
    @Value("${rabbitmq.username}")
    private String rabbitUsername;
    @Value("${rabbitmq.password}")
    private String rabbitPassword;

    // ConnectionFactory bean is created with rabbitHost, rabbitUsername and rabbitPassword values.
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitHost);
        connectionFactory.setUsername(rabbitUsername);
        connectionFactory.setPassword(rabbitPassword);
        return connectionFactory;
    }

    // RabbitTemplate bean is created with connectionFactory bean.
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    // Jackson2JsonMessageConverter bean is created.
    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Queue beans are created with queue names.
    @Bean
    public Queue accountQueue() {
        return new Queue("accountQueue", true);
    }
    @Bean
    public Queue transactionQueue() {
        return new Queue("transactionQueue", true);
    }

    // Exchange bean is created with exchange name.
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("bankingExchange");
    }

    // Binding beans are created with queue and exchange beans.
    @Bean
    public Binding accountBinding(Queue accountQueue, TopicExchange exchange) {
        return BindingBuilder.bind(accountQueue).to(exchange).with("account.*");
    }
    @Bean
    public Binding transactionBinding(Queue transactionQueue, TopicExchange exchange) {
        return BindingBuilder.bind(transactionQueue).to(exchange).with("transaction.*");
    }
}
