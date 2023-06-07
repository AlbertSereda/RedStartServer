package com.redstart.server.database.service;

import com.redstart.server.core.dto.UserDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserService {
    UserDTO createUser(String login, String password, String email);
    UserDTO getUser(String login, String email);
}
