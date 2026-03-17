package com.pawlak.subscription.email;

import com.pawlak.subscription.email.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailProducer implements EmailSender {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void send(String to, String body, String subject) {
        EmailMessage message = new EmailMessage(to, subject, body);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EMAIL_EXCHANGE,
                RabbitMQConfig.EMAIL_ROUTING_KEY,
                message
        );
    }
}