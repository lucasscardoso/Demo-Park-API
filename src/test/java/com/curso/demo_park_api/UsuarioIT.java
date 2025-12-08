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
    public void valida_Criacao_LoginSenha_retorna_status201_criado() {
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

    //Valida User Já Cadastrado
    @Test
    public void valida_LoginExistente_retorna_status409() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("lucas@gmail.com", "0412@Lost"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getMessage()).isEqualTo("Usuario {lucas@gmail.com} já cadastrado!");

    }

    //Valida Email
    @Test
    public void valida_Login_retorna_status422_usuario_vazio_email_formato_incorreto() {

        //Valida email vazio
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

        //valida email incompleto até o @

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

        //valida email incompleto faltando .com
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

        //valida email correto, porem senha maior que o esperado/incorreta
        responseBody = testClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("lucas@teste.com", "0412@Lost1222"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    //Valida alteração de senha
    @Test
    public void valida_Senha_retorna_status200_senha_alterada_com_sucesso() {
        //valida senha alterada corretamente
        MessageDto responseBody = responseBody = testClient
                .patch()
                .uri("api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserSenhaDto("0412@Lost", "0412@Yuri", "0412@Yuri"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(MessageDto.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getMessage()).isEqualTo("Senha do usuário lucas@gmail.com foi alterada com sucesso!");

    }

    @Test
    public void valida_Senha_retorna_status422_senha_invalida_ou_faltante_na_novaSenha_confirmacao_ou_atual() {

        //valida a troca de senha faltando a senha atual
        ErrorMessage responseEmpityPassword = testClient
                .patch()
                .uri("api/v1/usuarios/1001")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserSenhaDto("", "0412@Yost", "0412@Yost"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseEmpityPassword).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseEmpityPassword.getMessage()).isEqualTo("Campo(s) invalidos!");

        //valida a troca de senha faltando a senha nova
        ErrorMessage responseEmpityNewPassword = testClient
                .patch()
                .uri("api/v1/usuarios/1001")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserSenhaDto("0412@Lost", "", "0412@Yost122"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(responseEmpityNewPassword).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseEmpityNewPassword.getMessage()).isEqualTo("Campo(s) invalidos!");

        //valida a troca de senha faltando a senha de confirmação
        ErrorMessage responseEmpityConfirmPassword = testClient
                .patch()
                .uri("api/v1/usuarios/1001")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserSenhaDto("0412@Lost", "0412@Yost122", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(responseEmpityConfirmPassword).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseEmpityConfirmPassword.getMessage()).isEqualTo("Campo(s) invalidos!");
    }

    @Test
    public void valida_Senha_retorna_status422_senha_Menor_ou_Maior_Que_Padrao() {

        //valida nova senha e confirmação menor do que o solicitado por padrao
        MessageDto responseBody = testClient
                .patch()
                .uri("api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserSenhaDto("0412@Lost", "0412@", "0412@"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(MessageDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getMessage()).isEqualTo("Campo(s) invalidos!");

        //valida quando senha nova e confirmação são maiores que o padrão estabelecido
        ErrorMessage response = testClient
                .patch()
                .uri("api/v1/usuarios/1001")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserSenhaDto("0412@Lost", "0412@Yost122", "0412@Yost122"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(response).isNotNull();
        org.assertj.core.api.Assertions.assertThat(response.getMessage()).isEqualTo("Campo(s) invalidos!");

    }

    @Test
    public void valida_Senha_retorna_status404_Usuario_nao_existe() {

        //valida alteração de senha, porem id do usuario nao existe
        MessageDto responseBody = testClient
                .patch()
                .uri("api/v1/usuarios/1001")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserSenhaDto("0412@Lost", "0412@Lost", "0412@Lost"))
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(MessageDto.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getMessage()).isEqualTo("Usuario id = 1001 não encontrado.");

    }

    @Test
    public void valida_Senha_retorna_status204_senha_nova_nao_confere_com_confirmacao() {

        //valida troca de senha quando senha nova nao confere com a confirmação
        MessageDto responseBody = testClient
                .patch()
                .uri("api/v1/usuarios/1001")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserSenhaDto("0412@Lost", "0412@Yost", "0412@Yosr"))
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

        ParameterizedTypeReference<List<UsuarioResponseDto>> list = new ParameterizedTypeReference<List<UsuarioResponseDto>>() {
        };
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

    //valida busca de user por id
    @Test
    public void valida_Busca_Por_ID_retorna_status200() {

        //valida user localizado com sucesso
        UsuarioResponseDto responseBody = testClient
                .get()
                .uri("/api/v1/usuarios/100")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("lucas@gmail.com");
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("ADMIN");

        //valida user não localizado
        ErrorMessage responseErro = testClient
                .get()
                .uri("/api/v1/usuarios/100001")
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseErro).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseErro.getMessage()).isEqualTo("Usuario id = 100001 não encontrado.");
    }

    //valida casos de erro interno na api na busca de usuarios
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

        //valida id invalido
        ErrorMessage responseBody = testClient
                .delete()
                .uri("/api/v1/usuarios/1001")
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getMessage()).isEqualTo("Usuario id = 1001 não encontrado.");

        //valida exclusao com sucesso
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
