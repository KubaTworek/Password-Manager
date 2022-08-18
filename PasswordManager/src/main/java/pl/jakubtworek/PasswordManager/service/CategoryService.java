package pl.jakubtworek.PasswordManager.service;

import pl.jakubtworek.PasswordManager.entity.Category;

import java.util.List;

public interface CategoryService {
    public List<Category> findAll();

    public List<Category> findByUser();

    public Category findById(int theId);

    public void save(Category theCategory);

    public void deleteById(int theId);
}
