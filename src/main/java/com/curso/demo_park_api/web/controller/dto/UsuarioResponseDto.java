package com.curso.demo_park_api.web.controller.dto;

import com.curso.demo_park_api.roles.Roles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UsuarioResponseDto {

    private Long id;
    private String username;
    private String role;


    public UsuarioResponseDto() {
    }

    public UsuarioResponseDto(Long id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;

    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    //    @JsonIgnore
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

//    @JsonProperty("role") // Define o nome da chave JSON como "role"
//    public String getRoleFormatada() {
//        if (this.role == null) {
//            return null;
//        }
//
//        String nomeEnum = this.role.name();
//
//        // Remove "ROLE_" e converte para minúsculas
//        if (nomeEnum.startsWith("ROLE_")) {
//            return nomeEnum.substring(5).toLowerCase();
//        }
//
//        // Retorna o nome em minúsculas (caso não tenha o prefixo)
//        return nomeEnum.toLowerCase();
//    }

}
