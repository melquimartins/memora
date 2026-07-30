# Memora API

![Java](https://img.shields.io/badge/Java-26-007396?style=flat-square&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.0-6DB33F?style=flat-square&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?style=flat-square&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Supported-2496ED?style=flat-square&logo=docker&logoColor=white)
![Tests](https://img.shields.io/badge/Tests-40_Passing-2ea44f?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-gray?style=flat-square)

---

> ⚠️ Projeto em desenvolvimento contínuo. Novas adições, ajustes e melhorias são adicionados aos poucos através de commits.

---

## Sobre o Projeto

O **Memora** é uma API RESTful de alta performance desenvolvida para otimizar o processo de retenção de conhecimento através do método de **Repetição Espaçada** (*Spaced Repetition System - SRS*). 

A plataforma permite que os usuários criem **Áreas de Trabalho (Workspaces)**, organizem os assuntos em **Módulos**, cadastrem **Desafios (Flashcards/Questões)** com **Alternativas** e pratiquem a resolução calculando dinamicamente os próximos intervalos de revisão com base no nível de dificuldade selecionado.

---

## Funcionalidades Principais

- **Autenticação & Segurança Robusta**: Registro e login de usuários via JWT (JSON Web Token), com senhas criptografadas usando BCrypt e controle de acesso individualizado por recurso.
- **Gerenciamento Hierárquico de Conteúdo**:
  - **Workspaces**: Áreas de estudo separadas por temas ou disciplinas.
  - **Módulos**: Tópicos ou capítulos dentro de um workspace.
  - **Desafios**: Questões de múltipla escolha vinculadas a um módulo.
  - **Alternativas**: Opções de resposta com sinalização da alternativa correta.
- **Algoritmo de Repetição Espaçada**:
  - Cálculo inteligente do tempo de disponibilidade do desafio (`availableAgainAt`) multiplicando os fatores do módulo e desafio pelo intervalo de dificuldade escolhido (*Easy, Medium, Hard*).
- **Tratamento Global de Exceções**: Retornos padronizados em formato JSON (`NotFoundException`, `ConflictException`, `BadRequestException`, `UnauthorizedException`).
- **Documentação Interativa (Swagger/OpenAPI)**: Interface amigável para testes de endpoints integrada via `springdoc-openapi`.

---

## Tecnologias Utilizadas

| Tecnologia | Descrição |
| :--- | :--- |
| **Java 26** | Linguagem principal do ecossistema backend |
| **Spring Boot 4.1.0** | Framework para construção de microsserviços e APIs RESTful |
| **Spring Data JPA / Hibernate** | Mapeamento Objeto-Relacional (ORM) e persistência de dados |
| **Spring Security** | Autenticação, autorização e proteção de endpoints |
| **Java JWT (Auth0)** | Geração e validação de tokens JWT |
| **PostgreSQL** | Banco de dados relacional para ambiente de produção/desenvolvimento |
| **Springdoc OpenAPI UI** | Documentação interativa da API (`/swagger-ui.html`) |
| **JUnit 5 & Mockito** | Testes unitários de alta cobertura |
| **Docker & Docker Compose** | Conteinerização e orquestração da aplicação e banco de dados |

---

## Como Executar o Projeto

### Pré-requisitos
- **Java 26** (JDK 26 instalado e configurado no `PATH`)
- **Maven 3.8+** (ou utilize o wrapper `./mvnw` incluso no projeto)
- **Docker** e **Docker Compose** (opcional, mas recomendado)

---

### Opção 1: Executando via Docker Compose (Recomendado)

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/melquimartins/memora.git
   cd memora
   ```

2. **Suba o banco PostgreSQL e a aplicação:**
   ```bash
   docker-compose up -d
   ```

3. A aplicação estará disponível em `http://localhost:8080`.

---

### Opção 2: Executando Localmente com Maven

1. **Certifique-se de ter um banco PostgreSQL rodando** com as credenciais configuradas no `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/memora
   spring.datasource.username=postgres
   spring.datasource.password=postgres
   ```

2. **Execute a aplicação via Maven Wrapper:**
   - **Linux / macOS:**
     ```bash
     ./mvnw spring-boot:run
     ```
   - **Windows:**
     ```powershell
     .\mvnw.cmd spring-boot:run
     ```

---

## Documentação da API (Swagger UI)

Após iniciar a aplicação, acesse a documentação interativa no navegador para testar todos os endpoints disponíveis:

🔗 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

---

## Executando os Testes Unitários

O projeto possui uma suíte completa de testes unitários cobrindo as regras de negócio, services e segurança.

Para rodar todos os testes unitários:

- **Linux / macOS:**
  ```bash
  ./mvnw test
  ```

- **Windows:**
  ```powershell
  .\mvnw.cmd test
  ```

---

## Visão Geral das Rotas (Endpoints)

### Autenticação (`/auth`)
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/auth/signup` | Cadastra um novo usuário e retorna token JWT |
| `POST` | `/auth/signin` | Realiza login e retorna token JWT |

### Workspaces (`/workspaces`)
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/workspaces` | Cria um novo workspace |
| `GET` | `/workspaces` | Lista todos os workspaces do usuário logado |
| `GET` | `/workspaces/{workspaceId}` | Busca um workspace por ID |
| `PUT` | `/workspaces/{workspaceId}` | Atualiza um workspace |
| `DELETE` | `/workspaces/{workspaceId}` | Remove um workspace |

### Módulos (`/workspaces/{workspaceId}/modules`)
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/workspaces/{workspaceId}/modules` | Cria um módulo dentro do workspace |
| `GET` | `/workspaces/{workspaceId}/modules` | Lista os módulos do workspace |
| `GET` | `/workspaces/{workspaceId}/modules/{moduleId}` | Detalhes do módulo |
| `PUT` | `/workspaces/{workspaceId}/modules/{moduleId}` | Atualiza um módulo |
| `DELETE` | `/workspaces/{workspaceId}/modules/{moduleId}` | Deleta um módulo |

### Desafios (`/workspaces/{workspaceId}/modules/{moduleId}/challenges`)
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `.../challenges` | Cria um novo desafio |
| `GET` | `.../challenges` | Lista os desafios do módulo |
| `GET` | `.../challenges/{challengeId}` | Obtém detalhes do desafio |
| `POST` | `.../challenges/{challengeId}/answer` | Responde ao desafio e calcula o SRS |
| `PUT` | `.../challenges/{challengeId}` | Atualiza um desafio |
| `DELETE` | `.../challenges/{challengeId}` | Remove um desafio |

---

<p align="center">
  Desenvolvido por <a href="https://github.com/melquimartins">Melquisedeque Martins</a>
</p>
