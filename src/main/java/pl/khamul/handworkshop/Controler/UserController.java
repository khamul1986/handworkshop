package pl.khamul.handworkshop.Controler;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.khamul.handworkshop.entity.User;
import pl.khamul.handworkshop.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/register")
    public String register(){



        return "/register";
    }

    @PostMapping("/register")
    public String registered(@ModelAttribute User user){

        userRepository.save(user);

        return "/confirm";
    }

    @GetMapping("/login")
    public String login(){
        return "/login";
    }
    @PostMapping("/login")
    public String logged(User user, HttpServletRequest request){
       User userList = userRepository.findFirstByEmail(user.getEmail());
       if(userList.getPassword().equals(user.getPassword())){
           HttpSession session = request.getSession(false);
           User loggedUser = userRepository.findFirstByEmail(user.getEmail());
           if(session == null){
               session = request.getSession();
           }

           session.setAttribute("user", loggedUser);
           System.out.println(session.getAttribute("user"));

           return "/confirm";
       }


        return "/index";
    }
}
