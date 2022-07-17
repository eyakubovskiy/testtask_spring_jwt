package ru.eyakubovskiy.testtask_spring_oauth2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.eyakubovskiy.testtask_spring_oauth2.dto.MessageResponseDto;
import ru.eyakubovskiy.testtask_spring_oauth2.dto.MessageRequestDto;
import ru.eyakubovskiy.testtask_spring_oauth2.service.MessageService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(value = "/api/")
@RestController
public class MessageController {

    private final MessageService messageService;

    @PostMapping(value = "message")
    public ResponseEntity message(@RequestBody MessageRequestDto messageDto) {

        boolean isHistoryRequest = messageDto.message().startsWith("history ");
        ResponseEntity result = null;

////////////////Вынести в апп сервис
        try {
            if (!isHistoryRequest) {
                messageService.addMessage(messageDto);

                result = ResponseEntity.ok("");
            } else {
                String messageText = messageDto.message();
                int indexOfStartCountText = messageText.indexOf(" ") + 1;
                String countText = messageDto.message().substring(indexOfStartCountText);
                int count = Integer.parseInt(countText);
                List<MessageResponseDto> messages = messageService.getLastMessages(count);

                result = ResponseEntity.ok(messages);
            }
        } catch (Exception e) {
            System.out.println("ERROR!!!" + System.lineSeparator() + e.getMessage() + System.lineSeparator() + e.getStackTrace().toString()  + System.lineSeparator());
            e.printStackTrace(System.out);
            System.out.println("END ERROR");
        }
        return result;
    }
}
