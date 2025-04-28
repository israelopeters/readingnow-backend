package com.israelopeters.ReadingNow_Backend.repository;


import com.israelopeters.ReadingNow_Backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String roleName);
}
