package ru.eyakubovskiy.testtask_spring_oauth2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.eyakubovskiy.testtask_spring_oauth2.dto.MessageResponseDto;
import ru.eyakubovskiy.testtask_spring_oauth2.model.Message;
import ru.eyakubovskiy.testtask_spring_oauth2.model.User;
import ru.eyakubovskiy.testtask_spring_oauth2.repository.MessageRepository;
import ru.eyakubovskiy.testtask_spring_oauth2.service.MessageService;
import ru.eyakubovskiy.testtask_spring_oauth2.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MessageServiceTestIntegration {

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
