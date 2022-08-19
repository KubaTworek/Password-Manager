package pl.jakubtworek.PasswordManager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.jakubtworek.PasswordManager.entity.Password;
import pl.jakubtworek.PasswordManager.service.PasswordService;

import java.util.List;

@Controller
@RequestMapping("/passwords")
public class PasswordController {
    private PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }


}
