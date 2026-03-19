package com.Man10h.social_network_app.clean_architecture.application.usecase.auth;

import com.Man10h.social_network_app.clean_architecture.application.port.MailGateway;
import com.Man10h.social_network_app.clean_architecture.application.port.PasswordHasher;
import com.Man10h.social_network_app.clean_architecture.application.shared.PageQuery;
import com.Man10h.social_network_app.clean_architecture.application.shared.PageResult;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.ConflictException;
import com.Man10h.social_network_app.clean_architecture.domain.user.entity.User;
import com.Man10h.social_network_app.clean_architecture.domain.user.entity.UserRole;
import com.Man10h.social_network_app.clean_architecture.domain.user.repository.UserAccountRepository;
import com.Man10h.social_network_app.clean_architecture.domain.user.repository.UserRoleRepository;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RegisterUserUseCaseTest {
    @Test
    void registersNewUserAndSendsMail() {
        InMemoryUserRepository userRepository = new InMemoryUserRepository();
        FakeMailGateway mailGateway = new FakeMailGateway();
        RegisterUserUseCase useCase = new RegisterUserUseCase(
                userRepository,
                () -> Optional.of(new UserRole(2L, "USER")),
                new FakePasswordHasher(),
                mailGateway
        );

        User user = useCase.execute(new RegisterUserCommand("man10h", "secret", "man10h@email.com", "Man", "Ten", "M"));

        assertNotNull(user.id());
        assertEquals("hashed-secret", user.passwordHash());
        assertEquals("man10h@email.com", mailGateway.lastRecipient);
        assertEquals(1, userRepository.storage.size());
    }

    @Test
    void rejectsDuplicateUsername() {
        InMemoryUserRepository userRepository = new InMemoryUserRepository();
        userRepository.save(User.registerNew("man10h", "first@email.com", "hashed-secret", "Man", "Ten", "M", new UserRole(2L, "USER")));

        RegisterUserUseCase useCase = new RegisterUserUseCase(
                userRepository,
                () -> Optional.of(new UserRole(2L, "USER")),
                new FakePasswordHasher(),
                new FakeMailGateway()
        );

        assertThrows(
                ConflictException.class,
                () -> useCase.execute(new RegisterUserCommand("man10h", "secret", "second@email.com", "Other", "User", "F"))
        );
    }

    private static final class InMemoryUserRepository implements UserAccountRepository {
        private final Map<String, User> storage = new LinkedHashMap<>();
        private int sequence = 1;

        @Override
        public Optional<User> findById(String id) {
            return Optional.ofNullable(storage.get(id));
        }

        @Override
        public Optional<User> findProfileById(String id) {
            return findById(id);
        }

        @Override
        public Optional<User> findByUsername(String username) {
            return storage.values().stream().filter(user -> user.username().equals(username)).findFirst();
        }

        @Override
        public Optional<User> findByEmail(String email) {
            return storage.values().stream().filter(user -> user.email().equals(email)).findFirst();
        }

        @Override
        public User save(User user) {
            String id = user.id() == null ? "user-" + sequence++ : user.id();
            User saved = new User(id, user.username(), user.email(), user.passwordHash(), user.firstName(), user.lastName(), user.gender(), user.enabled(), user.role(), user.profileImage());
            storage.put(saved.id(), saved);
            return saved;
        }

        @Override
        public PageResult<User> searchByName(String name, PageQuery pageQuery) {
            return new PageResult<>(storage.values().stream().toList(), pageQuery.page(), pageQuery.size(), storage.size(), 1);
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

    private static final class FakeMailGateway implements MailGateway {
        private String lastRecipient;

        @Override
        public void sendHtml(String to, String subject, String htmlContent) {
            this.lastRecipient = to;
        }
    }
}
