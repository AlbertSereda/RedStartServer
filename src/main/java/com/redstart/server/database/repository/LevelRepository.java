package com.redstart.server.database.repository;

import com.redstart.server.database.entity.LevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LevelRepository extends JpaRepository<LevelEntity, Integer> {
}
