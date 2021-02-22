package pl.khamul.handworkshop.controler;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.khamul.handworkshop.entity.User;
import pl.khamul.handworkshop.entity.UserNames;
import pl.khamul.handworkshop.repository.UserDetailsRepo;
import pl.khamul.handworkshop.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;


@Controller
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
    private final UserDetailsRepo userDetailsRepo;

    public UserController(UserRepository userRepository, UserDetailsRepo userDetailsRepo) {
        this.userRepository = userRepository;
        this.userDetailsRepo = userDetailsRepo;
    }


    @GetMapping("/")
    public String welcome(Model model, HttpServletRequest request){
        Principal principal = request.getUserPrincipal();
        User user = userRepository.findFirstByEmail(principal.getName());
        model.addAttribute("user", user);


        return "/userpanel";
    }

    @GetMapping("/detail") // dołożyć filtrowanie
    public String adres(){

        return "userdetails";
    }

    @PostMapping("/detail")
    public String addAdres(UserNames details, HttpSession session){

        User user = (User)session.getAttribute("user");
        user.setDetails(details);

        userDetailsRepo.save(details);
        userRepository.save(user);




        return "/confirm";
    }
}
