package com.yogesh.blog.services;

import com.yogesh.blog.entities.User;
import com.yogesh.blog.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void addUser(User user){
        userRepository.save(user);
    }

    public User getUser(Integer userId){
        return userRepository.getById(userId);
    }

}
