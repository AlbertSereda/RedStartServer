package com.redstart.server.database.service.impl;

import com.redstart.server.core.dto.UserDTO;
import com.redstart.server.database.entity.SpellForUserEntity;
import com.redstart.server.database.entity.UserDataEntity;
import com.redstart.server.database.repository.UserDataRepository;
import com.redstart.server.database.service.SpellForUserService;
import com.redstart.server.database.service.UserDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserDataServiceImpl implements UserDataService {
    private static final Logger log = LoggerFactory.getLogger(UserDataServiceImpl.class);
    private final UserDataRepository userDataRepository;
    private final int initMoney;
    private final SpellForUserService spellForUserService;

    public UserDataServiceImpl(UserDataRepository userDataRepository,
                               @Value("${adventure.initMoney}") int initMoney,
                               SpellForUserService spellForUserService) {
        this.userDataRepository = userDataRepository;
        this.initMoney = initMoney;
        this.spellForUserService = spellForUserService;
    }

    @Override
    @Transactional
    public UserDTO getUserdata(String login) {
        Optional<UserDataEntity> maybeUserData = userDataRepository.findById(login);
        if (maybeUserData.isPresent()) {
            UserDataEntity userDataEntity = maybeUserData.get();
            return UserDTO.of(userDataEntity);
        } else {
            throw new IllegalStateException("UserData not found for : " + login);
        }
    }

    @Override
    @Transactional
    public void updateUserMoney(UserDTO userDTO) {
        Optional<UserDataEntity> maybeUserData = userDataRepository.findById(userDTO.getLogin());
        if (maybeUserData.isPresent()) {
            UserDataEntity userDataEntity = maybeUserData.get();
            int oldCountMoney = userDataEntity.getMoney();
            userDataEntity.setMoney(userDTO.getMoney());
            userDataRepository.save(userDataEntity);
            log.info("Money for user {} updated. Old count money : {}, new count money : {}", userDTO.getLogin(), oldCountMoney, userDTO.getMoney());
        } else {
            throw new IllegalStateException("UserData not found for : " + userDTO.getLogin());
        }
    }

    @Override
    @Transactional
    public UserDTO createUserData(String login) {
        UserDataEntity userData = new UserDataEntity();
        userData.setLoginUser(login);
        userData.setLevel(1);
        userData.setMoney(initMoney);
        userData.setExperience(0);

        List<SpellForUserEntity> spellsForUser = spellForUserService.getDefaultSpellsForUser(login);

        userData.setSpellsForUser(spellsForUser);

        userDataRepository.save(userData);
        return UserDTO.of(userData);
    }

    @Override
    @Transactional
    public void updateUserExperience(UserDTO userDTO, int earnedExperience) {
        int oldUserExperience = userDTO.getExperience();
        int newUserExperience = oldUserExperience + earnedExperience;

        if (newUserExperience >= 3600) {
            //увеличиваем уровень и опыт
            userDTO.setLevel(userDTO.getLevel() + 1);
            userDTO.setExperience(newUserExperience - 3600);
            log.info("Level up for user {}. New level : {}", userDTO.getLogin(), userDTO.getLevel());
        } else {
            //увеличиваем только опыт
            userDTO.setExperience(newUserExperience);
        }
        Optional<UserDataEntity> maybeUserData = userDataRepository.findById(userDTO.getLogin());
        if (maybeUserData.isPresent()) {
            UserDataEntity userDataEntity = maybeUserData.get();
            userDataEntity.setLevel(userDTO.getLevel());
            userDataEntity.setExperience(userDTO.getExperience());
            userDataRepository.save(userDataEntity);
            log.info("Experience for user {} updated. Earned experience : {}", userDTO.getLogin(), earnedExperience);
        }
    }

    @Override
    @Transactional
    public List<UserDataEntity> getUsersDataToLeaderboard() {
        return userDataRepository.findAllByOrderByLevelDesc(Pageable.ofSize(10));
    }
}
