package com.curso.demo_park_api.web.controller;

import com.curso.demo_park_api.entity.Usuario;

import com.curso.demo_park_api.service.UsuarioService;
import com.curso.demo_park_api.web.controller.converter.UserToResponse;
import com.curso.demo_park_api.web.controller.dto.MessageDto;
import com.curso.demo_park_api.web.controller.dto.UserSenhaDto;
import com.curso.demo_park_api.web.controller.dto.UsuarioCreateDto;
import com.curso.demo_park_api.web.controller.dto.UsuarioResponseDto;

import com.curso.demo_park_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuarios", description = "Contém todas as operações de CRUD referente a um usuario")
@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private UserToResponse userToResponse;

    public UsuarioController(UsuarioService usuarioService, UserToResponse userToResponse) {
        this.usuarioService = usuarioService;
        this.userToResponse = userToResponse;

    }

    @Operation(summary = "Cria um novo usuario", description = "recurso cria um novo usuario",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "Usuario/email ja cadastrado no sistema",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado, dados invalidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @PostMapping
    public ResponseEntity<UsuarioResponseDto> create(@Valid @RequestBody UsuarioCreateDto usuario) {
        Usuario user = usuarioService.salvar(usuario);

        UsuarioResponseDto response = userToResponse.convertCreatUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(summary = "Busca Usuario pelo ID", description = "recurso localiza um usuario pelo id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Usuario não encontrado/não existe",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão necessária.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') OR (hasRole('CLIENTE')  AND #id == authentication.principal.id)")
    public ResponseEntity<UsuarioResponseDto> getById(@PathVariable Long id) {
        Usuario user = usuarioService.findById(id);
        UsuarioResponseDto response = userToResponse.convertCreatUser(user);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Alterar senha", description = "recurso altera a senha do usuario",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Senha do usuário foi alterada com sucesso!",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDto.class))),
                    @ApiResponse(responseCode = "400", description = "nova senha não confere com a confirmação ou formato da senha invalido",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Usuario não localizado/não existe",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "campos invalidos ou mal formatados",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),

            })
    @PatchMapping("/{id}")
    public ResponseEntity<MessageDto> changePassword(@PathVariable Long id, @Valid @RequestBody UserSenhaDto senhaDto) {
        Usuario user = usuarioService.alteraSenha(id, senhaDto.getPassword(), senhaDto.getNewPassword(), senhaDto.getConfirmPassword());
        String mensagem = String.format("Senha do usuário %s foi alterada com sucesso!", user.getUsername());
        MessageDto msg = new MessageDto(mensagem);
        return ResponseEntity.ok(msg);
    }


    @Operation(summary = "Buscar todos os usuarios", description = "recurso busca todos os usuarios cadastrados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Busca concluida com sucesso!",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> getAll() {
        List<Usuario> users = usuarioService.buscaTodos();
        List<UsuarioResponseDto> response = userToResponse.convertList(users);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Deleta Usuario pelo ID", description = "recurso deleta um usuario pelo id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso deletado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageDto.class))),
                    @ApiResponse(responseCode = "404", description = "Usuario não encontrado/não existe",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDto> deleteById(@PathVariable Long id) {
        Usuario user = usuarioService.deletaUsuario(id);
        String mensagem = String.format("Usuario foi deletado com sucesso!");
        MessageDto msg = new MessageDto(mensagem);
        return ResponseEntity.ok(msg);
    }

}
