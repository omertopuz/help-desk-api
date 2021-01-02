package com.auth.server.repository;

import com.auth.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepo extends JpaRepository<User,Integer> {
    Optional<User> findByUsername(String name);
}
