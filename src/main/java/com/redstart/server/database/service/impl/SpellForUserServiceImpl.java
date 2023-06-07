package com.redstart.server.database.service.impl;

import com.redstart.server.core.dto.IslandDTO;
import com.redstart.server.core.dto.SpellDTO;
import com.redstart.server.core.dto.SpellForUserDTO;
import com.redstart.server.core.dto.UserDTO;
import com.redstart.server.core.socket.message.responsedata.ErrorResponse;
import com.redstart.server.database.entity.SpellDefaultEntity;
import com.redstart.server.database.entity.SpellForUserEntity;
import com.redstart.server.database.repository.SpellDefaultRepository;
import com.redstart.server.database.repository.SpellForUserRepository;
import com.redstart.server.database.service.IslandService;
import com.redstart.server.database.service.SpellDefaultService;
import com.redstart.server.database.service.SpellForUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SpellForUserServiceImpl implements SpellForUserService {
    private static final Logger log = LoggerFactory.getLogger(SpellForUserServiceImpl.class);
    private final SpellDefaultRepository repository;
    private final IslandService islandService;
    private final SpellForUserRepository spellForUserRepository;
    private final SpellDefaultRepository spellDefaultRepository;
    private final SpellDefaultService spellDefaultService;

    public SpellForUserServiceImpl(SpellDefaultRepository repository,
                                   IslandService islandService,
                                   SpellForUserRepository spellForUserRepository,
                                   SpellDefaultRepository spellDefaultRepository,
                                   SpellDefaultService spellDefaultService) {
        this.repository = repository;
        this.islandService = islandService;
        this.spellForUserRepository = spellForUserRepository;
        this.spellDefaultRepository = spellDefaultRepository;
        this.spellDefaultService = spellDefaultService;
    }

    @Override
    public List<SpellForUserDTO> getSelectedSpells(UserDTO user, Set<String> nameSelectedSpells) {
        List<SpellForUserDTO> spellsForUser = user.getSpellsForUser();

        return spellsForUser.stream()
                .filter(spellForUserDTO -> nameSelectedSpells.contains(spellForUserDTO.getName()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateUserSpellFromBoss(UserDTO user, String islandName) {
        Optional<IslandDTO> maybeIsland = islandService.getAllIslands()
                .stream()
                .filter(islandDTO -> islandDTO.getName().equals(islandName))
                .findFirst();

        if (maybeIsland.isPresent()) {
            IslandDTO islandDTO = maybeIsland.get();
            SpellDTO spellDTO = islandDTO.getSpell();

            Optional<SpellForUserDTO> maybeSpellForUser = user.getSpellsForUser()
                    .stream()
                    .filter(spellForUserDTO -> spellForUserDTO.getName().equals(spellDTO.getName()))
                    .findFirst();

            if (maybeSpellForUser.isPresent()) {
                //у пользователя уже есть заклинание, значит улучшаем его
                SpellForUserDTO spellForUserDTO = maybeSpellForUser.get();

                updateUserSpell(user.getLogin(), spellForUserDTO.getName());
            } else {
                //у пользователя нет такого заклинания, добавляем ему
                SpellForUserEntity spellForUserEntity = new SpellForUserEntity();
                Optional<SpellDefaultEntity> maybeSpellDefault = spellDefaultRepository.findById(spellDTO.getName());

                if (maybeSpellDefault.isPresent()) {
                    SpellDefaultEntity spellDefaultEntity = maybeSpellDefault.get();

                    spellForUserEntity.setName(spellDefaultEntity.getName());
                    spellForUserEntity.setCostMana(spellDefaultEntity.getCostMana());
                    spellForUserEntity.setDamage(spellDefaultEntity.getDamage());
                    spellForUserEntity.setDurationTime(spellDefaultEntity.getDurationTime());
                    spellForUserEntity.setLogin(user.getLogin());
                    spellForUserEntity.setLevel(1);
                    spellForUserRepository.save(spellForUserEntity);
                    log.info("Added Spell {} for user", spellForUserEntity);
                }
            }
        } else {
            log.error("Island {} not found", islandName);
        }
    }

    @Override
    @Transactional
    public void updateUserSpell(String login, String nameUpgradedSpell) {
        Optional<SpellForUserEntity> maybeSpellForUser = spellForUserRepository.findByLoginAndName(login, nameUpgradedSpell);

        if (maybeSpellForUser.isPresent()) {
            SpellForUserEntity spellForUserEntity = maybeSpellForUser.get();
            int levelSpell = spellForUserEntity.getLevel();
            if (levelSpell < 10) {
                boostSpell(spellForUserEntity);
                spellForUserRepository.save(spellForUserEntity);
                log.info("Updated Spell {} for user", spellForUserEntity);
            } else {
                throw new IllegalStateException(ErrorResponse.SPELL_MAXIMUM_LEVEL.getErrorMessage());
            }
        } else {
            throw new IllegalStateException(ErrorResponse.SPELL_FOR_USER_NOT_FOUND.getErrorMessage());
        }
    }

    private void boostSpell(SpellForUserEntity spellForUserEntity) {
        switch (spellForUserEntity.getName()) {
            case "arrow":
            case "fireBall":
                spellForUserEntity.setDamage(spellForUserEntity.getDamage() + 1);
                break;
        }
        spellForUserEntity.setLevel(spellForUserEntity.getLevel() + 1);
    }

    @Override
    @Transactional
    public Optional<SpellForUserEntity> findById(int id) {
        return spellForUserRepository.findById(id);
    }

    @Override
    @Transactional
    public List<SpellForUserDTO> getAllSpellForUser(String login) {
        return spellForUserRepository
                .findAllByLogin(login)
                .stream()
                .map(SpellForUserDTO::of)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<SpellForUserEntity> getDefaultSpellsForUser(String login) {
        return spellDefaultService
                .getDefaultSpellsForNewUser()
                .stream().map(spellDefault -> {
                    SpellForUserEntity spellForUser = new SpellForUserEntity();
                    spellForUser.setName(spellDefault.getName());
                    spellForUser.setCostMana(spellDefault.getCostMana());
                    spellForUser.setDamage(spellDefault.getDamage());
                    spellForUser.setDurationTime(spellDefault.getDurationTime());
                    spellForUser.setLogin(login);
                    spellForUser.setLevel(1);
                    return spellForUser;
                })
                .toList();
    }
}
