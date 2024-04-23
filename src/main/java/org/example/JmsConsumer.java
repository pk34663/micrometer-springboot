package org.example;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsConsumer {
    @JmsListener(destination = "test.queue")
    public void recieveMessage(String message) {
        System.out.println("recieved:" + message);
    }
}
