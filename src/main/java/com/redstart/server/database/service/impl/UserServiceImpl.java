package com.redstart.server.database.service.impl;

import com.redstart.server.core.dto.UserDTO;
import com.redstart.server.core.socket.message.responsedata.ErrorResponse;
import com.redstart.server.database.entity.UserDataEntity;
import com.redstart.server.database.entity.UserEntity;
import com.redstart.server.database.repository.UserDataRepository;
import com.redstart.server.database.repository.UserRepository;
import com.redstart.server.database.service.SpellDefaultService;
import com.redstart.server.database.service.UserDataService;
import com.redstart.server.database.service.UserService;
import com.redstart.server.exception.LoginOrEmailAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final SpellDefaultService spellDefaultService;
    private final UserDataRepository userDataRepository;
    private final UserDataService userDataService;


    public UserServiceImpl(UserRepository userRepository,
                           SpellDefaultService spellDefaultService,
                           UserDataRepository userDataRepository, UserDataService userDataService) {
        this.userRepository = userRepository;
        this.spellDefaultService = spellDefaultService;
        this.userDataRepository = userDataRepository;
        this.userDataService = userDataService;
    }

    @Override
    @Transactional
    public UserDTO createUser(String login, String password, String email) {
        Optional<UserEntity> maybeUser = userRepository.findByLoginIgnoreCaseOrEmailIgnoreCase(login, email);
        if (maybeUser.isEmpty()) {
            //создаем нового пользователя
            log.info("Start creating user : {}", login);
            UserEntity user = new UserEntity();
            user.setLogin(login);
            user.setPassword(password);
            user.setEmail(email);
            user.setDateRegistration(LocalDateTime.now());
            user.setActive(true);
            userRepository.save(user);

            UserDTO userData = userDataService.createUserData(login);
            log.info("User created : {}", userData);
            return userData;
        } else {
            throw new LoginOrEmailAlreadyExistsException(ErrorResponse.LOGIN_OR_EMAIL_ALREADY_EXISTS.getErrorMessage());
        }
    }

    @Override
    @Transactional
    public UserDTO getUser(String login, String password) {
        Optional<UserEntity> maybeUser = userRepository.findByLoginIgnoreCaseAndPassword(login, password);
        if (maybeUser.isPresent()) {
            UserEntity entity = maybeUser.get();
            Optional<UserDataEntity> maybeDataEntity = userDataRepository.findById(entity.getLogin());

            if (maybeDataEntity.isPresent()) {
                return UserDTO.of(maybeDataEntity.get());
            } else {
                log.error("User data is empty for : {}", login);
            }
        }
        throw new IllegalStateException(ErrorResponse.INCORRECT_LOGIN_OR_PASSWORD.getErrorMessage());
    }
}
