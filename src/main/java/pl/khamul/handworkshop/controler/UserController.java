package pl.khamul.handworkshop.controler;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.khamul.handworkshop.entity.User;
import pl.khamul.handworkshop.entity.UserNames;
import pl.khamul.handworkshop.repository.UserDetailsRepo;
import pl.khamul.handworkshop.repository.UserRepository;
import pl.khamul.handworkshop.service.OrderHistoryService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
    private final UserDetailsRepo userDetailsRepo;
    private final OrderHistoryService orderHistoryService;

    public UserController(UserRepository userRepository, UserDetailsRepo userDetailsRepo, OrderHistoryService orderHistoryService) {
        this.userRepository = userRepository;
        this.userDetailsRepo = userDetailsRepo;
        this.orderHistoryService = orderHistoryService;
    }

    @GetMapping("")
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
    public String addNames(UserNames details, HttpSession session){

        User user = (User)session.getAttribute("user");
        user.setDetails(details);

        userDetailsRepo.save(details);
        userRepository.save(user);




        return "/confirm";
    }

    @RequestMapping("/admin")
    public String adminPanel(){
        return "/adminpanel";
    }

    @RequestMapping("/history")
    @ResponseBody
    public List showHistory(HttpServletRequest request){



        return orderHistoryService.showHistory(request);
    }
}
