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
@RequestMapping("/admin")
public class AdminController {
    private PasswordService passwordService;

    public AdminController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @GetMapping("/admin-passwords")
    public String getUserPasswords(Model theModel) {
        List<Password> thePasswords = passwordService.findAll();

        theModel.addAttribute("passwords", thePasswords);

        return "admin/admin-passwords";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {

        // create model attribute to bind form data
        Password thePassword = new Password();

        theModel.addAttribute("password", thePassword);

        return "admin/password-form";
    }

    @PostMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("passwordId") int theId,
                                    Model theModel) {

        // get the employee from the service
        Password thePassword = passwordService.findById(theId);

        // set employee as a model attribute to pre-populate the form
        theModel.addAttribute("password", thePassword);

        // send over to our form
        return "admin/password-form";
    }

    @PostMapping("/save")
    public String savePassword(@ModelAttribute("password") Password thePassword) {

        // save the employee
        passwordService.save(thePassword);

        // use a redirect to prevent duplicate submissions
        return "redirect:/admin/admin-passwords";
    }

    @PostMapping("/delete")
    public String deletePassword(@RequestParam("passwordId") int theId) {
        passwordService.deleteById(theId);
        return "redirect:/admin/admin-passwords";
    }
}
