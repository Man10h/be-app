package com.Man10h.social_network_app.service.impl;

import com.Man10h.social_network_app.exception.exceptions.NotFoundException;
import com.Man10h.social_network_app.exception.exceptions.UnauthorizedException;
import com.Man10h.social_network_app.model.dto.CommentDTO;
import com.Man10h.social_network_app.model.entity.CommentEntity;
import com.Man10h.social_network_app.model.entity.PostEntity;
import com.Man10h.social_network_app.model.entity.UserEntity;
import com.Man10h.social_network_app.model.response.CommentResponse;
import com.Man10h.social_network_app.repository.CommentRepository;
import com.Man10h.social_network_app.repository.PostRepository;
import com.Man10h.social_network_app.repository.UserRepository;
import com.Man10h.social_network_app.service.CommentService;
import com.Man10h.social_network_app.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final NotificationService notificationService;

    @Transactional
    public CommentResponse createComment(String postId, String userId, CommentDTO comment) {
        Optional<UserEntity> optionalUserEntity = userRepository.getProfile(userId);
        Optional<PostEntity> optionalPostEntity = postRepository.findById(postId);
        if(optionalUserEntity.isEmpty()){
            throw new NotFoundException("User not found");
        }
        if(optionalPostEntity.isEmpty()){
            throw new NotFoundException("Post not found");
        }
        UserEntity userEntity = optionalUserEntity.get();
        PostEntity postEntity = optionalPostEntity.get();

        CommentEntity commentEntity = CommentEntity.builder()
                .content(comment.getContent())
                .userId(userEntity.getId())
                .url(userEntity.getImageEntityList().isEmpty() ? null : userEntity.getImageEntityList().getFirst().getUrl())
                .fullName(userEntity.getFirstName() + " " + userEntity.getLastName())
                .createDate(new Date())
                .postEntity(postEntity)
                .build();

        commentRepository.save(commentEntity);

        notificationService.commentPost(postId, userEntity);
        return CommentResponse.builder()
                .id(commentEntity.getId())
                .content(commentEntity.getContent())
                .createDate(commentEntity.getCreateDate())
                .userId(userEntity.getId())
                .fullName(userEntity.getFirstName() + " " + userEntity.getLastName())
                .url(userEntity.getImageEntityList().isEmpty() ? null : userEntity.getImageEntityList().getFirst().getUrl())
                .build();
    }

    @Transactional
    public void deleteComment(String commentId) {
        try{
            commentRepository.deleteById(commentId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void deleteCommentByUser(String commentId, UserEntity userEntity) {
        Optional<CommentEntity> optional = commentRepository.findById(commentId);
        if(optional.isEmpty()){
            throw new NotFoundException("Comment not found");
        }
        CommentEntity commentEntity = optional.get();

        List<CommentEntity> commentEntityList = commentRepository.findByUserId(userEntity.getId());
        if(!commentEntityList.contains(commentEntity)){
            throw new UnauthorizedException("You are not authorized to delete this comment");
        }
        commentRepository.delete(commentEntity);
    }
}
