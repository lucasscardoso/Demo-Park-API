
# Demo-Park-Api
- API de Gestão de EstacionamentoA demo-park-api é uma aplicação backend robusta e segura, desenvolvida em Java com o framework Spring Boot.
-  Ela foi projetada para gerenciar operações básicas de um sistema de estacionamento, começando pela gestão de usuários. A segurança é implementada através do Spring Security e JSON Web Tokens (JWT).

## Tecnologias:
- Linguagem: Java17
- FrameworkSpring Boot 3.5.7
- SegurançaSpring Security
- TokenizaçãoJWT (JSON Web Token)
- Build ToolApache Maven3.9+
- Banco de DadosMySQL8.0+
- ContainerizaçãoDockerLatest

##  Como Rodar a Aplicaçãoa:
forma mais recomendada para executar o projeto é utilizando o Docker Compose, que gerencia a aplicação e o banco de dados juntos.Pré-requisitosCertifique-se de ter o Docker e o Docker Compose instalados em seu sistema.
- Inicialização via Docker ComposeExecute o comando abaixo na raiz do projeto (onde está localizado o arquivo docker-compose.yml):Bashdocker-compose up --build -d , O parâmetro --build garante que a imagem da aplicação seja criada a partir do Dockerfile.O parâmetro -d executa os containers em background (modo detached).
- Acesso à AplicaçãoAPI (Aplicação Spring Boot): Rodando em http://localhost:8080Banco de Dados (MySQL): Rodando na porta 3306 (acessível apenas pelo container da aplicação por padrão).
- Endpoints de Segurança e UsuáriosAutenticação (Login)MétodoEndpointDescriçãoPOST/api/v1/authGera o JWT após validação das credenciais do usuário.Gestão de Usuários (CRUD)MétodoEndpointDescriçãoRequer JWTPOST/api/v1/usuariosCria um novo usuário.NãoGET/api/v1/usuarios/{id}Busca um usuário por ID (Apenas ADMIN).SimPATCH/api/v1/usuarios/{id}Altera a senha do usuário (Apenas o próprio usuário ou ADMIN).SimNota: Todos os endpoints protegidos requerem o envio do JWT válido no cabeçalho Authorization no formato: Authorization: Bearer <seu_token>.
- Configuração da AplicaçãoAs configurações de banco de dados e JWT são definidas nos arquivos de configuração do Spring Boot e nos arquivos do Docker.Variáveis de Ambiente e Docker ComposeO arquivo docker-compose.yml e o Dockerfile encapsulam a configuração necessária para o ambiente de desenvolvimento, incluindo:MySQL: Nome do banco de dados, usuário e senha para o container do MySQL.Spring Boot: URL de conexão do JDBC, nome do banco, usuário e senha, que são passados como variáveis de ambiente para o container da aplicação.
## TestesTestes Unitários
- O projeto inclui testes unitários criados para garantir a correta implementação da lógica de negócio e serviços.Para executar os testes unitários via Maven:Bashmvn test

### Swagger
- http://localhost:8080/docs-park.html

