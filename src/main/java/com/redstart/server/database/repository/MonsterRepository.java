package com.redstart.server.database.repository;

import com.redstart.server.database.entity.MonsterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonsterRepository extends JpaRepository<MonsterEntity, Integer> {
}
