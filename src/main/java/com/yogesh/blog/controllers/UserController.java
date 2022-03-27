package com.yogesh.blog.controllers;

import com.yogesh.blog.exceptions.EntityNotFoundException;
import com.yogesh.blog.exceptions.UnauthorizedException;
import com.yogesh.blog.models.Role;
import com.yogesh.blog.models.User;
import com.yogesh.blog.services.RoleService;
import com.yogesh.blog.services.UserPrincipalService;
import com.yogesh.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;
    private final UserPrincipalService userPrincipalService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, RoleService roleService, UserPrincipalService userPrincipalService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.userPrincipalService = userPrincipalService;
        this.passwordEncoder = passwordEncoder;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping
    public List<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("{id}")
    public User getUserById(@PathVariable Integer id) {
        User user = userService.findUserById(id).orElseThrow(() -> new EntityNotFoundException("User not found."));
        if (!userPrincipalService.isUserAuthorizedForUserOperation(user)) {
            throw new UnauthorizedException("You are not authorized to do this.");
        }
        return user;
    }

    @PostMapping
    public User saveUser(@RequestBody User user) {
        List<Role> userRoles = user.getRoles();
        userRoles.forEach(userRole -> {
            String roleName = userRole.getName();
            Role role = roleService.findRoleByName(roleName).orElse(new Role(roleName));
            userRoles.add(role);
        });
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        user.setRoles(userRoles);
        userService.saveUser(user);
        return user;
    }

    @DeleteMapping("{id}")
    public void deleteUserById(@PathVariable Integer id) {
        User user = userService.findUserById(id).orElseThrow(() -> new EntityNotFoundException("User not found."));
        if (!userPrincipalService.isUserAuthorizedForUserOperation(user)) {
            throw new UnauthorizedException("You are not authorized to do this.");
        }
        userService.deleteUserById(id);
    }

    @PatchMapping("{id}")
    public User updateUserById(@PathVariable Integer id, @RequestBody Map<Object, Object> userFields) {
        User user = userService.findUserById(id).orElseThrow(() -> new EntityNotFoundException("User not found."));
        userFields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(User.class, (String) key);
            Objects.requireNonNull(field).setAccessible(true);
            ReflectionUtils.setField(field, user, value);
        });
        return userService.saveUser(user);
    }
}
