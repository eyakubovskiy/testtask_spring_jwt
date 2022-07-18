package ru.eyakubovskiy.testtask_spring_jwt.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.eyakubovskiy.testtask_spring_jwt.model.User;

@RequiredArgsConstructor
@Setter
@Getter
public class UserDto {
    private final Long id;
    private final String name;
    private final String password;

    public static User toUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setPassword(userDto.getPassword());

        return user;
    }

    public static UserDto fromUser(User user) {
        UserDto userDto = new UserDto(user.getId(), user.getName(), user.getPassword());
        return userDto;
    }

}
