package com.curso.demo_park_api;


import com.curso.demo_park_api.service.UsuarioService;
import com.curso.demo_park_api.web.controller.dto.MessageDto;
import com.curso.demo_park_api.web.controller.dto.UserSenhaDto;
import com.curso.demo_park_api.web.controller.dto.UsuarioCreateDto;
import com.curso.demo_park_api.web.controller.dto.UsuarioResponseDto;
import com.curso.demo_park_api.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/usuarios/usuarios-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/sql/usuarios/usuarios-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class UsuarioIT {

    @Autowired
    WebTestClient testClient;

    @SpyBean
    private UsuarioService usuarioService;

    //Valida Criação
    @Test
    public void valida_LoginSenha_retorna_status201_criado(){
        UsuarioResponseDto responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("lucas.silva@teste.com", "0412@Lost"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("lucas.silva@teste.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE");


    }

    //Valida Email
    @Test
    public void valida_Login_retorna_status422_usuario_vazio_email_formato_incorreto(){
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("", "0412@Lost"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

                responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("lucas@", "0412@Lost"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("lucas@teste", "0412@Lost"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    //Valida alteração de senha
    @Test
    public void valida_Senha_retorna_status204_senha_alterada_com_sucesso(){

        MessageDto responseBody = responseBody = testClient
                .patch()
                .uri("api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserSenhaDto("0412@Lost","0412@Yuri", "0412@Yuri"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(MessageDto.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getMessage()).isEqualTo("Senha do usuário lucas@gmail.com foi alterada com sucesso!");

                 responseBody = testClient
                .patch()
                .uri("api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserSenhaDto("0412@Lost","0412@", "0412@"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(MessageDto.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getMessage()).isEqualTo("Campo(s) invalidos!");

        responseBody = testClient
                .patch()
                .uri("api/v1/usuarios/1001")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserSenhaDto("0412@Lost","0412@Lost", "0412@Lost"))
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(MessageDto.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getMessage()).isEqualTo("Usuario id = 1001 não encontrado.");

        responseBody = testClient
                .patch()
                .uri("api/v1/usuarios/1001")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserSenhaDto("0412@Lost","0412@Yost", "0412@Yosr"))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(MessageDto.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getMessage()).isEqualTo("A nova senha e a senha de confirmação são diferentes! por gentileza, digite novamente!");
    }


    //Valida Busca Todos
    @Test
    public void valida_Busca_usuarios_status200_localizados() {

        ParameterizedTypeReference<List<UsuarioResponseDto>> list = new ParameterizedTypeReference<List<UsuarioResponseDto>>() {};
        List<UsuarioResponseDto> responseBody = testClient
               .get()
               .uri("/api/v1/usuarios")
               .exchange()
               .expectStatus().isEqualTo(200)
               .expectBody(list)
               .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.size()).isGreaterThan(0);



    }

    @Test
    public void valida_Busca_Usuarios_status500_erro_interno() {

        when(usuarioService.buscaTodos()).thenThrow(new RuntimeException("Ocorreu um erro interno."));

        ErrorMessage response = testClient
                .get()
                .uri("/api/v1/usuarios")
                .exchange()
                .expectStatus().isEqualTo(500)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(response).isNotNull();
        org.assertj.core.api.Assertions.assertThat(response.getStatus()).isEqualTo(500);
        org.assertj.core.api.Assertions.assertThat(response.getMessage()).contains("Ocorreu um erro interno.");

    }

    //Valida Exclusão
    @Test
    public void valida_Exclui_usuario_status200_usuario_nao_localizado() {
        ErrorMessage responseBody = testClient
                .delete()
                .uri("/api/v1/usuarios/1001")
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getMessage()).isEqualTo("Usuario id = 1001 não encontrado.");

        responseBody = testClient
                .delete()
                .uri("/api/v1/usuarios/100")
                .exchange()
                .expectStatus().isEqualTo(200)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getMessage()).isEqualTo("Usuario foi deletado com sucesso!");
    }

}
