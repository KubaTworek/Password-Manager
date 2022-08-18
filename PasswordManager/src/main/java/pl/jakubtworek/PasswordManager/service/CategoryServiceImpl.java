package pl.jakubtworek.PasswordManager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubtworek.PasswordManager.dao.CategoryDAO;
import pl.jakubtworek.PasswordManager.dao.PasswordDAO;
import pl.jakubtworek.PasswordManager.entity.Category;
import pl.jakubtworek.PasswordManager.entity.Password;
import pl.jakubtworek.PasswordManager.entity.User;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    private CategoryDAO categoryDAO;

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
        else {
            throw new RuntimeException("Did not find category id - " + theId);
        }

        return theCategory;
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
