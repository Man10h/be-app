package com.Man10h.social_network_app.service;


import com.Man10h.social_network_app.model.entity.UserEntity;

public interface NotificationService {
    public void createAndSend(UserEntity sender, UserEntity receiver, String content, String targetId);
    public void likePost(String postId, UserEntity currentUser);
    public void commentPost(String postId, UserEntity currentUser);
    public void followUser(String followerId, UserEntity currentUser);
}
