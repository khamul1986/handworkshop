package pl.khamul.handworkshop.service;

import org.springframework.stereotype.Service;
import pl.khamul.handworkshop.entity.Adress;
import pl.khamul.handworkshop.entity.User;
import pl.khamul.handworkshop.repository.AdresRepository;
import pl.khamul.handworkshop.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Service
public class AdresService implements AdresServiceInterface {

    private UserRepository userRepository;
    private AdresRepository adresRepository;

    public AdresService(UserRepository userRepository, AdresRepository adresRepository) {
        this.userRepository = userRepository;
        this.adresRepository = adresRepository;
    }

    @Override
    public List adressList(HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();
        User user = userRepository.findFirstByEmail(principal.getName());

        List adresList = adresRepository.findAllByUserId(user.getId());

        return adresList;
    }

    @Override
    public void addAdress(Adress adress, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();
        User user = userRepository.findFirstByEmail(principal.getName());

        List list = user.getAdres();
        list.add(adress);
        user.setAdres(list);
        adresRepository.save(adress);
        userRepository.save(user);


    }
}
