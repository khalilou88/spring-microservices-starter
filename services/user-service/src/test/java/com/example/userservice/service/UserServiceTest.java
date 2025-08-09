package com.example.userservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.example.core.messaging.producer.EventPublisher;
import com.example.userservice.exception.UserAlreadyExistsException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventPublisher eventPublisher;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, eventPublisher);
    }

    @Test
    void createUser_ShouldCreateUser_WhenEmailNotExists() {
        User user = new User("John Doe", "john@example.com");
        User savedUser = new User("John Doe", "john@example.com");
        savedUser.setId(1L);

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(savedUser);

        User result = userService.createUser(user);

        assertThat(result).isEqualTo(savedUser);
        verify(userRepository).findByEmail("john@example.com");
        verify(userRepository).save(user);
        verify(eventPublisher).publishEvent(anyString(), anyString(), any());
    }

    @Test
    void createUser_ShouldThrowException_WhenEmailExists() {
        User user = new User("John Doe", "john@example.com");
        User existingUser = new User("Existing User", "john@example.com");
        existingUser.setId(1L);

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(existingUser));

        assertThatThrownBy(() -> userService.createUser(user))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("User with email john@example.com already exists");

        verify(userRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(anyString(), anyString(), any());
    }

    @Test
    void getUserById_ShouldReturnUser_WhenExists() {
        User user = new User("John Doe", "john@example.com");
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertThat(result).isEqualTo(user);
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_ShouldThrowException_WhenNotExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(1L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found with id: 1");

        verify(userRepository).findById(1L);
    }

    @Test
    void updateUser_ShouldUpdateUser_WhenValidData() {
        User existingUser = new User("John Doe", "john@example.com");
        existingUser.setId(1L);

        User updateData = new User("John Updated", "john.updated@example.com");

        User updatedUser = new User("John Updated", "john.updated@example.com");
        updatedUser.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail("john.updated@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(1L, updateData);

        assertThat(result.getName()).isEqualTo("John Updated");
        assertThat(result.getEmail()).isEqualTo("john.updated@example.com");
        verify(eventPublisher).publishEvent(anyString(), anyString(), any());
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenExists() {
        User user = new User("John Doe", "john@example.com");
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
        verify(eventPublisher).publishEvent(anyString(), anyString(), any());
    }
}
