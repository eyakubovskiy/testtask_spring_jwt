package ru.eyakubovskiy.testtask_spring_oauth2.service;

import ru.eyakubovskiy.testtask_spring_oauth2.dto.MessageResponseDto;
import ru.eyakubovskiy.testtask_spring_oauth2.dto.MessageRequestDto;

import java.util.List;

public interface MessageService {

    List<MessageResponseDto> getLastMessages(int countMessages);
    void addMessage(MessageRequestDto message);

}
