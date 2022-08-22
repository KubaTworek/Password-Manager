package pl.jakubtworek.PasswordManager.service;

import pl.jakubtworek.PasswordManager.entity.Category;
import pl.jakubtworek.PasswordManager.entity.User;

import java.util.List;

public interface CategoryService {
    public List<Category> findAll();

    public Category findById(int theId);

    public Category findByName(String theName);

    public void save(Category theCategory);

    public void deleteById(int theId);
}
