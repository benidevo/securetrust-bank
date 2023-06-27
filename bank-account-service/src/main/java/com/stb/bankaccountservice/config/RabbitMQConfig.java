package com.stb.bankaccountservice.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(Environment.RABBITMQ_HOST);
        connectionFactory.setPort(Environment.RABBITMQ_PORT);
        connectionFactory.setUsername(Environment.RABBITMQ_USERNAME);
        connectionFactory.setPassword(Environment.RABBITMQ_PASSWORD);
        connectionFactory.setVirtualHost(Environment.RABBITMQ_VIRTUAL_HOST);
        return connectionFactory;
    }

    @Bean
    public CachingConnectionFactory cachingConnectionFactory() {
        return new CachingConnectionFactory(connectionFactory());
    }
}
