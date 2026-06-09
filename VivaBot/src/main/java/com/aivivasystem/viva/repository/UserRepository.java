package com.aivivasystem.viva.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.aivivasystem.viva.model.User;
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
}

