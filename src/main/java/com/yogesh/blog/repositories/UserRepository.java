package com.yogesh.blog.repositories;


import com.yogesh.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select user from User user where user.name = :username")
    Optional<User> findUserByName(String username);
}
