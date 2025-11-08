package com.newsletter.repository;

import com.newsletter.model.EmailLog;
import com.newsletter.model.EmailLog.EmailStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {
    List<EmailLog> findByContentId(Long contentId);
    List<EmailLog> findByStatus(EmailStatus status);
    List<EmailLog> findBySubscriberId(Long subscriberId);
}
