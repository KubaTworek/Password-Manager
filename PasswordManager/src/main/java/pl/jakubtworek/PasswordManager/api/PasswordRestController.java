package pl.jakubtworek.PasswordManager.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.PasswordManager.entity.Password;
import pl.jakubtworek.PasswordManager.service.CategoryService;
import pl.jakubtworek.PasswordManager.service.PasswordService;
import pl.jakubtworek.PasswordManager.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PasswordRestController {

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @GetMapping("/passwords")
    public List<Password> getPasswords(){
        return passwordService.findAll();
    }

    @GetMapping("/passwords/id/{passwordId}")
    public Password getPasswordsById(@PathVariable int passwordId) throws Exception {
        if(passwordService.findById(passwordId) == null) throw new Exception("Password id not found - " + passwordId);

        return passwordService.findById(passwordId);
    }

    @GetMapping("/passwords/name/{passwordName}")
    public Password getPasswordsById(@PathVariable String passwordName) throws Exception {
        if(passwordService.findByName(passwordName) == null) throw new Exception("Password name not found - " + passwordName);

        return passwordService.findByName(passwordName);
    }

    @GetMapping("/passwords/category/{categoryId}")
    public List<Password> getPasswordsByCategory(@PathVariable int categoryId) throws Exception {
        if(passwordService.findByCategory(categoryService.findById(categoryId)) == null) throw new Exception("Passwords with this category not found");

        return passwordService.findByCategory(categoryService.findById(categoryId));
    }

    @GetMapping("/passwords/user/{username}")
    public List<Password> getPasswordsByUser(@PathVariable String username) throws Exception {
        if(passwordService.findAllByUser(userService.findByUsername(username)).isEmpty()) throw new Exception("Passwords for that user not found");

        return passwordService.findAllByUser(userService.findByUsername(username));
    }

    @GetMapping("/password/user/{username}/{passwordName}")
    public Password getPasswordsByUserAndName(@PathVariable String username, @PathVariable String passwordName) throws Exception {
        if(passwordService.findAllByUser(userService.findByUsername(username)).isEmpty()) throw new Exception("Passwords for that user not found");

        return passwordService.findByNameAndUser(passwordName,userService.findByUsername(username));
    }

    @GetMapping("/passwords/user/{username}/{categoryId}")
    public List<Password> getPasswordsByUserAndCategory(@PathVariable String username, @PathVariable int categoryId) throws Exception {
        if(passwordService.findAllByUser(userService.findByUsername(username)).isEmpty()) throw new Exception("Passwords for that user not found");

        return passwordService.findByCategoryAndUser(categoryService.findById(categoryId),userService.findByUsername(username));
    }

    @PostMapping("/password")
    public Password savePassword(@RequestBody Password thePassword){
        thePassword.setId(0);
        passwordService.save(thePassword);

        return thePassword;
    }

    @PutMapping("/password")
    public Password updatePassword(@RequestBody Password thePassword){
        passwordService.save(thePassword);

        return thePassword;
    }

    @DeleteMapping("/password/{passwordId}")
    public String deletePassword(@PathVariable int passwordId) throws Exception {
        if(passwordService.findById(passwordId) == null) throw new Exception("Password id not found - " + passwordId);

        return "Deleted password is - " + passwordId;
    }

}
