package pl.jakubtworek.PasswordManager.service;

import pl.jakubtworek.PasswordManager.entity.Category;
import pl.jakubtworek.PasswordManager.entity.Password;
import pl.jakubtworek.PasswordManager.entity.User;

import java.util.List;

public interface PasswordService {
    public List<Password> findAll();

    public Password findByName(String name);

    public List<Password> findByCategory(Category category);

    public Password findById(int theId);

    public List<Password> findAllByUser(User username);

    public Password findByNameAndUser(String name, User username);

    public List<Password> findByCategoryAndUser(Category category, User username);

    public void save(Password thePassword);

    public void deleteById(int theId);
}
