package com.curso.demo_park_api.web.controller.converter;

import com.curso.demo_park_api.entity.Usuario;
import com.curso.demo_park_api.web.controller.dto.UsuarioResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class UserToResponse {

    public UsuarioResponseDto convertCreatUser(Usuario usuario){
        UsuarioResponseDto response = new UsuarioResponseDto(usuario.getId(),usuario.getUsername(),usuario.getRole());
        return  response;
    }

    public List<UsuarioResponseDto> convertList(List<Usuario> user){
        return user.stream().map(this::convertCreatUser).collect(Collectors.toList());

    }
}
