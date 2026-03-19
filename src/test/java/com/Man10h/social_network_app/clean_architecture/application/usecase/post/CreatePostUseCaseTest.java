package com.Man10h.social_network_app.clean_architecture.application.usecase.post;

import com.Man10h.social_network_app.clean_architecture.application.port.PostImageStorage;
import com.Man10h.social_network_app.clean_architecture.application.shared.PageQuery;
import com.Man10h.social_network_app.clean_architecture.application.shared.PageResult;
import com.Man10h.social_network_app.clean_architecture.application.shared.UploadedFile;
import com.Man10h.social_network_app.clean_architecture.domain.post.entity.Post;
import com.Man10h.social_network_app.clean_architecture.domain.post.repository.PostRepository;
import com.Man10h.social_network_app.clean_architecture.domain.user.entity.User;
import com.Man10h.social_network_app.clean_architecture.domain.user.entity.UserRole;
import com.Man10h.social_network_app.clean_architecture.domain.user.repository.UserAccountRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreatePostUseCaseTest {
    @Test
    void storesPostAndUploadsImages() {
        PostMemoryRepository postRepository = new PostMemoryRepository();
        RecordingPostImageStorage imageStorage = new RecordingPostImageStorage();
        CreatePostUseCase useCase = new CreatePostUseCase(
                new SingleUserRepository(),
                postRepository,
                imageStorage
        );

        Post post = useCase.execute(
                new CreatePostCommand(
                        "user-1",
                        "Hello",
                        "World",
                        List.of(new UploadedFile("a.png", "image/png", new byte[]{1, 2, 3}))
                )
        );

        assertEquals("post-1", post.id());
        assertEquals(1, imageStorage.uploadedCount);
        assertEquals(1, postRepository.savedPosts.size());
    }

    private static final class SingleUserRepository implements UserAccountRepository {
        private final User user = new User("user-1", "man10h", "man10h@email.com", "hashed-secret", "Man", "Ten", "M", true, new UserRole(2L, "USER"), null);

        @Override
        public Optional<User> findById(String id) {
            return user.id().equals(id) ? Optional.of(user) : Optional.empty();
        }

        @Override
        public Optional<User> findProfileById(String id) {
            return findById(id);
        }

        @Override
        public Optional<User> findByUsername(String username) {
            return Optional.empty();
        }

        @Override
        public Optional<User> findByEmail(String email) {
            return Optional.empty();
        }

        @Override
        public User save(User user) {
            return user;
        }

        @Override
        public PageResult<User> searchByName(String name, PageQuery pageQuery) {
            return new PageResult<>(List.of(user), pageQuery.page(), pageQuery.size(), 1, 1);
        }
    }

    private static final class PostMemoryRepository implements PostRepository {
        private final List<Post> savedPosts = new ArrayList<>();

        @Override
        public Post save(Post post) {
            Post saved = new Post("post-1", post.title(), post.content(), post.likeCount(), post.createdAt(), post.authorId(), post.images(), post.comments());
            savedPosts.add(saved);
            return saved;
        }

        @Override
        public Optional<Post> findById(String id) {
            return savedPosts.stream().filter(post -> post.id().equals(id)).findFirst();
        }

        @Override
        public Optional<Post> findDetailedById(String id) {
            return findById(id);
        }

        @Override
        public PageResult<Post> findFollowerPosts(String userId, PageQuery pageQuery) {
            return new PageResult<>(savedPosts, pageQuery.page(), pageQuery.size(), savedPosts.size(), 1);
        }

        @Override
        public PageResult<Post> findByAuthorId(String userId, PageQuery pageQuery) {
            return new PageResult<>(savedPosts, pageQuery.page(), pageQuery.size(), savedPosts.size(), 1);
        }

        @Override
        public void deleteById(String id) {
        }
    }

    private static final class RecordingPostImageStorage implements PostImageStorage {
        private int uploadedCount;

        @Override
        public void attach(String postId, List<UploadedFile> images) {
            uploadedCount = images.size();
        }

        @Override
        public void deleteAll(String postId) {
        }
    }
}
