package pl.jakubtworek.PasswordManager.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.jakubtworek.PasswordManager.entity.Password;
import pl.jakubtworek.PasswordManager.service.PasswordService;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private PasswordService passwordService;

    public UserController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @GetMapping("/user-passwords")
    public String getUserPasswords(Model theModel) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        List<Password> thePasswords = passwordService.findAllByUser(currentPrincipalName);

        theModel.addAttribute("passwords", thePasswords);

        return "user/user-passwords";
    }

}
