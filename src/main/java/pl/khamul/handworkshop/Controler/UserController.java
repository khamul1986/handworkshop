package pl.khamul.handworkshop.Controler;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import pl.khamul.handworkshop.Exception.UserAlreadyExistsException;
import pl.khamul.handworkshop.Service.UserService;
import pl.khamul.handworkshop.entity.User;
import pl.khamul.handworkshop.entity.UserNames;
import pl.khamul.handworkshop.repository.UserDetailsRepo;
import pl.khamul.handworkshop.repository.UserRepository;
import pl.khamul.handworkshop.transfer.UserDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;



@Controller
public class UserController {

    private final UserRepository userRepository;
    private final UserDetailsRepo userDetailsRepo;
    private final UserService userService;


    public UserController(UserRepository userRepository, UserDetailsRepo userDetailsRepo, UserService userService) {
        this.userRepository = userRepository;
        this.userDetailsRepo = userDetailsRepo;
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(WebRequest request, Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "/register";
    }

    @PostMapping("/register")
    public ModelAndView registerUserAccount(
            @ModelAttribute("user") @Valid UserDto userDto,
            HttpServletRequest request, Errors errors) {

        try {
            User registered = userService.registerNewUserAccount(userDto);
        } catch (UserAlreadyExistsException uaeEx) {
            ModelAndView mav = new ModelAndView();
            mav.addObject("message", "An account for that username/email already exists.");
            return mav;
        }

        return new ModelAndView("/confirm", "user", userDto);
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

    @PostMapping("/login") // Logowanie na header z dwoma wariantami widoku ??
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
    @GetMapping("/detail") // dołożyć filtrowanie
    public String adres(){

        return "/details";
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