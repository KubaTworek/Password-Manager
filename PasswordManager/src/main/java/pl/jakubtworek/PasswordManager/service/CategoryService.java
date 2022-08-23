package pl.jakubtworek.PasswordManager.service;

import pl.jakubtworek.PasswordManager.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();

    Category findById(int theId);

    Category findByName(String theName);

    void save(Category theCategory);

    void deleteById(int theId);
}
