package pl.jakubtworek.PasswordManager.service;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.PasswordManager.dao.PasswordDAO;
import pl.jakubtworek.PasswordManager.entity.Category;
import pl.jakubtworek.PasswordManager.entity.Password;
import pl.jakubtworek.PasswordManager.entity.User;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PasswordServiceImpl implements PasswordService{

    private PasswordDAO passwordDAO;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    public PasswordServiceImpl(PasswordDAO thePasswordDAO) {
        passwordDAO = thePasswordDAO;
    }

    @Override
    public List<Password> findAll() {
        return passwordDAO.findAll();
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
    public void save(Password thePassword) {
        String encodedString = new String(Base64.encodeBase64(thePassword.getValue().getBytes()));
        thePassword.setValue(encodedString);

        passwordDAO.save(thePassword);
    }

    @Override
    public void deleteById(int theId) {
        passwordDAO.deleteById(theId);
    }

    @Override
    @Transactional
    public Password findByName(String name) {
        List<Password> passwords = passwordDAO.findAll();
        for(Password password  : passwords){
            if(Objects.equals(password.getName(), name)) return password;
        }
        throw new RuntimeException("Did not find password name - " + name);
    }

    @Override
    @Transactional
    public List<Password> findByCategory(Category category) {
        List<Password> passwords = passwordDAO.findAll();
        passwords.removeIf(password -> !Objects.equals(password.getCategory().getId(), category.getId()));
        if(passwords.isEmpty()) throw new RuntimeException("Did not find passwords with category - " + category.getName());
        return passwords;
    }

    @Override
    @Transactional
    public List<Password> findAllByUser(String username) {
        List<Password> passwords = passwordDAO.findAll();
        passwords.removeIf(password -> !Objects.equals(password.getUser().getUsername(), username));
        return passwords;
    }

    @Override
    @Transactional
    public Password findByNameAndUser(String name, String username) {
        List<Password> passwords = findAllByUser(username);
        Password password = findByName(name);
        if(passwords.contains(password)) return password;
        throw new RuntimeException("Did not find password name - " + name);

    }

    @Override
    @Transactional
    public List<Password> findByCategoryAndUser(Category category, String username) {
        List<Password> passwordsByUser = findAllByUser(username);
        List<Password> passwordsByCategory = findByCategory(category);
        List<Password> passwords = new ArrayList<>();
        for(Password password : passwordsByUser){
            if(passwordsByCategory.contains(password)) passwords.add(password);
        }
        if(passwords.isEmpty()) throw new RuntimeException("Did not find passwords in this category - " + category.getName());
        return passwords;
    }


    @Override
    @Transactional
    public void saveWithCategoryAndUser(Password thePassword, Category category) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        thePassword.setUser(userService.findByUsername(currentPrincipalName));
        thePassword.setCategory(category);
        save(thePassword);
    }
}
