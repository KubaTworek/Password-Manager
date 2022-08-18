package pl.jakubtworek.PasswordManager.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.PasswordManager.entity.Category;
import pl.jakubtworek.PasswordManager.entity.Password;
import pl.jakubtworek.PasswordManager.service.CategoryService;
import pl.jakubtworek.PasswordManager.service.PasswordService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryRestController {
    private final CategoryService categoryService;

    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/category")
    public List<Category> getCategories(){
        return categoryService.findAll();
    }

    @GetMapping("/category/id/{categoryId}")
    public Category getCategoryById(@PathVariable int categoryId) throws Exception {
        if(categoryService.findById(categoryId) == null) throw new Exception("Category id not found - " + categoryId);

        return categoryService.findById(categoryId);
    }

    @PostMapping("/category")
    public Category saveCategory(@RequestBody Category theCategory){
        theCategory.setId(0);
        categoryService.save(theCategory);

        return theCategory;
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

    @DeleteMapping("/category/{categoryId}")
    public String deleteCategory(@PathVariable int categoryId) throws Exception {
        if(categoryService.findById(categoryId) == null) throw new Exception("Category id not found - " + categoryId);
        categoryService.deleteById(categoryId);

        return "Deleted category is - " + categoryId;
    }
}
