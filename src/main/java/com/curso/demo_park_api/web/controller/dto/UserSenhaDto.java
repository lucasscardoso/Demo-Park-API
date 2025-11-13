package com.curso.demo_park_api.web.controller.dto;

public class UserSenhaDto {

    private String password;

    public UserSenhaDto(){}
    public UserSenhaDto(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String passord) {
        this.password = passord;
    }
}
