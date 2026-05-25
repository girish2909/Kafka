package org.pih.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/send")
public class KafkaProducerController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping
    public String sendMessage(@RequestParam String message) {
        kafkaTemplate.send("order-topic", message);
        return "Message sent: " + message;
    }
}