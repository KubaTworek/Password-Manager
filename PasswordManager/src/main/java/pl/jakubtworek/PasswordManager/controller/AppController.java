package pl.jakubtworek.PasswordManager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {
    @GetMapping("/")
    public String getStarted(Model theModel) {

        return "menu";
    }
}
