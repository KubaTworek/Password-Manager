package pl.jakubtworek.PasswordManager.api;

import org.springframework.web.bind.annotation.*;
import pl.jakubtworek.PasswordManager.entity.Category;
import pl.jakubtworek.PasswordManager.service.CategoryService;

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

    @DeleteMapping("/category/{categoryId}")
    public String deleteCategory(@PathVariable int categoryId) throws Exception {
        if(categoryService.findById(categoryId) == null) throw new Exception("Category id not found - " + categoryId);
        categoryService.deleteById(categoryId);

        return "Deleted category is - " + categoryId;
    }
}
