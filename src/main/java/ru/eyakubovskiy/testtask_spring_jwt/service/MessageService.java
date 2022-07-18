package ru.eyakubovskiy.testtask_spring_jwt.service;

import ru.eyakubovskiy.testtask_spring_jwt.dto.MessageResponseDto;
import ru.eyakubovskiy.testtask_spring_jwt.dto.MessageRequestDto;

import java.util.List;

public interface MessageService {

    List<MessageResponseDto> getLastMessages(int countMessages);

    void addMessage(MessageRequestDto message);

}
