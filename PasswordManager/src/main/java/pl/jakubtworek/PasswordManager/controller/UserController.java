package pl.jakubtworek.PasswordManager.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {

        // create model attribute to bind form data
        Password thePassword = new Password();

        theModel.addAttribute("password", thePassword);

        return "user/password-form";
    }

    @PostMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("passwordId") int theId,
                                    Model theModel) {

        // get the employee from the service
        Password thePassword = passwordService.findById(theId);

        // set employee as a model attribute to pre-populate the form
        theModel.addAttribute("password", thePassword);

        // send over to our form
        return "user/password-form";
    }

    @PostMapping("/save")
    public String savePassword(@ModelAttribute("password") Password thePassword) {

        // save the employee
        passwordService.saveWithCategoryAndUser(thePassword, thePassword.getCategory());

        // use a redirect to prevent duplicate submissions
        return "redirect:/user/user-passwords";
    }

    @PostMapping("/delete")
    public String deletePassword(@RequestParam("passwordId") int theId) {
        passwordService.deleteById(theId);
        return "redirect:/user/user-passwords";
    }

}
