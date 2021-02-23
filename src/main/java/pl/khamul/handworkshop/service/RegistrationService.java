package pl.khamul.handworkshop.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.khamul.handworkshop.exception.UserAlreadyExistsException;
import pl.khamul.handworkshop.entity.User;
import pl.khamul.handworkshop.repository.RoleRepository;
import pl.khamul.handworkshop.repository.UserRepository;
import pl.khamul.handworkshop.transfer.UserDto;

import javax.transaction.Transactional;
import java.util.Arrays;

@Service
public class RegistrationService implements RegistrationServiceInterface {


    private UserRepository userRepository;
    private BCryptPasswordEncoder encoder;
    private RoleRepository roleRepository;

    public RegistrationService(UserRepository userRepository, BCryptPasswordEncoder encoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
    }

    @Transactional
    @Override
    public User registerNewUserAccount(UserDto userDto)
            throws UserAlreadyExistsException {

        if (emailExists(userDto.getEmail())) {
            throw new UserAlreadyExistsException(
                    "Ten adres jest juz w bazie"
                            + userDto.getEmail());
        }
        User user = new User();
        user.setUserName(userDto.getUserName());
        String encodedPass = encoder.encode(userDto.getPassword());
        user.setPassword(encodedPass);
        user.setEmail(userDto.getEmail());
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
        return userRepository.save(user);
    }

    private boolean emailExists(String email) {
        return userRepository.findFirstByEmail(email) != null;
    }

}
