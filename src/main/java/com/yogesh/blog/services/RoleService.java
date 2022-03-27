package com.yogesh.blog.services;

import com.yogesh.blog.models.Role;
import com.yogesh.blog.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Optional<Role> findRoleByName(String roleName) {
        return roleRepository.findRoleByName(roleName);
    }
}
