package com.redstart.server.database.repository;

import com.redstart.server.database.entity.SpellForUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpellForUserRepository extends JpaRepository<SpellForUserEntity, Integer> {
    Optional<SpellForUserEntity> findByLoginAndName(String login, String name);
    List<SpellForUserEntity> findAllByLogin(String login);
}
