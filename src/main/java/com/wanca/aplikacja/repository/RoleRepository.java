package com.wanca.aplikacja.repository;

import com.wanca.aplikacja.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
