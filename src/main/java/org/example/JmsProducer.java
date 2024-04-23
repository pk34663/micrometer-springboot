package org.example;

import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class JmsProducer {
    private final JmsMessagingTemplate jmsMessagingTemplate;

    public JmsProducer(JmsMessagingTemplate jmsMessagingTemplate) {
        this.jmsMessagingTemplate = jmsMessagingTemplate;
    }

    public void sendMessage(String message) {
        this.jmsMessagingTemplate.convertAndSend("test.queue", message);
    }
}
