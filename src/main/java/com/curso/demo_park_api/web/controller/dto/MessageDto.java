package com.curso.demo_park_api.web.controller.dto;

public class MessageDto {

    private final String message;


    public MessageDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
