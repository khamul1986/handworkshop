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
import pl.khamul.handworkshop.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
    private final OrderHistoryService orderHistoryService;
    private final UserService userService;

    public UserController(UserRepository userRepository, OrderHistoryService orderHistoryService, UserService userService) {
        this.userRepository = userRepository;
        this.orderHistoryService = orderHistoryService;
        this.userService = userService;
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
    public String addNames(UserNames details, HttpServletRequest request){

     userService.setDetails(request, details);



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

    @RequestMapping("/admin/userlist")
    @ResponseBody
    public List<User> userList(){

        return userService.userList();
    }
    @RequestMapping("/admin/orders")
    @ResponseBody
    public List orderList(){
        return orderHistoryService.showAll();
    }
    @RequestMapping("/403")
    public String denied(){
        return "/403";
    }
}
