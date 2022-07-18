package ru.eyakubovskiy.testtask_spring_jwt;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.eyakubovskiy.testtask_spring_jwt.model.User;
import ru.eyakubovskiy.testtask_spring_jwt.repository.UserRepository;
import ru.eyakubovskiy.testtask_spring_jwt.service.UserService;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class UserServiceTestIntegration {

    @Container
    public static PostgreSQLContainer postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:14.4")
                    .withDatabaseName("jwtdemo")
                    .withPassword("postgres")
                    .withUsername("postgres")
                    .withExposedPorts(5432)
                    .withStartupTimeout(Duration.ofSeconds(30));

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.driver-class-name:org.postgresql.Driver", postgreSQLContainer::getDriverClassName);
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public UserService userService;

    @BeforeEach
    void setUp() {
        deleteAllNotLiquibaseCreatedUsers();
    }

    @AfterEach
    void tearDown() {
        deleteAllNotLiquibaseCreatedUsers();
    }

    @Test
    public void correctNotFindByUsernameWhenNoUser() {
        User userExpect = userService.findByUsername("user_test1");

        assertNull(userExpect);
    }

    @Test
    public void correctFindByUsernameWhenHasUser() {
        User user = new User();
        user.setName("user_test2");
        user.setPassword("user_test_password");
        User userSaved = userRepository.save(user);

        User userExpect = userService.findByUsername(userSaved.getName());

        assertEquals(userExpect.getId(), user.getId());
        assertEquals(userExpect.getPassword(), user.getPassword());
    }

    @Test
    public void correctNotFindByIdWhenNoUser() {
        User userExpect = userService.findById(100L);

        assertNull(userExpect);
    }

    @Test
    public void correctFindByIdWhenHasUser() {
        User user = new User();
        user.setName("user_test3");
        user.setPassword("user_test_password");

        User userSaved = userRepository.save(user);

        User userExpect = userService.findById(userSaved.getId());
        assertEquals(userExpect.getId(), user.getId());
        assertEquals(userExpect.getPassword(), user.getPassword());
    }

    private void deleteAllNotLiquibaseCreatedUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            Long id = user.getId();
            if (id != 1L && id != 2L) {
                userRepository.deleteById(id);
            }
        }
    }
}
