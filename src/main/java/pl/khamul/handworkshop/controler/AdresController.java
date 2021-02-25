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
import pl.khamul.handworkshop.service.AdresService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class AdresController {


    private AdresService adresService;


    public AdresController( AdresService adresService) {

        this.adresService = adresService;
    }

    @GetMapping("/adress")
    public String adres(){

        return "/addAdres";
    }

    @PostMapping("/adress")
    public String addAdres(Adress adress, HttpServletRequest request){

       adresService.addAdress(adress,request);



        return "/confirm";
    }

    @RequestMapping("/viewadres")
    @ResponseBody
    public List adresList(Model model, HttpServletRequest request){


 /*       model.addAttribute("adresList", adresList);*/

        return adresService.adressList(request) ;
    }

}
