package com.Man10h.social_network_app.clean_architecture.infrastructure.config;

import com.Man10h.social_network_app.clean_architecture.application.port.*;
import com.Man10h.social_network_app.clean_architecture.application.usecase.auth.*;
import com.Man10h.social_network_app.clean_architecture.application.usecase.post.*;
import com.Man10h.social_network_app.clean_architecture.application.usecase.user.GetUserProfileUseCase;
import com.Man10h.social_network_app.clean_architecture.application.usecase.user.SearchUsersUseCase;
import com.Man10h.social_network_app.clean_architecture.application.usecase.user.UpdateUserProfileUseCase;
import com.Man10h.social_network_app.clean_architecture.domain.post.repository.PostRepository;
import com.Man10h.social_network_app.clean_architecture.domain.user.repository.UserAccountRepository;
import com.Man10h.social_network_app.clean_architecture.domain.user.repository.UserRoleRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadLocalRandom;

@Configuration
public class CleanArchitectureConfig {
    @Bean
    public RegisterUserUseCase registerUserUseCase(
            UserAccountRepository userAccountRepository,
            UserRoleRepository userRoleRepository,
            PasswordHasher passwordHasher,
            MailGateway mailGateway
    ) {
        return new RegisterUserUseCase(userAccountRepository, userRoleRepository, passwordHasher, mailGateway);
    }

    @Bean
    public LoginUserUseCase loginUserUseCase(
            UserAccountRepository userAccountRepository,
            PasswordHasher passwordHasher,
            CredentialsAuthenticator credentialsAuthenticator,
            TokenIssuer tokenIssuer
    ) {
        return new LoginUserUseCase(userAccountRepository, passwordHasher, credentialsAuthenticator, tokenIssuer);
    }

    @Bean
    public ForgotPasswordUseCase forgotPasswordUseCase(
            UserAccountRepository userAccountRepository,
            PasswordHasher passwordHasher,
            RandomCodeGenerator randomCodeGenerator,
            MailGateway mailGateway
    ) {
        return new ForgotPasswordUseCase(userAccountRepository, passwordHasher, randomCodeGenerator, mailGateway);
    }

    @Bean
    public GoogleLoginUseCase googleLoginUseCase(
            UserAccountRepository userAccountRepository,
            UserRoleRepository userRoleRepository,
            GoogleIdentityVerifier googleIdentityVerifier,
            TokenIssuer tokenIssuer
    ) {
        return new GoogleLoginUseCase(userAccountRepository, userRoleRepository, googleIdentityVerifier, tokenIssuer);
    }

    @Bean
    public GetUserProfileUseCase getUserProfileUseCase(UserAccountRepository userAccountRepository) {
        return new GetUserProfileUseCase(userAccountRepository);
    }

    @Bean
    public UpdateUserProfileUseCase updateUserProfileUseCase(
            UserAccountRepository userAccountRepository,
            UserProfileImageStorage userProfileImageStorage
    ) {
        return new UpdateUserProfileUseCase(userAccountRepository, userProfileImageStorage);
    }

    @Bean
    public SearchUsersUseCase searchUsersUseCase(UserAccountRepository userAccountRepository) {
        return new SearchUsersUseCase(userAccountRepository);
    }

    @Bean
    public CreatePostUseCase createPostUseCase(
            UserAccountRepository userAccountRepository,
            PostRepository postRepository,
            PostImageStorage postImageStorage
    ) {
        return new CreatePostUseCase(userAccountRepository, postRepository, postImageStorage);
    }

    @Bean
    public GetPostDetailUseCase getPostDetailUseCase(PostRepository postRepository) {
        return new GetPostDetailUseCase(postRepository);
    }

    @Bean
    public ListFollowerPostsUseCase listFollowerPostsUseCase(PostRepository postRepository) {
        return new ListFollowerPostsUseCase(postRepository);
    }

    @Bean
    public ListUserPostsUseCase listUserPostsUseCase(PostRepository postRepository) {
        return new ListUserPostsUseCase(postRepository);
    }

    @Bean
    public UpdatePostUseCase updatePostUseCase(PostRepository postRepository) {
        return new UpdatePostUseCase(postRepository);
    }

    @Bean
    public DeletePostUseCase deletePostUseCase(PostRepository postRepository, PostImageStorage postImageStorage) {
        return new DeletePostUseCase(postRepository, postImageStorage);
    }

    @Bean
    public RandomCodeGenerator randomCodeGenerator() {
        return () -> String.format("%05d", ThreadLocalRandom.current().nextInt(100000));
    }
}
