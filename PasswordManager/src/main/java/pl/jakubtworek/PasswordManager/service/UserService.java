package pl.jakubtworek.PasswordManager.service;

import pl.jakubtworek.PasswordManager.entity.Category;
import pl.jakubtworek.PasswordManager.entity.User;

import java.util.List;

public interface UserService {
    public List<User> findAll();

    public User findByUsername(String theUsername);

    public void save(User theUser);

    public void deleteByUsername(String theUsername);

    public void insertAuthority(String username, String authority);
}
