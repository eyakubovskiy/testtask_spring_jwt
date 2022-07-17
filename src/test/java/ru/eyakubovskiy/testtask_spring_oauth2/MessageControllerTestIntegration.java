package ru.eyakubovskiy.testtask_spring_oauth2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.eyakubovskiy.testtask_spring_oauth2.dto.MessageRequestDto;
import ru.eyakubovskiy.testtask_spring_oauth2.dto.MessageResponseDto;
import ru.eyakubovskiy.testtask_spring_oauth2.security.JwtTokenProvider;
import ru.eyakubovskiy.testtask_spring_oauth2.service.MessageService;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MessageControllerTestIntegration {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    MessageService messageService;

    @Test
    public void correctStatusWhenCorrectToken() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        List<MessageResponseDto> messages = List.of(
                new MessageResponseDto(0L, "user", "message1"),
                new MessageResponseDto(1L, "user", "message2"),
                new MessageResponseDto(2L, "user", "message3")
        );
        when(messageService.getLastMessages(3)).thenReturn(messages);

        String requestBody = objectMapper.writeValueAsString(new MessageRequestDto("user", "history 3"));

        String token = jwtTokenProvider.createToken("user");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer_" + token);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

        URI uri = new URI("http://localhost:" + randomServerPort + "/api/message");
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void correctResponseWhenCorrectToken() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        List<MessageResponseDto> messages = List.of(
                new MessageResponseDto(0L, "user", "message1"),
                new MessageResponseDto(1L, "user", "message2"),
                new MessageResponseDto(2L, "user", "message3")
        );
        when(messageService.getLastMessages(3)).thenReturn(messages);

        String expectResponse = objectMapper.writeValueAsString(messages);

        String requestBody = objectMapper.writeValueAsString(new MessageRequestDto("user", "history 3"));

        String token = jwtTokenProvider.createToken("user");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer_" + token);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

        URI uri = new URI("http://localhost:" + randomServerPort + "/api/message");
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);

        assertEquals(expectResponse, response.getBody());
    }

    @Test
    public void returnError403WhenIncorrectToken() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        String requestBody = objectMapper.writeValueAsString(new MessageRequestDto("user", "history 3"));

        String token = "incorrectToken";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer_" + token);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

        URI uri = new URI("http://localhost:" + randomServerPort + "/api/message");
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void correctStatusWhenSendMessage() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        String requestBody = objectMapper.writeValueAsString(new MessageRequestDto("user", "testMessage"));

        String token = jwtTokenProvider.createToken("user");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer_" + token);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

        URI uri = new URI("http://localhost:" + randomServerPort + "/api/message");
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
