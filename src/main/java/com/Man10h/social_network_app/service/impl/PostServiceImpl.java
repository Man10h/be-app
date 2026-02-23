package com.Man10h.social_network_app.service.impl;

import com.Man10h.social_network_app.exception.exceptions.NotFoundException;
import com.Man10h.social_network_app.model.dto.PostDTO;
import com.Man10h.social_network_app.model.dto.PostUpdateDTO;
import com.Man10h.social_network_app.model.entity.CommentEntity;
import com.Man10h.social_network_app.model.entity.ImageEntity;
import com.Man10h.social_network_app.model.entity.PostEntity;
import com.Man10h.social_network_app.model.entity.UserEntity;
import com.Man10h.social_network_app.model.response.CommentResponse;
import com.Man10h.social_network_app.model.response.ImageResponse;
import com.Man10h.social_network_app.model.response.PostResponse;
import com.Man10h.social_network_app.repository.PostRepository;
import com.Man10h.social_network_app.repository.UserRepository;
import com.Man10h.social_network_app.service.CloudinaryService;
import com.Man10h.social_network_app.service.ImageService;
import com.Man10h.social_network_app.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CloudinaryService cloudinaryService;
    private final ImageService imageService;

    public PostResponse convertPostToPostResponse(PostEntity postEntity) {
        List<ImageResponse> imageResponseList = new ArrayList<>();
        for(ImageEntity imageEntity : postEntity.getImageEntityList()){
            ImageResponse imageResponse = ImageResponse.builder()
                    .url(imageEntity.getUrl())
                    .id(imageEntity.getId())
                    .build();
            imageResponseList.add(imageResponse);
        }
        return PostResponse.builder()
                .images(imageResponseList)
                .title(postEntity.getTitle())
                .content(postEntity.getContent())
                .like(postEntity.getLikeCount())
                .id(postEntity.getId())
                .build();
    }

    @Override
    public PostResponse createPost(String userId, PostDTO postDTO, List<MultipartFile> images) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()){
            throw new NotFoundException("User not found");
        }
        UserEntity user = optionalUser.get();

        PostEntity post = PostEntity.builder()
                .title(postDTO.getTitle())
                .likeCount(0L)
                .createDate(new Date())
                .content(postDTO.getContent())
                .commentEntityList(new ArrayList<>())
                .userEntity(user)
                .build();
        postRepository.save(post);

        List<ImageEntity> imageEntityList = new ArrayList<>();
        List<ImageResponse> imageResponseList = new ArrayList<>();
        if(!images.isEmpty()){
            for(MultipartFile file : images){
                ImageEntity imageEntity = imageService.createImageWithPostEntity(file, post);
                imageEntityList.add(imageEntity);

                ImageResponse imageResponse = ImageResponse.builder()
                        .url(imageEntity.getUrl())
                        .id(imageEntity.getId())
                        .build();
                imageResponseList.add(imageResponse);
            }
        }

        post.setImageEntityList(imageEntityList);
        postRepository.save(post);

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .like(post.getLikeCount())
                .images(imageResponseList)
                .build();
    }

    @Override
    public PostResponse getPostById(String id) {
        Optional<PostEntity> optionalPost = postRepository.getPostsWithImage(id);
        if(optionalPost.isEmpty()){
            throw new NotFoundException("Post not found");
        }
        PostEntity post = optionalPost.get();
        postRepository.getPostsWithComments(id);

        List<ImageResponse> imageResponseList = new ArrayList<>();
        for(ImageEntity imageEntity : post.getImageEntityList()){
            ImageResponse imageResponse = ImageResponse.builder()
                    .url(imageEntity.getUrl())
                    .id(imageEntity.getId())
                    .build();
            imageResponseList.add(imageResponse);
        }
        List<CommentResponse> commentResponseList = new ArrayList<>();
        for(CommentEntity commentEntity : post.getCommentEntityList()){
            CommentResponse commentResponse = CommentResponse.builder()
                    .id(commentEntity.getId())
                    .content(commentEntity.getContent())
                    .fullName(commentEntity.getFullName())
                    .url(commentEntity.getUrl())
                    .userId(commentEntity.getUserId())
                    .build();
            commentResponseList.add(commentResponse);
        }
        return PostResponse.builder()
                .images(imageResponseList)
                .title(post.getTitle())
                .content(post.getContent())
                .commentResponseList(commentResponseList)
                .like(post.getLikeCount())
                .id(post.getId())
                .build();
    }

    @Override
    public Page<PostResponse> getFollowerPosts(String userId, Pageable pageable) {
        return postRepository.getPosts(userId, pageable)
                .map(this::convertPostToPostResponse);
    }

    @Override
    public Page<PostResponse> getUserPosts(String userId, Pageable pageable) {
        return postRepository.findByUserEntity_Id(userId, pageable)
                .map(this::convertPostToPostResponse);
    }

    @Override
    public void deletePostById(String id) {
        Optional<PostEntity> optionalPost = postRepository.findById(id);
        if(optionalPost.isEmpty()){
            throw new NotFoundException("Post not found");
        }
        imageService.deleteByPostEntity(optionalPost.get());
        postRepository.deleteById(id);
    }

    @Override
    public void updatePost(String id, PostDTO postDTO) {
        Optional<PostEntity> optionalPost = postRepository.findById(id);
        if(optionalPost.isEmpty()){
            throw new NotFoundException("Post not found");
        }
        PostEntity post = optionalPost.get();
        if(!postDTO.getContent().isEmpty()){
            post.setContent(postDTO.getContent());
        }
        if(!postDTO.getTitle().isEmpty()){
            post.setTitle(postDTO.getTitle());
        }
    }

    @Override
    public Page<PostResponse> getAllPosts(Pageable pageable) {
        return postRepository.getAllPosts(pageable)
                .map(this::convertPostToPostResponse);
    }
}
