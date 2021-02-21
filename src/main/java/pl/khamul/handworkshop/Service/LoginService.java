package pl.khamul.handworkshop.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.khamul.handworkshop.Exception.UserAlreadyExistsException;
import pl.khamul.handworkshop.entity.User;
import pl.khamul.handworkshop.repository.UserRepository;
import pl.khamul.handworkshop.transfer.UserDto;

import javax.transaction.Transactional;

@Service
public class LoginService implements LoinServiceInterface {


    private UserRepository userRepository;
    private BCryptPasswordEncoder encoder;

    public LoginService(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

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
        user.setUserName(userDto.getUserName());
        String encodedPass = encoder.encode(userDto.getPassword());
        user.setPassword(encodedPass);
        user.setEmail(userDto.getEmail());
       /* user.setRoles(Arrays.asList("ROLE_USER"));*/
        return userRepository.save(user);
    }

    private boolean emailExists(String email) {
        return userRepository.findFirstByEmail(email) != null;
    }

}
