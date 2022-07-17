package ru.eyakubovskiy.testtask_spring_oauth2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.eyakubovskiy.testtask_spring_oauth2.dto.LoginRequestDto;
import ru.eyakubovskiy.testtask_spring_oauth2.model.User;
import ru.eyakubovskiy.testtask_spring_oauth2.security.JwtTokenProvider;
import ru.eyakubovskiy.testtask_spring_oauth2.service.MessageService;
import ru.eyakubovskiy.testtask_spring_oauth2.service.UserService;

import java.net.URI;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginRestControllerTestIntegration {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    MessageService messageService;

    @MockBean
    UserService userService;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @Test
    public void returnTokenWhenLoginPasswordIsCorrect() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        String expectToken = "98765432109876543210";
        when(jwtTokenProvider.createToken(eq("user"))).thenReturn(expectToken);

        String expectResponse = objectMapper.writeValueAsString(Map.of("token", expectToken));

        User user = new User();
        user.setId(1L);
        user.setName("user");
        String passwordEncoded = passwordEncoder.encode("user");
        user.setPassword(passwordEncoded);

        when(userService.findByUsername(eq("user"))).thenReturn(user);

        URI uri = new URI("http://localhost:" + randomServerPort + "/api/login");
        String request = objectMapper.writeValueAsString(new LoginRequestDto("user", "user"));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(request,httpHeaders);
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(expectResponse, response.getBody());
    }

    @Test
    public void returnError403WhenLoginPasswordIsIncorrect() throws Exception {
        String expectToken = "98765432109876543210";
        when(jwtTokenProvider.createToken(eq("user"))).thenReturn(expectToken);

        User user = new User();
        user.setId(1L);
        user.setName("user");
        String passwordEncoded = passwordEncoder.encode("wrongpassword");
        user.setPassword(passwordEncoded);

        when(userService.findByUsername(eq("user"))).thenReturn(user);

        URI uri = new URI("http://localhost:" + randomServerPort + "/api/login");
        String request = new ObjectMapper().writeValueAsString(new LoginRequestDto("user", "user"));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(request,httpHeaders);
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);

        assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
    }


}
