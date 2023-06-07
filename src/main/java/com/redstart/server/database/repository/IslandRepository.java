package com.redstart.server.database.repository;

import com.redstart.server.database.entity.IslandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IslandRepository extends JpaRepository<IslandEntity, String> {
}
