package com.redstart.server.database.service;

import com.redstart.server.database.entity.SpellDefaultEntity;

import java.util.List;

public interface SpellDefaultService {
    List<SpellDefaultEntity> getDefaultSpellsForNewUser();
}
