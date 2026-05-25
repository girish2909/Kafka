package org.pih.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dlq")
public class DLQController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/reprocess")
    public String reprocess(@RequestBody String message) {

        // DLQ से message लेकर main topic में भेज दिया
        kafkaTemplate.send("order-topic", message);

        return "Message reprocessed successfully";
    }
}