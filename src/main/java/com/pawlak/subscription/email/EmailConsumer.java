package com.pawlak.subscription.email;

import com.pawlak.subscription.email.config.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class EmailConsumer {

    private final JavaMailSender mailSender;
    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void consume(EmailMessage message,
                        Channel channel,
                        @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            sendEmail(message);
            channel.basicAck(deliveryTag, false);
            log.info("Email sent successfully: {} -> {}", message.getSubject(), message.getTo());

        } catch (Exception e) {
            handleFailure(message, channel, deliveryTag, e);
        }
    }

    private void handleFailure(EmailMessage message, Channel channel, long deliveryTag, Exception e) throws IOException {
        int currentRetry = message.getRetryCount();

        channel.basicAck(deliveryTag, false);

        if (currentRetry < RabbitMQConfig.MAX_RETRY_ATTEMPTS - 1) {
            scheduleRetry(message);
        } else {
            sendToDlq(message);
        }
    }

    private void scheduleRetry(EmailMessage message) {
        EmailMessage retryMessage = message.withIncrementedRetry();
        rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_RETRY_EXCHANGE, RabbitMQConfig.EMAIL_RETRY_ROUTING_KEY, retryMessage);
    }

    private void sendToDlq(EmailMessage message) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EMAIL_DLX,
                RabbitMQConfig.EMAIL_DLQ_ROUTING_KEY,
                message
        );
        log.error("Email sent to DLQ after {} attempts: {} -> {}",
                RabbitMQConfig.MAX_RETRY_ATTEMPTS, message.getSubject(), message.getTo());
    }

    private void sendEmail(EmailMessage message) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setText(message.getBody(), true);
        helper.setTo(message.getTo());
        helper.setSubject(message.getSubject());
        helper.setFrom(fromEmail);
        mailSender.send(mimeMessage);
    }
}
