package ru.eyakubovskiy.testtask_spring_oauth2.dto;

import lombok.Getter;
import lombok.Setter;
import ru.eyakubovskiy.testtask_spring_oauth2.model.Message;

//public record MessageDto(String name, String message) {
//
//}
@Setter
@Getter
public class MessageRequestDto {

    private String name;
    private String message;

    public MessageRequestDto(String name, String message) {
        this.name = name;
        this.message = message;
    }
}
