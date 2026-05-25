package org.pih.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DLQConsumer {

    @KafkaListener(topics = "order-topic-dlq", groupId = "dlq-group")
    public void consumeDLQ(String message) {

        System.out.println("🚨 Message in DLQ: " + message);

        // real world:
        // - alert
        // - store in DB
        // - reprocess manually
    }
}