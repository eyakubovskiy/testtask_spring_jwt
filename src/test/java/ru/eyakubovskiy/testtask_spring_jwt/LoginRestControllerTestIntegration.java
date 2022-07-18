package ru.eyakubovskiy.testtask_spring_jwt;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
//import ru.eyakubovskiy.testtask_spring_jwt.config.ContainersInitializer;
import ru.eyakubovskiy.testtask_spring_jwt.dto.LoginRequestDto;
import ru.eyakubovskiy.testtask_spring_jwt.model.User;
import ru.eyakubovskiy.testtask_spring_jwt.security.JwtTokenProvider;
import ru.eyakubovskiy.testtask_spring_jwt.service.MessageService;
import ru.eyakubovskiy.testtask_spring_jwt.service.UserService;

import java.net.URI;
import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class LoginRestControllerTestIntegration {

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
