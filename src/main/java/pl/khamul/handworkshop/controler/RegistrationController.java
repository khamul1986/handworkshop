package pl.khamul.handworkshop.controler;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import pl.khamul.handworkshop.exception.UserAlreadyExistsException;
import pl.khamul.handworkshop.service.RegistrationService;
import pl.khamul.handworkshop.transfer.UserDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;



@Controller
public class RegistrationController {


    private final RegistrationService registrationService;


    public RegistrationController(RegistrationService registrationService) {

        this.registrationService = registrationService;
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
             registrationService.registerNewUserAccount(userDto);
        } catch (UserAlreadyExistsException uaeEx) {
            ModelAndView mav = new ModelAndView();
            mav.addObject("message", "An account for that username/email already exists.");
            return mav;
        }

        return new ModelAndView("/confirm", "user", userDto);
    }


}