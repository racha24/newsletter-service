package com.newsletter.service;

import com.newsletter.model.Content;
import com.newsletter.model.EmailLog;
import com.newsletter.model.EmailLog.EmailStatus;
import com.newsletter.model.Subscriber;
import com.newsletter.repository.EmailLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailLogRepository emailLogRepository;

    @Async
    @Transactional
    public void sendEmail(Content content, Subscriber subscriber) {
        log.info("Sending email to: {} for content ID: {}", subscriber.getEmail(), content.getId());

        EmailLog emailLog = new EmailLog();
        emailLog.setContent(content);
        emailLog.setSubscriber(subscriber);
        emailLog.setRecipientEmail(subscriber.getEmail());

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(subscriber.getEmail());
            message.setSubject(content.getSubject());
            message.setText(formatEmailBody(content, subscriber));

            mailSender.send(message);

            emailLog.setStatus(EmailStatus.SUCCESS);
            log.info("Email sent successfully to: {}", subscriber.getEmail());

        } catch (Exception e) {
            log.error("Failed to send email to: {}. Error: {}", subscriber.getEmail(), e.getMessage());
            emailLog.setStatus(EmailStatus.FAILED);
            emailLog.setErrorMessage(e.getMessage());
        }

        emailLogRepository.save(emailLog);
    }

    private String formatEmailBody(Content content, Subscriber subscriber) {
        return String.format(
                "Hi %s,\n\n%s\n\n---\nTopic: %s\n\nTo unsubscribe, please contact us.",
                subscriber.getName(),
                content.getBody(),
                content.getTopic().getName()
        );
    }
}
