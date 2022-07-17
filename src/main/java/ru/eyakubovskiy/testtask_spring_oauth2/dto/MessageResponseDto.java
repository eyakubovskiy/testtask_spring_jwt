package ru.eyakubovskiy.testtask_spring_oauth2.dto;

import lombok.Getter;
import lombok.Setter;
import ru.eyakubovskiy.testtask_spring_oauth2.model.Message;

//public record MessageDto(String name, String message) {
//
//}
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
        System.out.println("User is null - " + message.getUser() == null);
        MessageResponseDto messageResponseDto = new MessageResponseDto(message.getId(),
                message.getUser().getName(),
                message.getMessage());
        return messageResponseDto;
    }

}
