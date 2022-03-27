package com.yogesh.blog.services;

import com.yogesh.blog.model.Role;
import com.yogesh.blog.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Optional<Role> findRoleByName (String name){
        return  roleRepository.findRoleByName(name);
    }
}
