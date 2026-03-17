package com.pawlak.subscription.email.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EMAIL_QUEUE = "email.queue";
    public static final String EMAIL_DLQ = "email.dlq";
    public static final String EMAIL_EXCHANGE = "email.exchange";
    public static final String EMAIL_DLX = "email.dlx";
    public static final String EMAIL_ROUTING_KEY = "email.send";
    public static final String EMAIL_DLQ_ROUTING_KEY = "email.dead";
    public static final String EMAIL_RETRY_EXCHANGE = "email.retry.exchange";
    public static final String EMAIL_RETRY_QUEUE = "email.retry.queue";
    public static final String EMAIL_RETRY_ROUTING_KEY = "email.retry";

    public static final int MAX_RETRY_ATTEMPTS = 5;
    public static final int RETRY_DELAY_MS = 10_000;

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(EMAIL_DLX);
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(EMAIL_DLQ).build();
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(EMAIL_DLQ_ROUTING_KEY);
    }

    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange(EMAIL_EXCHANGE);
    }

    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable(EMAIL_QUEUE)
                .withArgument("x-dead-letter-exchange", EMAIL_DLX)
                .withArgument("x-dead-letter-routing-key", EMAIL_DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding emailBinding() {
        return BindingBuilder
                .bind(emailQueue())
                .to(emailExchange())
                .with(EMAIL_ROUTING_KEY);
    }

    @Bean
    public DirectExchange emailRetryExchange() {
        return new DirectExchange(EMAIL_RETRY_EXCHANGE);
    }

    @Bean
    public Queue emailRetryQueue() {
        return QueueBuilder.durable(EMAIL_RETRY_QUEUE)
                .withArgument("x-message-ttl", RETRY_DELAY_MS)
                .withArgument("x-dead-letter-exchange", EMAIL_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", EMAIL_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding emailRetryBinding() {
        return BindingBuilder
                .bind(emailRetryQueue())
                .to(emailRetryExchange())
                .with(EMAIL_RETRY_ROUTING_KEY);
    }

    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }
}