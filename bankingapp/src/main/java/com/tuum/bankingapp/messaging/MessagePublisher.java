package com.tuum.bankingapp.messaging;

import com.tuum.bankingapp.model.Account;
import com.tuum.bankingapp.model.Transaction;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessagePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishAccountEvent(Account event) {
        rabbitTemplate.convertAndSend("bankingExchange", "account.created", event);
    }

    public void publishTransactionEvent(Transaction event) {
        rabbitTemplate.convertAndSend("bankingExchange", "transaction.created", event);
    }
}





