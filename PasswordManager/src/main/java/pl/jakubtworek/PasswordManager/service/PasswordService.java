package pl.jakubtworek.PasswordManager.service;

import pl.jakubtworek.PasswordManager.entity.Category;
import pl.jakubtworek.PasswordManager.entity.Password;
import pl.jakubtworek.PasswordManager.entity.User;

import java.util.List;

public interface PasswordService {
    List<Password> findAll();

    List<Password> findByName(String name);

    List<Password> findByCategory(Category category);

    Password findById(int theId);

    List<Password> findAllByUser(String username);

    void save(Password thePassword);

    void saveWithCategory(Password thePassword, Category category);

    void saveWithCategoryAndUser(Password thePassword, Category category, User user);

    void deleteById(int theId);
}
