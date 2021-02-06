package pl.khamul.handworkshop.Controler;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.khamul.handworkshop.entity.Adres;
import pl.khamul.handworkshop.entity.User;
import pl.khamul.handworkshop.entity.UserDetails;
import pl.khamul.handworkshop.repository.UserDetailsRepo;
import pl.khamul.handworkshop.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class UserController {

    private final UserRepository userRepository;
    private final UserDetailsRepo userDetailsRepo;


    public UserController(UserRepository userRepository, UserDetailsRepo userDetailsRepo) {
        this.userRepository = userRepository;
        this.userDetailsRepo = userDetailsRepo;
    }

    @GetMapping("/register")
    public String register(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session == null) {
            session = request.getSession();
        }
        if(session.getAttribute("user")!=null) {
            return "/userpanel";
        }

        return "/register";
    }

    @PostMapping("/register")
    public String registered(@ModelAttribute User user) {
        List<String> list= userRepository.emailList();

        for (String s : list){
            if(s.matches(user.getEmail())){
                return "/index";
            }

        }

        userRepository.save(user);

        return "/confirm";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            session = request.getSession();
        }
        if(session.getAttribute("user")!=null){
            return "/userpanel";
        }

        return "/login";
    }

    @PostMapping("/login")
    public String logged(User user, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            session = request.getSession();
        }
        if(session.getAttribute("user")!=null){
            return "/userpanel";
        }
        User userList = userRepository.findFirstByEmail(user.getEmail());
        if (userList.getPassword().equals(user.getPassword())) {
            User loggedUser = userRepository.findFirstByEmail(user.getEmail());
            session.setAttribute("user", loggedUser);
            System.out.println(session.getAttribute("user"));
            return "/userpanel";
        }
        return "/index";
    }
    @GetMapping("/detail")
    public String adres(){

        return "/adddetails";
    }

    @PostMapping("/detail")
    public String addAdres(UserDetails details, HttpSession session){

        User user = (User)session.getAttribute("user");
        user.setDetails(details);

        userDetailsRepo.save(details);
        userRepository.save(user);




        return "/confirm";
    }
}