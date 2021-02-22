package pl.khamul.handworkshop.controler;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.khamul.handworkshop.entity.Adress;
import pl.khamul.handworkshop.entity.User;
import pl.khamul.handworkshop.repository.AdresRepository;
import pl.khamul.handworkshop.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class AdresController {

    private final AdresRepository adresRepository;
    private final UserRepository userRepository;


    public AdresController(AdresRepository adresRepository, UserRepository userRepository) {
        this.adresRepository = adresRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/adres")
    public String adres(){

        return "/addAdres";
    }

    @PostMapping("/adres")
    public String addAdres(Adress adress, HttpServletRequest request, Model model){

        Principal principal = request.getUserPrincipal();
        User user = userRepository.findFirstByEmail(principal.getName());

        List list = user.getAdres();
        list.add(adress);
        user.setAdres(list);
        adresRepository.save(adress);
        userRepository.save(user);




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
