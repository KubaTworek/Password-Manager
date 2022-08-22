package pl.jakubtworek.PasswordManager.controller;

import org.apache.tomcat.util.codec.binary.Base64;
import org.aspectj.lang.annotation.Before;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.PasswordManager.entity.Category;
import pl.jakubtworek.PasswordManager.entity.Password;
import pl.jakubtworek.PasswordManager.service.CategoryService;
import pl.jakubtworek.PasswordManager.service.PasswordService;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private PasswordService passwordService;
    private CategoryService categoryService;

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

        // create model attribute to bind form data
        Password thePassword = new Password();

        List<Category> theCategories = categoryService.findAll();

        theModel.addAttribute("password", thePassword);
        theModel.addAttribute("categories", theCategories);

        return "user/password-form";
    }

    @PostMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("passwordId") int theId,
                                    Model theModel) {

        // get the employee from the service
        Password thePassword = passwordService.findById(theId);
        String decodedString = new String(Base64.decodeBase64(thePassword.getValue().getBytes()));
        thePassword.setValue(decodedString);


        // set employee as a model attribute to pre-populate the form
        theModel.addAttribute("password", thePassword);
        List<Category> theCategories = categoryService.findAll();
        theModel.addAttribute("categories", theCategories);
        
        return "user/password-form";
    }


    @PostMapping("/save")
    public String savePassword(@ModelAttribute("password") Password thePassword,@RequestParam("category.name") String categoryName) {

        passwordService.saveWithCategoryAndUser(thePassword, categoryService.findByName(categoryName));

        return "redirect:/user/user-passwords";
    }

    @PostMapping("/delete")
    public String deletePassword(@RequestParam("passwordId") int theId) {
        passwordService.deleteById(theId);
        return "redirect:/user/user-passwords";
    }
}
