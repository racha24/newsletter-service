package com.newsletter.service;

import com.newsletter.model.Content;
import com.newsletter.model.Content.ContentStatus;
import com.newsletter.model.Subscriber;
import com.newsletter.repository.ContentRepository;
import com.newsletter.repository.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsletterSchedulerService {

    private final ContentService contentService;
    private final SubscriberRepository subscriberRepository;
    private final ContentRepository contentRepository;
    private final EmailService emailService;

    /**
     * Scheduled job that runs every minute to check for due newsletters
     * Cron: every minute
     */
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void sendScheduledNewsletters() {
        log.info("Running scheduled newsletter job at: {}", LocalDateTime.now());

        List<Content> dueContent = contentService.getDueContent();

        if (dueContent.isEmpty()) {
            log.info("No newsletters due for sending at this time");
            return;
        }

        log.info("Found {} newsletters to send", dueContent.size());

        for (Content content : dueContent) {
            try {
                sendNewsletterToSubscribers(content);
            } catch (Exception e) {
                log.error("Error processing content ID: {}. Error: {}", content.getId(), e.getMessage(), e);
                content.setStatus(ContentStatus.FAILED);
                contentRepository.save(content);
            }
        }

        log.info("Scheduled newsletter job completed");
    }

    @Transactional
    public void sendNewsletterToSubscribers(Content content) {
        log.info("Processing newsletter for content ID: {}, Topic: {}",
                content.getId(), content.getTopic().getName());

        List<Subscriber> subscribers = subscriberRepository.findByTopicAndActiveTrue(content.getTopic());

        if (subscribers.isEmpty()) {
            log.warn("No active subscribers found for topic: {}", content.getTopic().getName());
            content.setStatus(ContentStatus.SENT);
            content.setSentAt(LocalDateTime.now());
            contentRepository.save(content);
            return;
        }

        log.info("Sending newsletter to {} subscribers", subscribers.size());

        int successCount = 0;
        int failCount = 0;

        for (Subscriber subscriber : subscribers) {
            try {
                emailService.sendEmail(content, subscriber);
                successCount++;
            } catch (Exception e) {
                log.error("Failed to send email to subscriber ID: {}. Error: {}",
                        subscriber.getId(), e.getMessage());
                failCount++;
            }
        }

        content.setStatus(ContentStatus.SENT);
        content.setSentAt(LocalDateTime.now());
        contentRepository.save(content);

        log.info("Newsletter sent successfully. Success: {}, Failed: {}", successCount, failCount);
    }

    /**
     * Manual trigger to send a specific newsletter immediately
     */
    @Transactional
    public void sendNewsletterNow(Long contentId) {
        log.info("Manual trigger to send newsletter for content ID: {}", contentId);

        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("Content not found with ID: " + contentId));

        if (content.getStatus() == ContentStatus.SENT) {
            throw new IllegalArgumentException("Content has already been sent");
        }

        if (content.getStatus() == ContentStatus.CANCELLED) {
            throw new IllegalArgumentException("Cannot send cancelled content");
        }

        sendNewsletterToSubscribers(content);
    }
}
