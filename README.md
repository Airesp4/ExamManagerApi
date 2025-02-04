# Exam Manager - Backend

## Descrição

O backend da aplicação **Exam Manager** é uma API RESTful desenvolvida com **Spring Boot**. Ela fornece recursos para gerenciar exames, provas, questões e usuários. A API se comunica com o banco de dados e oferece endpoints para manipulação dos dados de maneira simples e eficiente.

## Tecnologias Utilizadas

- **Spring Boot**
- **Spring Data JPA** (para acesso ao banco de dados)
- **Spring Security** (para autenticação e autorização)
- **JWT** (para autenticação baseada em token)
- **PostgreSQL** (banco de dados)
- **Maven** (gerenciador de dependências)
- **Lombok** (para reduzir a verbosidade do código com anotações como `@Getter`, `@Setter`, etc.)
- **Springdoc OpenAPI** (para documentação da API e integração com Swagger)

## Funcionalidades

- **Cadastro de Provas**: Criar, listar, atualizar e excluir provas.
- **Gestão de Questões**: Criar, listar, atualizar e excluir questões associadas às provas.
- **Autenticação de Usuários**: Login de usuários com token JWT.
- **Gerenciamento de Usuários**: Criar, listar, atualizar e excluir usuários.

## Estrutura de Diretórios

```
/src
  /main
    /java
      /com
        /app
          /ExamManager
            /configuration # Definições de Cors e autorização de requisições (Spring Security)
            /controller  # Controllers da API
            /model       # Modelos de dados (Entidades)
            /repository  # Interfaces de repositórios
            /service     # Lógica de negócios
            /security    # Configuração de segurança (JWT)
            /dto         # Objetos de transferência de dados (DTO)
    /resources
      /application.properties  # Arquivo de configuração do Spring Boot
```

## Como Rodar a Aplicação Localmente

### Pré-requisitos

1. **Java 17 ou superior** instalado.
2. **Maven** instalado.
3. **PostgreSQL** ou outro banco de dados configurado e rodando.

### Passos para Execução

1. Clone o repositório:

   ```bash
   git clone https://github.com/Airesp4/ExamManagerApi.git
   cd ExameManagerBackend
   ```

2. Configure o arquivo `application.properties` com as credenciais de seu banco de dados:

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/seu_banco
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   ```

3. Compile e rode a aplicação:

   ```bash
   mvn spring-boot:run
   ```

4. A API estará disponível em `http://localhost:8080`.

## Conclusão

Este projeto é uma API completa para o gerenciamento de exames, provas e questões, com funcionalidades de autenticação de usuários, permitindo fácil integração com o front-end e escalabilidade. A utilização de **Spring Boot** e **JWT** assegura uma arquitetura robusta e segura, enquanto o uso de **Lombok** e **Springdoc OpenAPI** facilita o desenvolvimento e a documentação da API. Com essas ferramentas, o **Exam Manager** oferece uma solução eficiente e de fácil manutenção para o gerenciamento de provas e exames.
