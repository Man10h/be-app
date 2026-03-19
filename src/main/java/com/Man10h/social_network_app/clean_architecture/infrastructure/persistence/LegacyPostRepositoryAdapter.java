package com.Man10h.social_network_app.clean_architecture.infrastructure.persistence;

import com.Man10h.social_network_app.clean_architecture.application.shared.PageQuery;
import com.Man10h.social_network_app.clean_architecture.application.shared.PageResult;
import com.Man10h.social_network_app.clean_architecture.domain.post.entity.Post;
import com.Man10h.social_network_app.clean_architecture.domain.post.repository.PostRepository;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.ResourceNotFoundException;
import com.Man10h.social_network_app.clean_architecture.infrastructure.persistence.mapper.LegacyPostMapper;
import com.Man10h.social_network_app.model.entity.PostEntity;
import com.Man10h.social_network_app.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LegacyPostRepositoryAdapter implements PostRepository {
    private final com.Man10h.social_network_app.repository.PostRepository postRepository;
    private final com.Man10h.social_network_app.repository.UserRepository userRepository;
    private final LegacyPostMapper legacyPostMapper;

    public LegacyPostRepositoryAdapter(
            com.Man10h.social_network_app.repository.PostRepository postRepository,
            com.Man10h.social_network_app.repository.UserRepository userRepository,
            LegacyPostMapper legacyPostMapper
    ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.legacyPostMapper = legacyPostMapper;
    }

    @Override
    public Post save(Post post) {
        PostEntity entity = post.id() == null
                ? new PostEntity()
                : postRepository.findById(post.id()).orElse(new PostEntity());

        UserEntity author = userRepository.findById(post.authorId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        PostEntity savedEntity = postRepository.save(legacyPostMapper.copyToEntity(post, entity, author));
        return legacyPostMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Post> findById(String id) {
        return postRepository.findById(id).map(legacyPostMapper::toDomain);
    }

    @Override
    public Optional<Post> findDetailedById(String id) {
        Optional<PostEntity> postWithImages = postRepository.getPostsWithImage(id);
        Optional<PostEntity> postWithComments = postRepository.getPostsWithComments(id);
        if (postWithImages.isPresent() && postWithComments.isPresent()) {
            postWithImages.get().setCommentEntityList(postWithComments.get().getCommentEntityList());
        }
        return postWithImages.map(legacyPostMapper::toDomain);
    }

    @Override
    public PageResult<Post> findFollowerPosts(String userId, PageQuery pageQuery) {
        Page<PostEntity> page = postRepository.getPosts(userId, PageRequest.of(pageQuery.page(), pageQuery.size()));
        return new PageResult<>(
                page.getContent().stream().map(legacyPostMapper::toDomain).toList(),
                pageQuery.page(),
                pageQuery.size(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    public PageResult<Post> findByAuthorId(String userId, PageQuery pageQuery) {
        Page<PostEntity> page = postRepository.findByUserEntity_Id(userId, PageRequest.of(pageQuery.page(), pageQuery.size()));
        return new PageResult<>(
                page.getContent().stream().map(legacyPostMapper::toDomain).toList(),
                pageQuery.page(),
                pageQuery.size(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    public void deleteById(String id) {
        postRepository.deleteById(id);
    }
}
