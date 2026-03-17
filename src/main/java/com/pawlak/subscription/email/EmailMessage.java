package com.pawlak.subscription.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessage {
    private String to;
    private String subject;
    private String body;
    private int retryCount;

    public EmailMessage(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.retryCount = 0;
    }

    public EmailMessage withIncrementedRetry() {
        return new EmailMessage(this.to, this.subject, this.body, this.retryCount + 1);
    }
}
