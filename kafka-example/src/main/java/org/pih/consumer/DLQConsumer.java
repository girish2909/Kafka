package org.pih.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pih.entity.FailedMessage;
import org.pih.repository.FailedMessageRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class DLQConsumer {

    private final FailedMessageRepository failedMessageRepository;

    @KafkaListener(topics = "order-topic-dlq", groupId = "dlq-group")
    public void consumeDLQ(
            String message,
            @Header(value = KafkaHeaders.DLT_ORIGINAL_TOPIC, required = false) String topic,
            @Header(value = "kafka_dlt-exception-message", required = false) String exceptionMessage) {

        log.error("🚨 Received message in DLQ from topic [{}]. Reason: {}", topic, exceptionMessage);

        try {
            // 1. Store in DB for auditing and manual reprocessing
            FailedMessage failedMessage = new FailedMessage();
            failedMessage.setPayload(message);
            failedMessage.setOriginalTopic(topic);
            failedMessage.setExceptionReason(exceptionMessage);
            failedMessage.setCreatedAt(LocalDateTime.now());
            failedMessage.setStatus("PENDING_MANUAL_REVIEW");

            failedMessageRepository.save(failedMessage);

            log.info("Successfully persisted DLQ message to database for manual intervention.");

            // 2. Alerting (Optional)
            // If you have a service like SlackNotifierService or PagerDutyService, call it here.
            // alertService.sendAlert("DLQ Message Received: " + exceptionMessage);

        } catch (Exception e) {
            log.error("CRITICAL: Failed to save DLQ message to database. Payload lost: {}", message, e);
        }
    }
}