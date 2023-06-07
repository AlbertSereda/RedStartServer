package com.redstart.server.database.service.impl;

import com.redstart.server.database.entity.IslandCompletedEntity;
import com.redstart.server.database.repository.IslandCompletedRepository;
import com.redstart.server.database.service.IslandCompletedService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class IslandCompletedServiceImpl implements IslandCompletedService {
    private final IslandCompletedRepository repository;

    public IslandCompletedServiceImpl(IslandCompletedRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public List<IslandCompletedEntity> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public Optional<IslandCompletedEntity> findById(int id) {
        return repository.findById(id);
    }
}
