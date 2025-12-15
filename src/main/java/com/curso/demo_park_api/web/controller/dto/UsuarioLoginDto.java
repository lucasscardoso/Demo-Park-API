package com.curso.demo_park_api.web.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UsuarioLoginDto {

    @NotBlank(message = "o email é obrigatorio")
    @Email(message = "formato do e-mail invalido!",
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private String username;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, max = 10, message = "A senha deve ter entre 6 e 10 caracteres.")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{6,10}$",
            message = "A senha precisa ter entre 8 a 20 caracteres,deve conter: 1 letra maiúscula, 1 minúscula, 1 número e 1 caractere especial.")
    private String password;

    public UsuarioLoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
