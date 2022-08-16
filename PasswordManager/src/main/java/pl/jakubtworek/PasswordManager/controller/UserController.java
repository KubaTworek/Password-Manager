package pl.jakubtworek.PasswordManager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
    @GetMapping("/user-passwords")
    public String getUserPasswords(Model theModel) {

        return "user/user-passwords";
    }
    @GetMapping("/user-category")
    public String getUserCategories(Model theModel) {

        return "user/user-category";
    }
}
