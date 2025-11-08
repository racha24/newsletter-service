package com.newsletter.repository;

import com.newsletter.model.Subscriber;
import com.newsletter.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {
    List<Subscriber> findByTopicAndActiveTrue(Topic topic);
    Optional<Subscriber> findByEmailAndTopic(String email, Topic topic);
    boolean existsByEmailAndTopic(String email, Topic topic);
    List<Subscriber> findByEmail(String email);
}
