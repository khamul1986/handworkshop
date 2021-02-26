package pl.khamul.handworkshop.service;

import org.springframework.stereotype.Service;
import pl.khamul.handworkshop.entity.Adress;
import pl.khamul.handworkshop.entity.UnregisteredOrder;
import pl.khamul.handworkshop.entity.User;
import pl.khamul.handworkshop.repository.AdresRepository;
import pl.khamul.handworkshop.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Service
public class AdresService implements AdresServiceInterface {

    private final UserRepository userRepository;
    private final AdresRepository adresRepository;
    private final UserService userService;

    public AdresService(UserRepository userRepository, AdresRepository adresRepository, UserService userService) {
        this.userRepository = userRepository;
        this.adresRepository = adresRepository;
        this.userService = userService;
    }

    @Override
    public List adressList(HttpServletRequest request) {


        User user = userService.getUser(request);

        List adresList = adresRepository.findAllByUserId(user.getId());

        return adresList;
    }

    @Override
    public void addAdress(Adress adress, HttpServletRequest request) {


        User user = userService.getUser(request);

        List list = user.getAdres();
        list.add(adress);
        user.setAdres(list);
        adresRepository.save(adress);
        userRepository.save(user);


    }

    public Adress createUnregisteredAdress(UnregisteredOrder unregisteredOrder){
        Adress adress = new Adress();
        adress.setCity(unregisteredOrder.getCity());
        adress.setStreet(unregisteredOrder.getStreet());
        adress.setNumber(unregisteredOrder.getNumber());

        adresRepository.save(adress);
        return adress;
    }
}
