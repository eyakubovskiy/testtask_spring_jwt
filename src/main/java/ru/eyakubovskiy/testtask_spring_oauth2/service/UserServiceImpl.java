package ru.eyakubovskiy.testtask_spring_oauth2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.eyakubovskiy.testtask_spring_oauth2.model.User;
import ru.eyakubovskiy.testtask_spring_oauth2.repository.UserRepository;

@RequiredArgsConstructor

//@Service
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User findByUsername(String username) {
        User result = userRepository.findByName(username);
        return result;
    }

    @Override
    public User findById(Long id) {
        User result = userRepository.findById(id).orElse(null);

        if (result == null) {
            return null;
        }
        return result;
    }

}
