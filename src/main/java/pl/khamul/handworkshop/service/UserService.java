package pl.khamul.handworkshop.service;

import org.springframework.stereotype.Service;
import pl.khamul.handworkshop.entity.User;
import pl.khamul.handworkshop.entity.UserNames;
import pl.khamul.handworkshop.repository.UserDetailsRepo;
import pl.khamul.handworkshop.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Service
public class UserService implements UserServiceInterface {
    private final UserRepository userRepository;
    private final UserDetailsRepo userDetailsRepo;

    public UserService(UserRepository userRepository, UserDetailsRepo userDetailsRepo) {
        this.userRepository = userRepository;
        this.userDetailsRepo = userDetailsRepo;
    }

    public User getUser(HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        User user = userRepository.findFirstByEmail(principal.getName());

        return user;
    }

    public void setDetails(HttpServletRequest request, UserNames details) {

        User user = getUser(request);

        user.setDetails(details);

        userDetailsRepo.save(details);
        userRepository.save(user);
    }
    public List<User> userList(){
        System.out.println(userRepository.findAll());
        return userRepository.findAll();
    }
}
