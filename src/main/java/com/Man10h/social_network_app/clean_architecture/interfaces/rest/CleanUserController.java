package com.Man10h.social_network_app.clean_architecture.interfaces.rest;

import com.Man10h.social_network_app.clean_architecture.application.shared.PageQuery;
import com.Man10h.social_network_app.clean_architecture.application.shared.UploadedFile;
import com.Man10h.social_network_app.clean_architecture.application.usecase.user.GetUserProfileUseCase;
import com.Man10h.social_network_app.clean_architecture.application.usecase.user.SearchUsersUseCase;
import com.Man10h.social_network_app.clean_architecture.application.usecase.user.UpdateUserProfileCommand;
import com.Man10h.social_network_app.clean_architecture.application.usecase.user.UpdateUserProfileUseCase;
import com.Man10h.social_network_app.clean_architecture.domain.shared.ImageData;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.ValidationException;
import com.Man10h.social_network_app.clean_architecture.domain.user.entity.User;
import com.Man10h.social_network_app.model.entity.UserEntity;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v2/clean/users")
public class CleanUserController {
    private final GetUserProfileUseCase getUserProfileUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final SearchUsersUseCase searchUsersUseCase;

    public CleanUserController(
            GetUserProfileUseCase getUserProfileUseCase,
            UpdateUserProfileUseCase updateUserProfileUseCase,
            SearchUsersUseCase searchUsersUseCase
    ) {
        this.getUserProfileUseCase = getUserProfileUseCase;
        this.updateUserProfileUseCase = updateUserProfileUseCase;
        this.searchUsersUseCase = searchUsersUseCase;
    }

    @GetMapping("/profile")
    public UserResponse getProfile(@AuthenticationPrincipal UserEntity userEntity) {
        return toResponse(getUserProfileUseCase.execute(userEntity.getId()));
    }

    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserResponse updateProfile(
            @AuthenticationPrincipal UserEntity userEntity,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String gender,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return toResponse(
                updateUserProfileUseCase.execute(
                        new UpdateUserProfileCommand(
                                userEntity.getId(),
                                firstName,
                                lastName,
                                gender,
                                toUploadedFile(image)
                        )
                )
        );
    }

    @GetMapping("/search")
    public ApiPageResponse<UserResponse> searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return ApiPageResponse.from(
                searchUsersUseCase.execute(name, new PageQuery(page, size)),
                this::toResponse
        );
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.id(),
                user.username(),
                user.firstName(),
                user.lastName(),
                user.gender(),
                toImageResponse(user.profileImage())
        );
    }

    private ImageResponse toImageResponse(ImageData image) {
        return image == null ? null : new ImageResponse(image.id(), image.url());
    }

    private UploadedFile toUploadedFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            return new UploadedFile(file.getOriginalFilename(), file.getContentType(), file.getBytes());
        } catch (IOException exception) {
            throw new ValidationException("Unable to read uploaded image");
        }
    }

    public record UserResponse(
            String id,
            String username,
            String firstName,
            String lastName,
            String gender,
            ImageResponse image
    ) {
    }

    public record ImageResponse(String id, String url) {
    }
}
