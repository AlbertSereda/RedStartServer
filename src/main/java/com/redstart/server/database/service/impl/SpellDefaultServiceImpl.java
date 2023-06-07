package com.redstart.server.database.service.impl;

import com.redstart.server.database.entity.SpellDefaultEntity;
import com.redstart.server.database.repository.SpellDefaultRepository;
import com.redstart.server.database.service.SpellDefaultService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SpellDefaultServiceImpl implements SpellDefaultService {
    private final SpellDefaultRepository repository;

    public SpellDefaultServiceImpl(SpellDefaultRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public List<SpellDefaultEntity> getDefaultSpellsForNewUser() {
        List<SpellDefaultEntity> defaultSpells = new ArrayList<>();
        Optional<SpellDefaultEntity> maybeArrow = repository.findById("arrow");
        if (maybeArrow.isPresent()) {
            SpellDefaultEntity arrow = maybeArrow.get();
            defaultSpells.add(arrow);
        }
        return defaultSpells;
    }
}
