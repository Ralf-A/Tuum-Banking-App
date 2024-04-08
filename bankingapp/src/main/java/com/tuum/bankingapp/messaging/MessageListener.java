package com.tuum.bankingapp.messaging;

import com.tuum.bankingapp.model.Account;
import com.tuum.bankingapp.model.Transaction;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class MessageListener {

    @RabbitListener(queues = "accountQueue")
    public void handleAccountEvent(Account event) {
        // Process account event
    }

    @RabbitListener(queues = "transactionQueue")
    public void handleTransactionEvent(Transaction event) {
        // Process transaction event
    }
}
