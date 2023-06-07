package com.redstart.server.database.repository;

import com.redstart.server.database.entity.LevelCompletedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LevelCompletedRepository extends JpaRepository<LevelCompletedEntity, Integer> {
}
