package com.goorm.devlink.authservice.repository;

import com.goorm.devlink.authservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserUuid(String userUuid);

    Optional<User> findByEmail(String email);
}
