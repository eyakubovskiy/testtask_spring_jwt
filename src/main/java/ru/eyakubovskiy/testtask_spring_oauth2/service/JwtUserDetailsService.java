package ru.eyakubovskiy.testtask_spring_oauth2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.eyakubovskiy.testtask_spring_oauth2.model.User;
import ru.eyakubovskiy.testtask_spring_oauth2.security.JwtUserFactory;

@RequiredArgsConstructor
@Service
public class JwtUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User \"" + username + "\" not found");
        }

        return JwtUserFactory.create(user);
    }
}
