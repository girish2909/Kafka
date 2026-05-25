package org.pih.scheduler;

import org.pih.entity.OutboxEvent;
import org.pih.repository.OutboxRepository;
import org.pih.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OutboxScheduler {

    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private KafkaProducerService producerService;

    @Scheduled(fixedDelay = 5000)
    public void publishEvents() {

        List<OutboxEvent> events = outboxRepository.findByStatus("NEW");

        for (OutboxEvent event : events) {
            try {

                producerService.sendEvent("order-topic", event.getPayload());

                event.setStatus("SENT");
                outboxRepository.save(event);

            } catch (Exception e) {
                // log error (retry next cycle)
                System.out.println("Error sending to Kafka: " + e.getMessage());
            }
        }
    }
}