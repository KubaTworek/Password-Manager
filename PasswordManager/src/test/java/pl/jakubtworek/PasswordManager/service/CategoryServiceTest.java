package pl.jakubtworek.PasswordManager.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import pl.jakubtworek.PasswordManager.entity.Category;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class CategoryServiceTest {

    @Autowired
    private JdbcTemplate jdbc;

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
    public void findAllCategoryTest() {
        Iterable<Category> iterableCategories = categoryService.findAll();

        List<Category> categories = new ArrayList<>();

        for(Category category : iterableCategories) {
            categories.add(category);
        }

        assertEquals(2,categories.size());
    }

    @Test
    public void findByIdCategoryTest() {
        Category category1 = categoryService.findById(1);
        Category category2 = categoryService.findById(2);

        assertEquals("Social Media",category1.getName());
        assertEquals("Bank",category2.getName());
    }

    @Test
    public void findByNameCategoryTest() {
        Category category1 = categoryService.findByName("Social Media");
        Category category2 = categoryService.findByName("Bank");

        assertEquals("Social Media",category1.getName());
        assertEquals("Bank",category2.getName());
    }

    @Test
    public void saveCategoryTest() {
        Category category = categoryService.findByName("Social Media");
        category.setId(3);
        categoryService.save(category);
        List<Category> categories = categoryService.findAll();

        assertEquals(3,categories.size());
    }

    @Test
    public void deleteByIdCategoryTest() {
        categoryService.deleteById(2);
        List<Category> categories = categoryService.findAll();

        assertEquals(1,categories.size());
    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute("DELETE FROM password");
        jdbc.execute("DELETE FROM users");
        jdbc.execute("DELETE FROM category");
    }
}
