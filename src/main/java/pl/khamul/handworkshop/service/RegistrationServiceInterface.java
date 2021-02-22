package pl.khamul.handworkshop.service;

import pl.khamul.handworkshop.exception.UserAlreadyExistsException;
import pl.khamul.handworkshop.entity.User;
import pl.khamul.handworkshop.transfer.UserDto;

public interface RegistrationServiceInterface {

    User registerNewUserAccount(UserDto userDto)
            throws UserAlreadyExistsException;
}
