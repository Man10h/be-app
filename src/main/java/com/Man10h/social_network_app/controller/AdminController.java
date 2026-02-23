package com.Man10h.social_network_app.controller;

import com.Man10h.social_network_app.model.entity.UserEntity;
import com.Man10h.social_network_app.model.response.PostResponse;
import com.Man10h.social_network_app.model.response.UserResponse;
import com.Man10h.social_network_app.service.PostService;
import com.Man10h.social_network_app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "API ADMIN", description = "Secured by JWT and required role ADMIN")
public class AdminController {

    private final PostService postService;
    private final UserService userService;

    @GetMapping("/posts")
    @Operation(summary = "Get a page of posts", description = "Param page and size")
    public ResponseEntity<Page<PostResponse>> getAllPosts(@RequestParam("page") int page,
                                                          @RequestParam("size") int size) {
        return ResponseEntity.ok(postService.getAllPosts(PageRequest.of(page, size)));
    }

    @DeleteMapping("/posts/{id}")
    @Operation(summary = "Delete post", description = "By id")
    public ResponseEntity<PostResponse> deletePost(@PathVariable("id") String id){
        postService.deletePostById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    @Operation(summary = "Get a page of users", description = "Param page and size")
    public ResponseEntity<Page<UserResponse>> getAllUser(@RequestParam("page") int page,
                                                       @RequestParam("size") int size){
        return ResponseEntity.ok(userService.getAllUsers(PageRequest.of(page, size)));
    }

    @PostMapping("/users/{id}/enabled")
    @Operation(summary = "Enable user", description = "Using user's id")
    public ResponseEntity<?> enabledUser(@PathVariable("id") String id){
        userService.enableUser(id);
        return ResponseEntity.ok().build();
    }
}
