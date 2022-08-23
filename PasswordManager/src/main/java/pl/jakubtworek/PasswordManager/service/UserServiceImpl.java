package pl.jakubtworek.PasswordManager.service;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import pl.jakubtworek.PasswordManager.dao.UserDAO;
import pl.jakubtworek.PasswordManager.entity.Password;
import pl.jakubtworek.PasswordManager.entity.User;

import java.sql.*;
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
        return userDAO.findAll();
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
        userDAO.save(theUser);
    }

    @Override
    public void deleteByUsername(String theUsername) {
        userDAO.delete(findByUsername(theUsername));
    }

    public void insertAuthority(@Param("username") String username, @Param("authority") String authority){
        try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/passwordmanager_db?useSSL=false&serverTimezone=UTC", "root", "qazwsx12345");
            Statement stmt = conn.createStatement();
        ) {
            String sql = "INSERT INTO authorities(username,authority)" + " values (?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,authority);
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS=0");
            preparedStatement.execute();
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS=1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
