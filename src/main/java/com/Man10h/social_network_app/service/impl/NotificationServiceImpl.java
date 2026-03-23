package com.Man10h.social_network_app.service.impl;

import com.Man10h.social_network_app.exception.exceptions.NotFoundException;
import com.Man10h.social_network_app.model.entity.FollowerEntity;
import com.Man10h.social_network_app.model.entity.NotificationEntity;
import com.Man10h.social_network_app.model.entity.PostEntity;
import com.Man10h.social_network_app.model.entity.UserEntity;
import com.Man10h.social_network_app.model.response.NotificationResponse;
import com.Man10h.social_network_app.repository.FollowerRepository;
import com.Man10h.social_network_app.repository.NotificationRepository;
import com.Man10h.social_network_app.repository.PostRepository;
import com.Man10h.social_network_app.repository.UserRepository;
import com.Man10h.social_network_app.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void createAndSend(UserEntity sender, UserEntity receiver, String content, String targetId) {
        try{
            NotificationEntity notificationEntity = NotificationEntity.builder()
                    .sender(sender)
                    .receiver(receiver)
                    .content(content)
                    .createdAt(new Date())
                    .targetId(targetId)
                    .build();
            notificationRepository.save(notificationEntity);

            NotificationResponse notificationResponse = NotificationResponse.builder()
                    .id(notificationEntity.getId())
                    .targetId(targetId)
                    .content(content)
                    .createdAt(notificationEntity.getCreatedAt())
                    .sender(sender.getUsername())
                    .receiver(receiver.getUsername())
                    .build();
            messagingTemplate.convertAndSendToUser(receiver.getUsername(), "/queue/notifications", notificationResponse);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Transactional
    public void likePost(String postId, UserEntity sender) {
        Optional<PostEntity> optional = postRepository.findById(postId);
        if(optional.isEmpty()){
            throw new NotFoundException("Post not found");
        }
        PostEntity post = optional.get();
        if(!post.getUserEntity().equals(sender)){
            createAndSend(sender, post.getUserEntity(), "liked your post", postId);
        }
    }

    @Transactional
    public void commentPost(String postId, UserEntity sender) {
        Optional<PostEntity> optional = postRepository.findById(postId);
        if(optional.isEmpty()){
            throw new NotFoundException("Post not found");
        }
        PostEntity post = optional.get();
        if(!post.getUserEntity().equals(sender)){
            createAndSend(sender, post.getUserEntity(), "commented your post", postId);
        }
    }

    @Transactional
    public void followUser(String followerId, UserEntity currentUser) {
        Optional<UserEntity> optionalFollower = userRepository.findById(followerId);
        if(optionalFollower.isEmpty()){
            throw new NotFoundException("Follower not found");
        }
        createAndSend(currentUser, optionalFollower.get(), "followed you", followerId);
    }


}
