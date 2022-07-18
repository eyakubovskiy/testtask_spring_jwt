package ru.eyakubovskiy.testtask_spring_jwt.dto;

import lombok.Getter;
import lombok.Setter;
import ru.eyakubovskiy.testtask_spring_jwt.model.Message;

@Setter
@Getter
public class MessageResponseDto {
    private Long id;
    private String name;
    private String message;

    public MessageResponseDto(Long id, String name, String message) {
        this.id = id;
        this.name = name;
        this.message = message;
    }

    public static MessageResponseDto fromMessage(Message message) {
        MessageResponseDto messageResponseDto = new MessageResponseDto(message.getId(),
                message.getUser().getName(),
                message.getMessage());
        return messageResponseDto;
    }

}
