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
import pl.khamul.handworkshop.Service.LoginService;
import pl.khamul.handworkshop.repository.UserRepository;
import pl.khamul.handworkshop.transfer.UserDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;



@Controller
public class LoginController {

    private final UserRepository userRepository;
    private final LoginService loginService;


    public LoginController(UserRepository userRepository, LoginService loginService) {
        this.userRepository = userRepository;
        this.loginService = loginService;
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
             loginService.registerNewUserAccount(userDto);
        } catch (UserAlreadyExistsException uaeEx) {
            ModelAndView mav = new ModelAndView();
            mav.addObject("message", "An account for that username/email already exists.");
            return mav;
        }

        return new ModelAndView("/confirm", "user", userDto);
    }


}