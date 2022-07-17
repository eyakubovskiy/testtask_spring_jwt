package ru.eyakubovskiy.testtask_spring_oauth2.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record LoginRequestDto(String name, String password) {
}
