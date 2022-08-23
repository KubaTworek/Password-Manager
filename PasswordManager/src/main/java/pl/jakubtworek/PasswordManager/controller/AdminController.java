package pl.jakubtworek.PasswordManager.controller;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.PasswordManager.entity.Category;
import pl.jakubtworek.PasswordManager.entity.Password;
import pl.jakubtworek.PasswordManager.entity.User;
import pl.jakubtworek.PasswordManager.service.CategoryService;
import pl.jakubtworek.PasswordManager.service.PasswordService;
import pl.jakubtworek.PasswordManager.service.UserService;

import java.security.SecureRandom;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final PasswordService passwordService;
    private final CategoryService categoryService;
    private final UserService userService;

    public AdminController(PasswordService passwordService, CategoryService categoryService, UserService userService) {
        this.passwordService = passwordService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping("/admin-passwords")
    public String getPasswords(Model theModel,@ModelAttribute("keyword") String keyword, @ModelAttribute("category") String category,@ModelAttribute("user") String user) {
        List<Password> thePasswords = passwordService.findAll();

        List<Category> theCategories = categoryService.findAll();
        theModel.addAttribute("categories", theCategories);
        List<User> theUsers = userService.findAll();
        theModel.addAttribute("users", theUsers);

        if(!user.isEmpty()) {
            thePasswords = passwordService.findAllByUser(user);
            theModel.addAttribute("user", user);
        }
        if(!keyword.isEmpty()) {
            thePasswords = passwordService.findByName(keyword);
            theModel.addAttribute("keyword", keyword);
        }
        if(!category.isEmpty()) {
            thePasswords = passwordService.findByCategory(categoryService.findByName(category));
            theModel.addAttribute("category", category);
        }

        for(Password password : thePasswords){
            String decodedString = new String(Base64.decodeBase64(password.getValue().getBytes()));
            password.setValue(decodedString);
        }

        theModel.addAttribute("passwords", thePasswords);

        return "admin/admin-passwords";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {

        Password thePassword = new Password();


        List<User> theUsers = userService.findAll();
        theModel.addAttribute("users", theUsers);
        List<Category> theCategories = categoryService.findAll();
        theModel.addAttribute("categories", theCategories);

        theModel.addAttribute("password", thePassword);

        return "admin/password-form";
    }

    @PostMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("passwordId") int theId,
                                    Model theModel) {

        Password thePassword = passwordService.findById(theId);
        String decodedString = new String(Base64.decodeBase64(thePassword.getValue().getBytes()));
        thePassword.setValue(decodedString);


        List<Category> theCategories = categoryService.findAll();
        theModel.addAttribute("categories", theCategories);
        List<User> theUsers = userService.findAll();
        theModel.addAttribute("users", theUsers);

        theModel.addAttribute("password", thePassword);

        return "admin/password-form";
    }

    @GetMapping("/showFormForAddCategory")
    public String showFormForAddCategory(Model theModel) {

        Category theCategory = new Category();
        theModel.addAttribute("category", theCategory);

        return "admin/category-form";
    }

    @PostMapping("/save")
    public String savePassword(@ModelAttribute("password") Password thePassword,@RequestParam("category.name") String categoryName,@RequestParam("user.username") String userUsername) {

        passwordService.saveWithCategoryAndUser(thePassword, categoryService.findByName(categoryName), userService.findByUsername(userUsername));

        return "redirect:/admin/admin-passwords";
    }

    @PostMapping("/saveCategory")
    public String savePassword(@ModelAttribute("category") Category theCategory) {

        categoryService.save(theCategory);

        return "redirect:/admin/admin-passwords";
    }


    @PostMapping("/register")
    public String showRegisterForm(@ModelAttribute("user") User theUser, String authority) {
        int strength = 10;
        BCryptPasswordEncoder bCryptPasswordEncoder =
                new BCryptPasswordEncoder(strength, new SecureRandom());
        theUser.setPassword("{bcrypt}"+bCryptPasswordEncoder.encode(theUser.getPassword()));

        StringBuilder newUsername = new StringBuilder(theUser.getUsername());
        theUser.setUsername(newUsername.deleteCharAt(0).toString());
        theUser.setEnabled(1);

        userService.save(theUser);
        userService.insertAuthority(theUser.getUsername(),authority);

        return "redirect:/admin/admin-passwords";
    }

    @GetMapping("/registerUser")
    public String registerUser(Model model) {

        User user = new User();

        model.addAttribute("user", user);

        return "admin/register-user";
    }

    @PostMapping("/delete")
    public String deletePassword(@RequestParam("passwordId") int theId) {
        passwordService.deleteById(theId);
        return "redirect:/admin/admin-passwords";
    }
}
