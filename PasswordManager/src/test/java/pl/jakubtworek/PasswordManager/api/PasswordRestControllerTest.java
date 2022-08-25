package pl.jakubtworek.PasswordManager.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.PasswordManager.entity.Category;
import pl.jakubtworek.PasswordManager.entity.Password;
import pl.jakubtworek.PasswordManager.entity.User;
import pl.jakubtworek.PasswordManager.service.PasswordService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class PasswordRestControllerTest {

    private static MockHttpServletRequest request;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private Password password;

    @Autowired
    private Category category;

    @Autowired
    private User user;


    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    @BeforeEach
    public void beforeEach() {
        jdbc.execute("SET FOREIGN_KEY_CHECKS=0");
        jdbc.execute("INSERT INTO category(id, name)" + "VALUES (1,'Social Media')");
        jdbc.execute("INSERT INTO category(id, name)" + "VALUES (2,'Bank')");
        jdbc.execute("INSERT INTO users(username, password, enabled)" + "VALUES ('admin_log', 'admin_pas', 1)");
        jdbc.execute("INSERT INTO password(id, name, value, category_id, user_username) " + "VALUES (1,'Facebook','qwerty',1,'admin_log')");
        jdbc.execute("INSERT INTO password(id, name, value, category_id, user_username) " + "VALUES (2,'Mbank','qazwsx',2,'admin_log')");
        jdbc.execute("SET FOREIGN_KEY_CHECKS=1");
    }

    @Test
    public void getPasswordsHttpRequest() throws Exception {

        category.setName("Laptop");
        user.setUsername("admin");
        user.setPassword("qwerty");
        user.setEnabled(1);
        password.setName("Dell");
        password.setValue("12345");
        password.setCategory(category);
        password.setUser(user);
        entityManager.persist(password);
        entityManager.flush();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/passwords"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void createPasswordHttpRequest() throws Exception {
        category.setName("Laptop");
        user.setUsername("admin");
        user.setPassword("qwerty");
        user.setEnabled(1);
        password.setName("Dell");
        password.setValue("12345");
        password.setCategory(category);
        password.setUser(user);

        mockMvc.perform(post("/api/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(password)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        List<Password> verifyPasswords = passwordService.findByName("Dell");
        assertNotNull(verifyPasswords.get(0));
    }

    @Test
    public void deletePasswordHttpRequest() throws Exception {
        assertNotNull(passwordService.findById(1));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/password/{passwordId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(0)));

        assertNull(passwordService.findById(1));
    }


    @Test
    public void getPasswordByIdHttpRequest() throws Exception {

        Password password = passwordService.findById(1);

        assertNotNull(password);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/passwords/id/{passwordId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Facebook")));
    }

/*    @Test
    public void updatePasswordHttpRequest() throws Exception {
        category.setName("Laptop");
        user.setUsername("admin");
        user.setPassword("qwerty");
        user.setEnabled(1);
        password.setId(1);
        password.setName("Dell");
        password.setValue("12345");
        password.setCategory(category);
        password.setUser(user);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(password)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

    }*/

    @Test
    public void getPasswordsByNameHttpRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/passwords/name/{passwordName}", "Facebook"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void getPasswordsByCategoryHttpRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/passwords/category/{categoryId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void getPasswordsByUserHttpRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/passwords/user/{username}", "admin_log"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)));
    }




    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute("DELETE FROM password");
        jdbc.execute("DELETE FROM users");
        jdbc.execute("DELETE FROM category");
    }
}
