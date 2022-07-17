package ru.eyakubovskiy.testtask_spring_oauth2.service;

import ru.eyakubovskiy.testtask_spring_oauth2.model.User;

public interface UserService {
    User findByUsername(String username);
    User findById(Long id);
}
