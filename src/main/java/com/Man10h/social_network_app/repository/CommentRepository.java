package com.Man10h.social_network_app.repository;

import com.Man10h.social_network_app.model.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, String> {
    void deleteById(String commentId);
}
