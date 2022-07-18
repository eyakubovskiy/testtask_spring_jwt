package ru.eyakubovskiy.testtask_spring_jwt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.eyakubovskiy.testtask_spring_jwt.dto.LoginRequestDto;
import ru.eyakubovskiy.testtask_spring_jwt.model.User;
import ru.eyakubovskiy.testtask_spring_jwt.security.JwtTokenProvider;
import ru.eyakubovskiy.testtask_spring_jwt.service.UserService;

import java.util.Map;

@RequiredArgsConstructor
@RequestMapping(value = "/api/")
@RestController
public class LoginRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @PostMapping(value = "login")
    public ResponseEntity login(@RequestBody LoginRequestDto loginRequestDto) {
            String username = loginRequestDto.name();
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            loginRequestDto.password()));
            User user = userService.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User \"" + username + "\" not found");
            }

            String token = jwtTokenProvider.createToken(username);
            Map result = Map.of("token", token);

            return ResponseEntity.ok(result);
    }
}
