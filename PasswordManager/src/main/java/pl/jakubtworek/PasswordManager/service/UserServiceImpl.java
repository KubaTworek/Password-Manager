package pl.jakubtworek.PasswordManager.service;

import org.springframework.stereotype.Service;
import pl.jakubtworek.PasswordManager.dao.UserDAO;
import pl.jakubtworek.PasswordManager.entity.Password;
import pl.jakubtworek.PasswordManager.entity.User;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService{

    private UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User findByUsername(String theUsername) {
        List<User> users = userDAO.findAll();
        for(User user  : users){
            if(Objects.equals(user.getUsername(), theUsername)) return user;
        }
        throw new RuntimeException("Did not find user name - " + theUsername);
    }

    @Override
    public void save(User theUser) {

    }

    @Override
    public void deleteByUsername(String theUsername) {

    }
}
