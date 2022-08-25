package pl.jakubtworek.PasswordManager.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import pl.jakubtworek.PasswordManager.entity.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute("SET FOREIGN_KEY_CHECKS=0");
        jdbc.execute("INSERT INTO category(id, name)" + "VALUES (1,'Social Media')");
        jdbc.execute("INSERT INTO category(id, name)" + "VALUES (2,'Bank')");
        jdbc.execute("INSERT INTO users(username, password, enabled)" + "VALUES ('admin_log', 'admin_pas', 1)");
        jdbc.execute("INSERT INTO password(id, name, value, category_id, user_username) " + "VALUES (1,'Facebook','qwerty',1,'admin')");
        jdbc.execute("SET FOREIGN_KEY_CHECKS=1");
    }

    @Test
    public void findAllUsersTest() {
        Iterable<User> iterableUsers = userService.findAll();

        List<User> users = new ArrayList<>();

        for(User user : iterableUsers) {
            users.add(user);
        }

        assertEquals(1,users.size());
    }

    @Test
    public void findByUsernameUserTest() {
        User user = userService.findByUsername("admin_log");

        assertEquals("admin_pas",user.getPassword());
    }

    @Test
    public void saveUserTest() {
        User user = userService.findByUsername("admin_log");
        user.setUsername("admin2");
        userService.save(user);

        List<User> users = userService.findAll();

        assertEquals(2,users.size());
    }

    @Test
    public void deleteByUsernameUserTest() {
        userService.deleteByUsername("admin_log");

        List<User> users = userService.findAll();

        assertEquals(0,users.size());
    }

//    @Test
//    public void insertAuthorityTest() {
//    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute("DELETE FROM password");
        jdbc.execute("DELETE FROM users");
        jdbc.execute("DELETE FROM category");
    }
}
