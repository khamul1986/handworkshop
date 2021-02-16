package pl.khamul.handworkshop.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.khamul.handworkshop.Exception.UserAlreadyExistsException;
import pl.khamul.handworkshop.entity.User;
import pl.khamul.handworkshop.repository.UserRepository;
import pl.khamul.handworkshop.transfer.UserDto;

import javax.transaction.Transactional;

@Service
public class UserService implements UserServiceInterface{

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public User registerNewUserAccount(UserDto userDto)
            throws UserAlreadyExistsException {

        if (emailExists(userDto.getEmail())) {
            throw new UserAlreadyExistsException(
                    "There is an account with that email address:"
                            + userDto.getEmail());
        }
        User user = new User();
        user.setUserName(userDto.getFirstName());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
       /* user.setRoles(Arrays.asList("ROLE_USER"));*/
        return userRepository.save(user);
    }

    private boolean emailExists(String email) {
        return userRepository.findFirstByEmail(email) != null;
    }

}
