package pl.jakubtworek.PasswordManager.service;

import pl.jakubtworek.PasswordManager.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User findByUsername(String theUsername);

    void save(User theUser);

    void deleteByUsername(String theUsername);

    void insertAuthority(String username, String authority);
}
