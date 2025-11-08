package com.newsletter.repository;

import com.newsletter.model.Content;
import com.newsletter.model.Content.ContentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    @Query("SELECT c FROM Content c WHERE c.status = :status AND c.scheduledTime <= :currentTime")
    List<Content> findDueContent(ContentStatus status, LocalDateTime currentTime);

    List<Content> findByStatus(ContentStatus status);

    List<Content> findByTopicId(Long topicId);
}
