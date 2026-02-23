package com.Man10h.social_network_app.repository;

import com.Man10h.social_network_app.model.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, String> {
}
