package com.curso.demo_park_api.web.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserSenhaDto {

    @NotBlank(message = "A senha é obrigatória")

    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{6,10}$",
            message = "A senha precisa ter entre 6 a 10 caracteres,deve conter: 1 letra maiúscula, 1 minúscula, 1 número e 1 caractere especial.")
    private String password;

    @NotBlank(message = "A senha é obrigatória")

    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{6,10}$",
            message = "A senha precisa ter entre 6 a 10 caracteres,deve conter: 1 letra maiúscula, 1 minúscula, 1 número e 1 caractere especial.")
    private String newPassword;

    @NotBlank(message = "A senha é obrigatória")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{6,10}$",
            message = "A senha precisa ter entre 6 a 10 caracteres,deve conter: 1 letra maiúscula, 1 minúscula, 1 número e 1 caractere especial."
    )
    private String confirmPassword;

    public UserSenhaDto() {
    }

    public UserSenhaDto(String password, String newPassword, String confirmPassword) {
        this.password = password;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
