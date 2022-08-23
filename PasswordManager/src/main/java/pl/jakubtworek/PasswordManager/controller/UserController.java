package pl.jakubtworek.PasswordManager.controller;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.PasswordManager.entity.Category;
import pl.jakubtworek.PasswordManager.entity.Password;
import pl.jakubtworek.PasswordManager.service.CategoryService;
import pl.jakubtworek.PasswordManager.service.PasswordService;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private final PasswordService passwordService;
    private final CategoryService categoryService;

    public UserController(PasswordService passwordService, CategoryService categoryService) {
        this.passwordService = passwordService;
        this.categoryService = categoryService;
    }

    @GetMapping("/user-passwords")
    public String getUserPasswords(Model theModel,@ModelAttribute("keyword") String keyword, @ModelAttribute("category") String category) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        List<Category> theCategories = categoryService.findAll();
        theModel.addAttribute("categories", theCategories);

        List<Password> thePasswords = passwordService.findAllByUser(currentPrincipalName);
        if(!keyword.isEmpty()) {
            thePasswords = passwordService.findByName(keyword);
            theModel.addAttribute("keyword", keyword);
        }
        if(!category.isEmpty()) {
            thePasswords = passwordService.findByCategory(categoryService.findByName(category));
            theModel.addAttribute("category", category);
        }

        theModel.addAttribute("passwords", thePasswords);

        return "user/user-passwords";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {

        Password thePassword = new Password();

        List<Category> theCategories = categoryService.findAll();

        theModel.addAttribute("password", thePassword);
        theModel.addAttribute("categories", theCategories);

        return "user/password-form";
    }

    @PostMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("passwordId") int theId,
                                    Model theModel) {

        Password thePassword = passwordService.findById(theId);
        String decodedString = new String(Base64.decodeBase64(thePassword.getValue().getBytes()));
        thePassword.setValue(decodedString);

        theModel.addAttribute("password", thePassword);
        List<Category> theCategories = categoryService.findAll();
        theModel.addAttribute("categories", theCategories);

        return "user/password-form";
    }


    @PostMapping("/save")
    public String savePassword(@ModelAttribute("password") Password thePassword,@RequestParam("category.name") String categoryName) {

        passwordService.saveWithCategory(thePassword, categoryService.findByName(categoryName));

        return "redirect:/user/user-passwords";
    }

    @PostMapping("/delete")
    public String deletePassword(@RequestParam("passwordId") int theId) {
        passwordService.deleteById(theId);
        return "redirect:/user/user-passwords";
    }
}
