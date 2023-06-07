package com.redstart.server.database.service;

import com.redstart.server.database.entity.IslandCompletedEntity;

import java.util.List;
import java.util.Optional;

public interface IslandCompletedService {
    List<IslandCompletedEntity> findAll();

    Optional<IslandCompletedEntity> findById(int id);
}
