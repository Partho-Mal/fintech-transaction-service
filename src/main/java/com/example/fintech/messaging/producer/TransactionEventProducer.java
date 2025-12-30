//TransactionEventProducer
//listen after commit
//publish transaction id
package com.example.fintech.messaging.producer;

import com.example.fintech.domain.Transaction;
import com.example.fintech.messaging.config.KafkaTopics;
//import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;


@Component
public class TransactionEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public TransactionEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishTransactionEvent(Transaction transaction) {
        kafkaTemplate.send(
                KafkaTopics.TRANSACTION_EVENTS,
                transaction.getId().toString()
        );
    }
}

