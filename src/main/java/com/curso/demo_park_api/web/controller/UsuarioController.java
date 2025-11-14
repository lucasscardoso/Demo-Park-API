package com.curso.demo_park_api.web.controller;

import com.curso.demo_park_api.entity.Usuario;

import com.curso.demo_park_api.service.UsuarioService;
import com.curso.demo_park_api.web.controller.converter.UserToResponse;
import com.curso.demo_park_api.web.controller.dto.MessageDto;
import com.curso.demo_park_api.web.controller.dto.UserSenhaDto;
import com.curso.demo_park_api.web.controller.dto.UsuarioCreateDto;
import com.curso.demo_park_api.web.controller.dto.UsuarioResponseDto;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private UserToResponse userToResponse;

    public UsuarioController(UsuarioService usuarioService,UserToResponse userToResponse) {
        this.usuarioService = usuarioService;
        this.userToResponse = userToResponse;

    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> create(@Valid  @RequestBody UsuarioCreateDto usuario){
        Usuario user = usuarioService.salvar(usuario);

        UsuarioResponseDto response = userToResponse.convertCreatUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> getById(@PathVariable Long id){
        Usuario user = usuarioService.findById(id);
        UsuarioResponseDto response = userToResponse.convertCreatUser(user);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MessageDto> changePassword(@PathVariable Long id,@Valid @RequestBody UserSenhaDto senhaDto){
        Usuario user = usuarioService.alteraSenha(id,senhaDto.getPassword(),senhaDto.getNewPassword(),senhaDto.getConfirmPassword());
        String mensagem = String.format("Senha do usuário %s foi alterada com sucesso!", user.getUsername());
        MessageDto msg = new MessageDto(mensagem);
        return ResponseEntity.ok(msg);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>>  getAll(){
        List<Usuario> users = usuarioService.buscaTodos();
        List<UsuarioResponseDto> response = userToResponse.convertList(users);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDto> deleteById(@PathVariable Long id){
        Usuario user = usuarioService.deletaUsuario(id);
        String mensagem = String.format("Usuario foi deletado com sucesso!");
        MessageDto msg = new MessageDto(mensagem);
        return ResponseEntity.ok(msg);
    }

}
