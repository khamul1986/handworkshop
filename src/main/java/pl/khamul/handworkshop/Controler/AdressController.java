package pl.khamul.handworkshop.Controler;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.khamul.handworkshop.entity.Adres;
import pl.khamul.handworkshop.entity.User;
import pl.khamul.handworkshop.repository.AdresRepository;
import pl.khamul.handworkshop.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
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
    public String addAdres(Adres adres, HttpServletRequest request, Model model){

        Principal principal = request.getUserPrincipal();
        User user = userRepository.findFirstByEmail(principal.getName());
        model.addAttribute("user", user);

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
    public List adresList(Model model, HttpServletRequest request){

        Principal principal = request.getUserPrincipal();
        User user = userRepository.findFirstByEmail(principal.getName());



        List adresList = adresRepository.findAllByUserId(user.getId());

        model.addAttribute("adresList", adresList);

        return adresList;
    }

}
