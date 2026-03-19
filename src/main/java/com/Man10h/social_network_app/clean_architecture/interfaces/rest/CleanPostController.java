package com.Man10h.social_network_app.clean_architecture.interfaces.rest;

import com.Man10h.social_network_app.clean_architecture.application.shared.PageQuery;
import com.Man10h.social_network_app.clean_architecture.application.shared.UploadedFile;
import com.Man10h.social_network_app.clean_architecture.application.usecase.post.*;
import com.Man10h.social_network_app.clean_architecture.domain.post.entity.Post;
import com.Man10h.social_network_app.clean_architecture.domain.post.entity.PostComment;
import com.Man10h.social_network_app.clean_architecture.domain.shared.ImageData;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.ValidationException;
import com.Man10h.social_network_app.model.entity.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v2/clean/posts")
public class CleanPostController {
    private final CreatePostUseCase createPostUseCase;
    private final GetPostDetailUseCase getPostDetailUseCase;
    private final ListFollowerPostsUseCase listFollowerPostsUseCase;
    private final ListUserPostsUseCase listUserPostsUseCase;
    private final UpdatePostUseCase updatePostUseCase;
    private final DeletePostUseCase deletePostUseCase;

    public CleanPostController(
            CreatePostUseCase createPostUseCase,
            GetPostDetailUseCase getPostDetailUseCase,
            ListFollowerPostsUseCase listFollowerPostsUseCase,
            ListUserPostsUseCase listUserPostsUseCase,
            UpdatePostUseCase updatePostUseCase,
            DeletePostUseCase deletePostUseCase
    ) {
        this.createPostUseCase = createPostUseCase;
        this.getPostDetailUseCase = getPostDetailUseCase;
        this.listFollowerPostsUseCase = listFollowerPostsUseCase;
        this.listUserPostsUseCase = listUserPostsUseCase;
        this.updatePostUseCase = updatePostUseCase;
        this.deletePostUseCase = deletePostUseCase;
    }

    @GetMapping("/feed")
    public ApiPageResponse<PostResponse> followerFeed(
            @AuthenticationPrincipal UserEntity userEntity,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return ApiPageResponse.from(
                listFollowerPostsUseCase.execute(userEntity.getId(), new PageQuery(page, size)),
                this::toResponse
        );
    }

    @GetMapping("/mine")
    public ApiPageResponse<PostResponse> myPosts(
            @AuthenticationPrincipal UserEntity userEntity,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return ApiPageResponse.from(
                listUserPostsUseCase.execute(userEntity.getId(), new PageQuery(page, size)),
                this::toResponse
        );
    }

    @GetMapping("/{postId}")
    public PostResponse getPost(@PathVariable String postId) {
        return toResponse(getPostDetailUseCase.execute(postId));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponse> createPost(
            @AuthenticationPrincipal UserEntity userEntity,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        Post post = createPostUseCase.execute(
                new CreatePostCommand(
                        userEntity.getId(),
                        title,
                        content,
                        toUploadedFiles(images)
                )
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(post));
    }

    @PutMapping("/{postId}")
    public PostResponse updatePost(
            @PathVariable String postId,
            @AuthenticationPrincipal UserEntity userEntity,
            @RequestBody UpdatePostRequest request
    ) {
        return toResponse(
                updatePostUseCase.execute(
                        new UpdatePostCommand(postId, userEntity.getId(), request.title(), request.content())
                )
        );
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable String postId,
            @AuthenticationPrincipal UserEntity userEntity
    ) {
        deletePostUseCase.execute(postId, userEntity.getId());
        return ResponseEntity.noContent().build();
    }

    private PostResponse toResponse(Post post) {
        return new PostResponse(
                post.id(),
                post.title(),
                post.content(),
                post.likeCount(),
                post.images().stream().map(this::toImageResponse).toList(),
                post.comments().stream().map(this::toCommentResponse).toList()
        );
    }

    private ImageResponse toImageResponse(ImageData imageData) {
        return new ImageResponse(imageData.id(), imageData.url());
    }

    private CommentResponse toCommentResponse(PostComment comment) {
        return new CommentResponse(comment.id(), comment.userId(), comment.fullName(), comment.imageUrl(), comment.content());
    }

    private List<UploadedFile> toUploadedFiles(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return List.of();
        }
        return files.stream()
                .filter(file -> file != null && !file.isEmpty())
                .map(this::toUploadedFile)
                .toList();
    }

    private UploadedFile toUploadedFile(MultipartFile file) {
        try {
            return new UploadedFile(file.getOriginalFilename(), file.getContentType(), file.getBytes());
        } catch (IOException exception) {
            throw new ValidationException("Unable to read uploaded image");
        }
    }

    public record UpdatePostRequest(String title, String content) {
    }

    public record PostResponse(
            String id,
            String title,
            String content,
            long likeCount,
            List<ImageResponse> images,
            List<CommentResponse> comments
    ) {
    }

    public record ImageResponse(String id, String url) {
    }

    public record CommentResponse(String id, String userId, String fullName, String imageUrl, String content) {
    }
}
