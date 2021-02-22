package pl.khamul.handworkshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.khamul.handworkshop.entity.LoginDetails;
import pl.khamul.handworkshop.entity.User;
import pl.khamul.handworkshop.repository.UserRepository;

public class LoginDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public LoginDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findFirstByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Brak użytkownika");
        }
        return new LoginDetails(user);
    }
}
