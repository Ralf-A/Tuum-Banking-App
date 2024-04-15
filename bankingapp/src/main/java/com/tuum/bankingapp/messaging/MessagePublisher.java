package com.tuum.bankingapp.messaging;

import com.tuum.bankingapp.model.Account;
import com.tuum.bankingapp.model.Transaction;
import org.slf4j.Logger;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Responsible for publishing messages to the RabbitMQ message broker.
 */
@Service
public class MessagePublisher {
    Logger log = org.slf4j.LoggerFactory.getLogger(MessagePublisher.class);

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessagePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // Publishes an account creation event to the RabbitMQ message broker.
    public void publishAccountEvent(Account event) {
        try {
            rabbitTemplate.convertAndSend("bankingExchange", "account.created", event);
            log.info("Published account creation event: {}", event);
        } catch (AmqpException e) {
            log.error("Failed to publish account creation event", e);
        }
    }

    // Publishes a transaction creation event to the RabbitMQ message broker.
    public void publishTransactionEvent(Transaction event) {
        try{
            rabbitTemplate.convertAndSend("bankingExchange", "transaction.created", event);
            log.info("Published transaction creation event: {}", event);
        } catch (AmqpException e) {
            log.error("Failed to publish transaction creation event", e);
        }
    }
}





