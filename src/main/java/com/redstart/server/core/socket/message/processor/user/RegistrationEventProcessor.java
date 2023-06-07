package com.redstart.server.core.socket.message.processor.user;

import com.redstart.server.core.dto.IslandDTO;
import com.redstart.server.core.dto.UserDTO;
import com.redstart.server.core.socket.SocketClient;
import com.redstart.server.core.socket.message.SocketEventType;
import com.redstart.server.core.socket.message.processor.NoAuthSocketEventProcessor;
import com.redstart.server.core.socket.message.requestdata.user.RegistrationRequestData;
import com.redstart.server.core.socket.message.responsedata.ErrorResponse;
import com.redstart.server.core.socket.message.responsedata.user.LoginResponseData;
import com.redstart.server.database.service.IslandService;
import com.redstart.server.database.service.UserService;
import com.redstart.server.exception.LoginOrEmailAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RegistrationEventProcessor extends NoAuthSocketEventProcessor<RegistrationRequestData, LoginResponseData> {
    private static final Logger log = LoggerFactory.getLogger(RegistrationEventProcessor.class);
    private final UserService userService;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private final Pattern pattern;
    private Matcher matcher;
    private final String serverMessage;

    private final IslandService islandService;

    public RegistrationEventProcessor(UserService userService,
                                      @Value("${serverMessage}") String serverMessage,
                                      IslandService islandService) {
        this.userService = userService;
        this.serverMessage = serverMessage;
        this.islandService = islandService;
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    @Override
    protected LoginResponseData action(RegistrationRequestData data, SocketClient socketClient) {
        ErrorResponse validateError = validate(data);
        if (validateError == ErrorResponse.NO_ERROR) {
            try {
                UserDTO userDTO = userService.createUser(data.getLogin(), data.getPassword(), data.getEmail());
                socketClient.setLogin(userDTO.getLogin());

                List<IslandDTO> islandsDTO = islandService.getAllIslands();
                return LoginResponseData.ofSuccess(userDTO, islandsDTO, serverMessage);
            } catch (LoginOrEmailAlreadyExistsException e) {
                log.info("{}. Login: {}, Email: {}", e.getMessage(), data.getLogin(), data.getEmail());
                throw new IllegalStateException(e.getMessage());
            }
        } else {
            log.info("Validate error : {}. Login {}, Email {}", validateError.getErrorMessage(), data.getLogin(), data.getEmail());
            throw new IllegalStateException(validateError.getErrorMessage());
        }
    }

    @Override
    public SocketEventType getEventType() {
        return SocketEventType.USER_REGISTRATION;
    }

    private ErrorResponse validate(RegistrationRequestData data) {
        matcher = pattern.matcher(data.getEmail());
        if (!matcher.matches()) {
            return ErrorResponse.INCORRECT_EMAIL;
        }

        data.setPassword(data.getPassword().trim());
        if (data.getPassword().length() < 6) {
            return ErrorResponse.PASSWORD_LESS_CHARACTER;
        }

        data.setLogin(data.getLogin().trim());
        if (data.getLogin().length() < 4) {
            return ErrorResponse.LOGIN_LESS_CHARACTER;
        }

        return ErrorResponse.NO_ERROR;
    }
}
