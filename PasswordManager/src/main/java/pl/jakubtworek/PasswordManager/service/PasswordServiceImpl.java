package pl.jakubtworek.PasswordManager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubtworek.PasswordManager.dao.PasswordDAO;
import pl.jakubtworek.PasswordManager.entity.Category;
import pl.jakubtworek.PasswordManager.entity.Password;
import pl.jakubtworek.PasswordManager.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PasswordServiceImpl implements PasswordService{

    private PasswordDAO passwordDAO;

    @Autowired
    public PasswordServiceImpl(PasswordDAO thePasswordDAO) {
        passwordDAO = thePasswordDAO;
    }


    @Override
    public List<Password> findAll() {
        return passwordDAO.findAll();
    }

    @Override
    public Password findByName(String name) {
        List<Password> passwords = passwordDAO.findAll();
        for(Password password  : passwords){
            if(Objects.equals(password.getName(), name)) return password;
        }
        throw new RuntimeException("Did not find password name - " + name);
    }

    @Override
    public List<Password> findByCategory(Category category) {
        List<Password> passwords = passwordDAO.findAll();
        passwords.removeIf(password -> !Objects.equals(password.getCategory(), category.getId()));
        if(passwords.isEmpty()) throw new RuntimeException("Did not find passwords with category - " + category.getName());
        return passwords;
    }

    @Override
    public Password findById(int theId) {
        Optional<Password> result = passwordDAO.findById(theId);

        Password thePassword = null;

        if (result.isPresent()) {
            thePassword = result.get();
        }
        else {
            throw new RuntimeException("Did not find password id - " + theId);
        }

        return thePassword;
    }

    @Override
    public List<Password> findAllByUser(User user) {
        List<Password> passwords = passwordDAO.findAll();
        passwords.removeIf(password -> !Objects.equals(password.getUser_username(), user.getUsername()));
        if(passwords.isEmpty()) throw new RuntimeException("Did not find passwords for user - " + user.getUsername());
        return passwords;
    }

    @Override
    public Password findByNameAndUser(String name, User user) {
        List<Password> passwords = findAllByUser(user);
        Password password = findByName(name);
        if(passwords.contains(password)) return password;
        throw new RuntimeException("Did not find password name - " + name);

    }

    @Override
    public List<Password> findByCategoryAndUser(Category category, User user) {
        List<Password> passwordsByUser = findAllByUser(user);
        List<Password> passwordsByCategory = findByCategory(category);
        List<Password> passwords = new ArrayList<>();
        for(Password password : passwordsByUser){
            if(passwordsByCategory.contains(password)) passwords.add(password);
        }
        if(passwords.isEmpty()) throw new RuntimeException("Did not find passwords in this category - " + category.getName());
        return passwords;
    }

    @Override
    public void save(Password thePassword) {
        passwordDAO.save(thePassword);
    }

    @Override
    public void deleteById(int theId) {
        passwordDAO.deleteById(theId);
    }
}
