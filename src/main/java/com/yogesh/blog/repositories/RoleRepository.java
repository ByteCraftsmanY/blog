package com.yogesh.blog.repositories;

import com.yogesh.blog.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query("select role from Role role where role.name = :name")
    Optional<Role> findRoleByName(String name);
}
