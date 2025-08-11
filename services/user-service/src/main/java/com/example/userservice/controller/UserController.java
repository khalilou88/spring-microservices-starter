package com.example.userservice.controller;

import com.example.core.web.controller.BaseController;
import com.example.core.web.response.ApiResponse;
import com.example.core.web.response.PageResult;
import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return created(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        return success(user);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<User>>> getAllUsers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        PageResult<User> users = userService.getAllUsers(page, size);
        return pagedSuccess(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable("id") Long id, @Valid @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return success("User updated successfully", updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return noContent();
    }
}
