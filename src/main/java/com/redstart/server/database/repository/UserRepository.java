package com.redstart.server.database.repository;

import com.redstart.server.database.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByLoginIgnoreCaseOrEmailIgnoreCase(String login, String email);

    Optional<UserEntity> findByLoginIgnoreCaseAndPassword(String login, String password);
}
