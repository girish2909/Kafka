package org.pih.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentConsumer {

    @KafkaListener(topics = "order-topic", groupId = "payment-group")
    public void consume(String message) {

        System.out.println("Received: " + message);

        // Simulate failure
        if (message.contains("fail")) {
            System.out.println("❌ Payment failed");
            throw new RuntimeException("Payment processing failed");
        }

        System.out.println("✅ Payment processed successfully");
    }
}