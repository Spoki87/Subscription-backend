package com.pawlak.subscription.email;

public interface EmailSender {
    void send(String to, String body, String subject);
}
