package pl.jakubtworek.PasswordManager.service;

import pl.jakubtworek.PasswordManager.entity.Category;
import pl.jakubtworek.PasswordManager.entity.Password;
import pl.jakubtworek.PasswordManager.entity.User;

import java.util.List;

public interface PasswordService {
    public List<Password> findAll();

    public List<Password> findByName(String name);

    public List<Password> findByCategory(Category category);

    public Password findById(int theId);

    public List<Password> findAllByUser(String username);

    public void save(Password thePassword);

    public void saveWithCategory(Password thePassword, Category category);

    public void saveWithCategoryAndUser(Password thePassword, Category category, User user);

    public void deleteById(int theId);
}
