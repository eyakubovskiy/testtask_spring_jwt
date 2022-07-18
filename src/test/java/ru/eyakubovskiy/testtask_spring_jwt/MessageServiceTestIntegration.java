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
import ru.eyakubovskiy.testtask_spring_jwt.dto.MessageResponseDto;
import ru.eyakubovskiy.testtask_spring_jwt.model.Message;
import ru.eyakubovskiy.testtask_spring_jwt.model.User;
import ru.eyakubovskiy.testtask_spring_jwt.repository.MessageRepository;
import ru.eyakubovskiy.testtask_spring_jwt.service.MessageService;
import ru.eyakubovskiy.testtask_spring_jwt.service.UserService;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class MessageServiceTestIntegration {

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
    public MessageRepository messageRepository;

    @Autowired
    public MessageService messageService;

    @Autowired
    public UserService userService;

    @BeforeEach
    void setUp() {
        messageRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        messageRepository.deleteAll();
    }

    @Test
    public void returnNullWhenThereAreNoMessages() {
        List<MessageResponseDto> messages = messageService.getLastMessages(15);
        assertTrue(messages.isEmpty());
    }

    @Test
    public void returnAllMessagesWhenTheirNumberIsLessThanRequested() {
        User user = userService.findByUsername("user");
        String messageText = "MessageText";

        for (int i = 0; i < 3; i++) {
            Message message = new Message();
            message.setMessage(messageText + i);
            message.setUser(user);
            messageRepository.save(message);
        }

        List<MessageResponseDto> messages = messageService.getLastMessages(4);
        assertEquals(3, messages.size());
    }

    @Test
    public void returnCorrectNumbersOfMessages() {
        User user = userService.findByUsername("user");
        String messageText = "MessageText";

        for (int i = 0; i < 10; i++) {
            Message message = new Message();
            message.setMessage(messageText + i);
            message.setUser(user);
            messageRepository.save(message);
        }

        List<MessageResponseDto> messages = messageService.getLastMessages(4);
        assertEquals(4, messages.size());

        assertEquals("MessageText9", messages.get(0).getMessage());
        assertEquals("MessageText8", messages.get(1).getMessage());
        assertEquals("MessageText7", messages.get(2).getMessage());
        assertEquals("MessageText6", messages.get(3).getMessage());
    }
}
