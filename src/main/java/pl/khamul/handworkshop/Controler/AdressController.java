package pl.khamul.handworkshop.Controler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.khamul.handworkshop.entity.Adres;
import pl.khamul.handworkshop.entity.User;
import pl.khamul.handworkshop.repository.AdresRepository;
import pl.khamul.handworkshop.repository.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller("/user")
public class AdressController {

    private final AdresRepository adresRepository;
    private final UserRepository userRepository;


    public AdressController(AdresRepository adresRepository, UserRepository userRepository) {
        this.adresRepository = adresRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/adres")
    public String adres(){

        return "/addAdres";
    }

    @PostMapping("/adres")
    public String addAdres(Adres adres, HttpSession session){

        User user = (User)session.getAttribute("user");

        List list = user.getAdres();
        list.add(adres);
        user.setAdres(list);
        adresRepository.save(adres);
        userRepository.save(user);
        System.out.println(adres);



        return "/confirm";
    }

    @RequestMapping("/viewadres")
    @ResponseBody //widok nie JSON ;)
    public List adresList(HttpSession session){
        User user = (User)session.getAttribute("user");

        List adressList = adresRepository.findAllByUserId(user.getId());

        return adressList;
    }

}
