package ru.eyakubovskiy.testtask_spring_oauth2.security;

import ru.eyakubovskiy.testtask_spring_oauth2.model.User;

public class JwtUserFactory {
    public JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getName(),
                user.getPassword(),
                null); //выпилить
    }
}
