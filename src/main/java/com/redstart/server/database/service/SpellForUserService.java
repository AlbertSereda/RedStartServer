package com.redstart.server.database.service;

import com.redstart.server.core.dto.SpellForUserDTO;
import com.redstart.server.core.dto.UserDTO;
import com.redstart.server.database.entity.SpellForUserEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SpellForUserService {

    List<SpellForUserDTO> getSelectedSpells(UserDTO user, Set<String> nameSelectedSpells);

    void updateUserSpellFromBoss(UserDTO user, String islandName);

    void updateUserSpell(String login, String nameUpgradedSpell);

    Optional<SpellForUserEntity> findById(int id);

    List<SpellForUserDTO> getAllSpellForUser(String login);

    List<SpellForUserEntity> getDefaultSpellsForUser(String login);
}
