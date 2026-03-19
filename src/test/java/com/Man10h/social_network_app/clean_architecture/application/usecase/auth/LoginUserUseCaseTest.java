package com.Man10h.social_network_app.clean_architecture.application.usecase.auth;

import com.Man10h.social_network_app.clean_architecture.application.port.CredentialsAuthenticator;
import com.Man10h.social_network_app.clean_architecture.application.port.PasswordHasher;
import com.Man10h.social_network_app.clean_architecture.application.port.TokenIssuer;
import com.Man10h.social_network_app.clean_architecture.application.shared.PageQuery;
import com.Man10h.social_network_app.clean_architecture.application.shared.PageResult;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.UnauthorizedActionException;
import com.Man10h.social_network_app.clean_architecture.domain.user.entity.User;
import com.Man10h.social_network_app.clean_architecture.domain.user.entity.UserRole;
import com.Man10h.social_network_app.clean_architecture.domain.user.repository.UserAccountRepository;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LoginUserUseCaseTest {
    @Test
    void returnsTokenWhenCredentialsAreValid() {
        User user = new User("user-1", "man10h", "man10h@email.com", "hashed-secret", "Man", "Ten", "M", true, new UserRole(2L, "USER"), null);
        LoginUserUseCase useCase = new LoginUserUseCase(
                new SingleUserRepository(user),
                new FakePasswordHasher(),
                new RecordingAuthenticator(),
                currentUser -> "token-for-" + currentUser.username()
        );

        String token = useCase.execute(new LoginUserCommand("man10h", "secret"));

        assertEquals("token-for-man10h", token);
    }

    @Test
    void rejectsInvalidPassword() {
        User user = new User("user-1", "man10h", "man10h@email.com", "hashed-secret", "Man", "Ten", "M", true, new UserRole(2L, "USER"), null);
        LoginUserUseCase useCase = new LoginUserUseCase(
                new SingleUserRepository(user),
                new FakePasswordHasher(),
                (username, password) -> {},
                currentUser -> "token"
        );

        assertThrows(UnauthorizedActionException.class, () -> useCase.execute(new LoginUserCommand("man10h", "wrong")));
    }

    private static final class SingleUserRepository implements UserAccountRepository {
        private final Map<String, User> users;

        private SingleUserRepository(User user) {
            this.users = Map.of(user.id(), user);
        }

        @Override
        public Optional<User> findById(String id) {
            return Optional.ofNullable(users.get(id));
        }

        @Override
        public Optional<User> findProfileById(String id) {
            return findById(id);
        }

        @Override
        public Optional<User> findByUsername(String username) {
            return users.values().stream().filter(user -> user.username().equals(username)).findFirst();
        }

        @Override
        public Optional<User> findByEmail(String email) {
            return users.values().stream().filter(user -> user.email().equals(email)).findFirst();
        }

        @Override
        public User save(User user) {
            return user;
        }

        @Override
        public PageResult<User> searchByName(String name, PageQuery pageQuery) {
            return new PageResult<>(users.values().stream().toList(), pageQuery.page(), pageQuery.size(), users.size(), 1);
        }
    }

    private static final class FakePasswordHasher implements PasswordHasher {
        @Override
        public String hash(String rawPassword) {
            return "hashed-" + rawPassword;
        }

        @Override
        public boolean matches(String rawPassword, String hashedPassword) {
            return ("hashed-" + rawPassword).equals(hashedPassword);
        }
    }

    private static final class RecordingAuthenticator implements CredentialsAuthenticator {
        private boolean called;

        @Override
        public void authenticate(String username, String password) {
            called = true;
        }
    }
}
