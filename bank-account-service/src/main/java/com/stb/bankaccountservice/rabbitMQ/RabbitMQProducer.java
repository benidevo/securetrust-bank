package com.stb.bankaccountservice.rabbitMQ;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitMQProducer {

    private final AmqpTemplate amqpTemplate;
    private final AmqpAdmin amqpAdmin;

    @Autowired
    public RabbitMQProducer(AmqpTemplate amqpTemplate, AmqpAdmin amqpAdmin) {
        this.amqpTemplate = amqpTemplate;
        this.amqpAdmin = amqpAdmin;
    }

    public void publishMessage(String queueName, String message) {
        Queue queue = new Queue(queueName, true);
        amqpAdmin.declareQueue(queue);
        amqpTemplate.convertAndSend(queueName, message);
        log.info("Message sent to queue: " + queueName);
    }
}
