package com.Man10h.social_network_app.service.impl;

import com.Man10h.social_network_app.exception.exceptions.NotFoundException;
import com.Man10h.social_network_app.model.entity.PostEntity;
import com.Man10h.social_network_app.model.entity.PostLikeEntity;
import com.Man10h.social_network_app.model.entity.UserEntity;
import com.Man10h.social_network_app.repository.PostLikeRepository;
import com.Man10h.social_network_app.repository.PostRepository;
import com.Man10h.social_network_app.repository.UserRepository;
import com.Man10h.social_network_app.service.NotificationService;
import com.Man10h.social_network_app.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeServiceImpl implements PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;


    @Override
    @Transactional
    public void likePost(String postId, String userId) {
        Optional<PostEntity> optionalPostEntity = postRepository.findById(postId);
        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
        if(optionalPostEntity.isEmpty() || optionalUserEntity.isEmpty()) {
            throw new NotFoundException("User/Post not found");
        }
        PostEntity postEntity = optionalPostEntity.get();
        UserEntity userEntity = optionalUserEntity.get();

        Optional<PostLikeEntity> optionalPostLikeEntity = postLikeRepository.findByUserEntityAndPostEntity(userEntity, postEntity);
        if(optionalPostLikeEntity.isEmpty()){
            PostLikeEntity postLikeEntity = PostLikeEntity.builder()
                    .postEntity(postEntity)
                    .userEntity(userEntity)
                    .createAt(new Date())
                    .build();

            postLikeRepository.save(postLikeEntity);

            postEntity.setLikeCount(postEntity.getLikeCount() + 1);
            postRepository.save(postEntity);

            notificationService.likePost(postId, userEntity);
        }
        else{
            postLikeRepository.deleteByUserEntityAndPostEntity(userEntity, postEntity);
            postEntity.setLikeCount(postEntity.getLikeCount() - 1);
            postRepository.save(postEntity);
        }
    }
}
