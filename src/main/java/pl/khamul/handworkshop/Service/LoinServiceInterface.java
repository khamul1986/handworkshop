package pl.khamul.handworkshop.Service;

import pl.khamul.handworkshop.Exception.UserAlreadyExistsException;
import pl.khamul.handworkshop.entity.User;
import pl.khamul.handworkshop.transfer.UserDto;

public interface LoinServiceInterface {

    User registerNewUserAccount(UserDto userDto)
            throws UserAlreadyExistsException;
}
