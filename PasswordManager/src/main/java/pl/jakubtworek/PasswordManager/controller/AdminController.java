package pl.jakubtworek.PasswordManager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("/admin-passwords")
    public String getAdminPasswords(Model theModel) {

        return "admin/admin-passwords";
    }
    @GetMapping("/admin-category")
    public String getAdminCategories(Model theModel) {

        return "admin/admin-category";
    }
}
