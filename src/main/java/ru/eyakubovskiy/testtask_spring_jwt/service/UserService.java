package ru.eyakubovskiy.testtask_spring_jwt.service;

import ru.eyakubovskiy.testtask_spring_jwt.model.User;

public interface UserService {
    User findByUsername(String username);

    User findById(Long id);
}
