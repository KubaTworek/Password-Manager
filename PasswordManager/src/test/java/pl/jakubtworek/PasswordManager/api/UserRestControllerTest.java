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
import pl.jakubtworek.PasswordManager.service.UserService;

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
public class UserRestControllerTest {

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
    private UserService userService;

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
    public void getUsersHttpRequest() throws Exception {

        user.setUsername("admin");
        user.setPassword("qwerty");
        user.setEnabled(1);
        entityManager.persist(user);
        entityManager.flush();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void createUserHttpRequest() throws Exception {
        user.setUsername("admin");
        user.setPassword("qwerty");
        user.setEnabled(1);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        User verifyUser = userService.findByUsername("admin");
        assertNotNull(verifyUser);
    }

    @Test
    public void deleteUserHttpRequest() throws Exception {
        assertNotNull(userService.findByUsername("admin_log"));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{userUsername}", "admin_log"))
                .andExpect(status().isOk());
    }


    @Test
    public void getUserByUsernameHttpRequest() throws Exception {

        User user = userService.findByUsername("admin_log");

        assertNotNull(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/username/{userUsername}", "admin_log"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.username", is("admin_log")));
    }



    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute("DELETE FROM password");
        jdbc.execute("DELETE FROM users");
        jdbc.execute("DELETE FROM category");
    }
}
