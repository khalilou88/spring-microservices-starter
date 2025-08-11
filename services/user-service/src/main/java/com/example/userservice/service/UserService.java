package com.example.userservice.service;

import com.example.core.messaging.producer.EventPublisher;
import com.example.core.web.response.PageResult;
import com.example.userservice.event.UserCreatedEvent;
import com.example.userservice.event.UserDeletedEvent;
import com.example.userservice.event.UserUpdatedEvent;
import com.example.userservice.exception.UserAlreadyExistsException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final String USER_EVENTS_TOPIC = "user-events";

    private final UserRepository userRepository;
    private final EventPublisher eventPublisher;

    public UserService(UserRepository userRepository, EventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    public User createUser(User user) {
        logger.info("Creating user: {}", user.getEmail());

        // Check if user already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }

        User savedUser = userRepository.save(user);

        // Publish event
        UserCreatedEvent event = new UserCreatedEvent(savedUser.getId(), savedUser.getName(), savedUser.getEmail());
        eventPublisher.publishEvent(USER_EVENTS_TOPIC, savedUser.getId().toString(), event);

        logger.info("User created successfully: {}", savedUser.getId());
        return savedUser;
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public PageResult<User> getAllUsers(int page, int size) {
        int offset = page * size;
        List<User> users = userRepository.findAll(size, offset);
        long total = userRepository.count();
        return new PageResult<>(users, page, size, total);
    }

    public User updateUser(Long id, User userUpdate) {
        logger.info("Updating user: {}", id);

        User existingUser = getUserById(id);

        // Check if email is being changed and if new email already exists
        if (!existingUser.getEmail().equals(userUpdate.getEmail())) {
            userRepository.findByEmail(userUpdate.getEmail()).ifPresent(user -> {
                throw new UserAlreadyExistsException("User with email " + userUpdate.getEmail() + " already exists");
            });
        }

        existingUser.setName(userUpdate.getName());
        existingUser.setEmail(userUpdate.getEmail());

        User updatedUser = userRepository.save(existingUser);

        // Publish event
        UserUpdatedEvent event =
                new UserUpdatedEvent(updatedUser.getId(), updatedUser.getName(), updatedUser.getEmail());
        eventPublisher.publishEvent(USER_EVENTS_TOPIC, updatedUser.getId().toString(), event);

        logger.info("User updated successfully: {}", updatedUser.getId());
        return updatedUser;
    }

    public void deleteUser(Long id) {
        logger.info("Deleting user: {}", id);

        User user = getUserById(id);
        userRepository.deleteById(id);

        // Publish event
        UserDeletedEvent event = new UserDeletedEvent(user.getId(), user.getEmail());
        eventPublisher.publishEvent(USER_EVENTS_TOPIC, user.getId().toString(), event);

        logger.info("User deleted successfully: {}", id);
    }
}
