package pl.jakubtworek.PasswordManager.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import pl.jakubtworek.PasswordManager.entity.Password;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class PasswordServiceTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private CategoryService categoryService;

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
    public void findAllPasswordsTest(){
        Iterable<Password> iterablePasswords = passwordService.findAll();

        List<Password> passwords = new ArrayList<>();

        for(Password password : iterablePasswords) {
            passwords.add(password);
        }

        assertEquals(1,passwords.size());
    }

    @Test
    public void findByIdPasswordTest(){
        Password password = passwordService.findById(1);

        assertEquals("Facebook",password.getName());
    }

    @Test
    public void savePasswordTest(){
        Password password = passwordService.findById(1);
        password.setId(2);
        password.setName("Instagram");
        password.setValue("qaz");
        passwordService.save(password);

        Iterable<Password> iterablePasswords = passwordService.findAll();

        List<Password> passwords = new ArrayList<>();

        for(Password tempPassword : iterablePasswords) {
            passwords.add(tempPassword);
        }

        assertEquals(2,passwords.size());
    }

    @Test
    public void deleteByIdPasswordTest(){
        passwordService.deleteById(1);
        Iterable<Password> iterablePasswords = passwordService.findAll();

        List<Password> passwords = new ArrayList<>();

        for(Password password : iterablePasswords) {
            passwords.add(password);
        }

        assertEquals(0,passwords.size());
    }

    @Test
    public void findByNamePasswordTest(){
        Iterable<Password> iterablePasswords = passwordService.findByName("Facebook");

        List<Password> passwords = new ArrayList<>();

        for(Password password : iterablePasswords) {
            passwords.add(password);
        }

        for(Password password : passwords){
            assertEquals("Facebook",password.getName());
        }
    }

/*    @Test
    public void findByCategoryPasswordTest(){
        Iterable<Password> iterablePasswords = passwordService.findByCategory(categoryService.findByName("Social Media"));

        List<Password> passwords = new ArrayList<>();

        for(Password password : iterablePasswords) {
            passwords.add(password);
        }

        for(Password password : passwords){
            assertEquals("Social Media",password.getCategory().getName());
        }
    }*/

    @Test
    public void findByAllUserPasswordTest(){
        Iterable<Password> iterablePasswords = passwordService.findByCategory(categoryService.findByName("Social Media"));

        List<Password> passwords = new ArrayList<>();

        for(Password password : iterablePasswords) {
            passwords.add(password);
        }

        assertEquals("Facebook",passwords.get(0).getName());
    }

/*    @Test
    public void saveWithCategoryPasswordTest(){
        Password password = passwordService.findById(1);
        Category category = categoryService.findById(2);
        password.setId(2);
        password.setName("Instagram");
        password.setValue("qaz");
        passwordService.saveWithCategory(password, category);

        Iterable<Password> iterablePasswords = passwordService.findAll();

        List<Password> passwordsSM = new ArrayList<>();
        List<Password> passwordsB = new ArrayList<>();

        for(Password tempPassword : iterablePasswords) {
            if(Objects.equals(tempPassword.getCategory().getName(), "Social Media")) passwordsSM.add(password);
            if(Objects.equals(tempPassword.getCategory().getName(), "Bank"))passwordsB.add(password);
        }

        assertEquals(1,passwordsSM.size());
        assertEquals(1,passwordsB.size());
    }

    @Test
    public void saveWithCategoryAndUserPasswordTest(){
        Password password = passwordService.findById(1);
        password.setId(2);
        password.setName("Instagram");
        password.setValue("qaz");
        passwordService.saveWithCategoryAndUser(password, categoryService.findByName("Bank"), userService.findByUsername("admin_log"));

        Iterable<Password> iterablePasswords = passwordService.findAll();

        List<Password> passwordsSM = new ArrayList<>();
        List<Password> passwordsB = new ArrayList<>();
        List<Password> passwordsU = new ArrayList<>();

        for(Password tempPassword : iterablePasswords) {
            if(tempPassword.getCategory().getName()=="Social Media") passwordsSM.add(password);
            if(tempPassword.getCategory().getName()=="Bank")passwordsB.add(password);
            if(tempPassword.getUser().getUsername()=="admin_log")passwordsU.add(password);
        }

        assertEquals(1,passwordsSM.size());
        assertEquals(1,passwordsB.size());
        assertEquals(2,passwordsU.size());
    }*/

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute("DELETE FROM password");
        jdbc.execute("DELETE FROM users");
        jdbc.execute("DELETE FROM category");
    }
}
