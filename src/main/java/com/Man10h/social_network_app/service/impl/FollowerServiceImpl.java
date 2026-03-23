package com.Man10h.social_network_app.service.impl;

import com.Man10h.social_network_app.exception.exceptions.NotFoundException;
import com.Man10h.social_network_app.model.entity.FollowerEntity;
import com.Man10h.social_network_app.model.entity.UserEntity;
import com.Man10h.social_network_app.model.response.FollowerResponse;
import com.Man10h.social_network_app.repository.FollowerRepository;
import com.Man10h.social_network_app.repository.UserRepository;
import com.Man10h.social_network_app.service.FollowerService;
import com.Man10h.social_network_app.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowerServiceImpl implements FollowerService {

    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;
    private final NotificationService notificationService;

    @Override
    public List<FollowerResponse> getFollowers(String userId) {
        return followerRepository.getFollowers(userId);
    }

    @Transactional
    public void follow(String followerId, String userId) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
        Optional<UserEntity> optionalFollower = userRepository.findById(followerId);

        if(optionalUserEntity.isEmpty() || optionalFollower.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        Optional<FollowerEntity> optionalFollowerEntity = followerRepository.findById(followerId);
        if(optionalFollowerEntity.isEmpty()){
            UserEntity userEntity = optionalUserEntity.get();
            FollowerEntity followerEntity = FollowerEntity.builder()
                    .followerId(followerId)
                    .userEntity(userEntity)
                    .build();
            followerRepository.save(followerEntity);

            notificationService.followUser(followerId, userEntity);
        }
        else {
            followerRepository.delete(optionalFollowerEntity.get());
        }

    }
}
