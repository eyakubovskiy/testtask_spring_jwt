package ru.eyakubovskiy.testtask_spring_jwt.security;

import ru.eyakubovskiy.testtask_spring_jwt.model.User;

public class JwtUserFactory {
    public JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getName(),
                user.getPassword(),
                null);
    }
}
