//consume event
//try process
//retry count
//send to DLQ
package com.example.fintech.messaging.consumer;

import com.example.fintech.messaging.config.KafkaTopics;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.example.fintech.risk.RiskClient;

@Component
public class TransactionEventConsumer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final RiskClient riskClient;

    public TransactionEventConsumer(
                KafkaTemplate<String, String> kafkaTemplate,
                RiskClient riskClient
            ) {
        this.kafkaTemplate = kafkaTemplate;
        this.riskClient = riskClient;
    }

    @KafkaListener(
            topics = KafkaTopics.TRANSACTION_EVENTS,
            groupId = "transaction-group"
    )
    public void consume(String transactionId) {
        try {
            // simulate processing
            process(transactionId);
        } catch (Exception ex) {
            kafkaTemplate.send(
                    KafkaTopics.TRANSACTION_DLQ,
                    transactionId
            );
        }
    }

    private void process(String transactionId) {
        // downstream processing (email, ledger, etc.)
        // advisory call, never blocks money flow
        riskClient.assessRisk(transactionId);
    }
}
