package pl.jakubtworek.PasswordManager.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
