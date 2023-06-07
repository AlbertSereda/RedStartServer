package com.redstart.server.database.repository;

import com.redstart.server.database.entity.IslandCompletedEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IslandCompletedRepository extends JpaRepository<IslandCompletedEntity, Integer> {
}
