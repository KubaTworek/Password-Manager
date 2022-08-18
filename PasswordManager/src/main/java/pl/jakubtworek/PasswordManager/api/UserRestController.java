package pl.jakubtworek.PasswordManager.api;

import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.PasswordManager.entity.Category;
import pl.jakubtworek.PasswordManager.entity.User;
import pl.jakubtworek.PasswordManager.service.CategoryService;
import pl.jakubtworek.PasswordManager.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getUsers(){
        return userService.findAll();
    }

    @GetMapping("/users/username/{userUsername}")
    public User getUserByUsername(@PathVariable String userUsername) throws Exception {
        if(userService.findByUsername(userUsername) == null) throw new Exception("User not found - " + userUsername);

        return userService.findByUsername(userUsername);
    }

    @PostMapping("/users")
    public User saveUser(@RequestBody User theUser){
        userService.save(theUser);

        return theUser;
    }

//    @PutMapping("/password")
//    public void updatePassword(@RequestBody Password newPassword){
//        if(passwordService.findById(newPassword.getId()) != null){
//            Password password = passwordService.findById(newPassword.getId());
//            password.setName(newPassword.getName());
//            password.setValue(newPassword.getValue());
//            password.setCategory(categoryService.findById(newPassword.getCategory()));
//            password.setUser(userService.findByUsername(newPassword.getUser_username()));
//            passwordService.save(password);
//        } else {
//            passwordService.save(newPassword);
//        }
//    }

    @DeleteMapping("/users/{userUsername}")
    public String deleteUser(@PathVariable String userUsername) throws Exception {
        if(userService.findByUsername(userUsername) == null) throw new Exception("User not found - " + userUsername);
        userService.deleteByUsername(userUsername);

        return "Deleted user is - " + userUsername;
    }
}
