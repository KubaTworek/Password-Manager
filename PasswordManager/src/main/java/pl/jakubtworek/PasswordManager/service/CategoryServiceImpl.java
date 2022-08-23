package pl.jakubtworek.PasswordManager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubtworek.PasswordManager.dao.CategoryDAO;
import pl.jakubtworek.PasswordManager.entity.Category;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryDAO categoryDAO;

    @Autowired
    public CategoryServiceImpl(CategoryDAO theCategoryDAO) {
        categoryDAO = theCategoryDAO;
    }

    @Override
    public List<Category> findAll() {
        return categoryDAO.findAll();
    }

    @Override
    public Category findById(int theId) {

        Optional<Category> result = categoryDAO.findById(theId);

        Category theCategory = null;

        if (result.isPresent()) {
            theCategory = result.get();
        }

        return theCategory;
    }

    @Override
    public Category findByName(String theName){
        List<Category> categories = categoryDAO.findAll();
        for(Category category  : categories){
            if(Objects.equals(category.getName(), theName)) return category;
        }
        throw new RuntimeException("Did not find password name - " + theName);
    }

    @Override
    public void save(Category theCategory) {
        categoryDAO.save(theCategory);
    }

    @Override
    public void deleteById(int theId) {
        categoryDAO.deleteById(theId);
    }
}
