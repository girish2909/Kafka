package org.pih.component;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @KafkaListener(topics = "${pih.custom.topic}", groupId = "groupId")
    void listener(String data){
        System.out.println("listener received : "+ data + " :)");
    }
}
