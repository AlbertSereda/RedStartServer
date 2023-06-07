package com.redstart.server.database.service.impl;

import com.redstart.server.database.repository.LevelRepository;
import com.redstart.server.database.service.LevelService;
import org.springframework.stereotype.Service;

@Service
public class LevelServiceImpl implements LevelService {
    private final LevelRepository repository;

    public LevelServiceImpl(LevelRepository repository) {
        this.repository = repository;
    }


}
