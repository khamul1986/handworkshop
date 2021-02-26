package pl.khamul.handworkshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.khamul.handworkshop.entity.LoginDetails;
import pl.khamul.handworkshop.entity.Privilege;
import pl.khamul.handworkshop.entity.Role;
import pl.khamul.handworkshop.entity.User;
import pl.khamul.handworkshop.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class LoginDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findFirstByEmail(username);
        LoginDetails userDetails = new LoginDetails(user);
        if (user == null) {
            throw new UsernameNotFoundException("Brak użytkownika");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), userDetails.isEnabled(),
                true, true, true, getAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(final Collection<Role> roles) {
        final List<String> privileges = new ArrayList<>();
        final List<Privilege> collection = new ArrayList<>();
        for (final Role role : roles) {
            collection.addAll(role.getPrivileges());
        }
        for (final Privilege item : collection) {
            privileges.add(item.getName());
        }

        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(final List<String> privileges) {
        final List<GrantedAuthority> authorities = new ArrayList<>();
        for (final String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}




