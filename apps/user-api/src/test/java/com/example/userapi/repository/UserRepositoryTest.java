package com.example.userapi.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.core.testing.annotation.IntegrationTest;
import com.example.userapi.model.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

@IntegrationTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void save_ShouldPersistUser() {
        User user = new User("John Doe", "john@example.com");

        User savedUser = userRepository.save(user);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("John Doe");
        assertThat(savedUser.getEmail()).isEqualTo("john@example.com");
        assertThat(savedUser.getCreatedAt()).isNotNull();
        assertThat(savedUser.getUpdatedAt()).isNotNull();
    }

    @Test
    void findById_ShouldReturnUser_WhenExists() {
        User user = new User("John Doe", "john@example.com");
        User savedUser = userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("John Doe");
        assertThat(foundUser.get().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        Optional<User> foundUser = userRepository.findById(999L);

        assertThat(foundUser).isEmpty();
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        User user1 = new User("John Doe", "john@example.com");
        User user2 = new User("Jane Smith", "jane@example.com");

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();

        assertThat(users).hasSize(2);
        assertThat(users).extracting(User::getName).containsExactlyInAnyOrder("John Doe", "Jane Smith");
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenExists() {
        User user = new User("John Doe", "john@example.com");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail("john@example.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("John Doe");
    }

    @Test
    void deleteById_ShouldRemoveUser() {
        User user = new User("John Doe", "john@example.com");
        User savedUser = userRepository.save(user);

        userRepository.deleteById(savedUser.getId());

        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertThat(foundUser).isEmpty();
    }

    @Test
    void existsById_ShouldReturnTrue_WhenExists() {
        User user = new User("John Doe", "john@example.com");
        User savedUser = userRepository.save(user);

        boolean exists = userRepository.existsById(savedUser.getId());

        assertThat(exists).isTrue();
    }

    @Test
    void existsById_ShouldReturnFalse_WhenNotExists() {
        boolean exists = userRepository.existsById(999L);

        assertThat(exists).isFalse();
    }
}
