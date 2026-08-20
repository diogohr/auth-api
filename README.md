# Auth API - Spring Boot & Docker

API de Autenticação e Gerenciamento de Usuários desenvolvida com Spring Boot, Spring Security (JWT), PostgreSQL e Docker. O projeto foi estruturado seguindo boas práticas de desenvolvimento backend e padrões de mercado.

---

## Tecnologias Utilizadas

* Java 21
* Spring Boot 3.2.4
* Spring Security & JWT (Autenticação Stateless)
* Spring Data JPA / Hibernate (Persistência de dados)
* PostgreSQL (Banco de dados relacional)
* Docker & Docker Compose (Containerização)
* JUnit 5 (Testes unitários)

---

## Funcionalidades

* **Cadastro de Usuários:** Registro com criptografia de senha (BCrypt).
* **Autenticação (Login):** Geração e validação de token JWT.
* **Recuperação de Senha:** Fluxo de "Esqueci a senha" com geração de token seguro e simulação de envio.
* **Reset de Senha:** Atualização de credenciais via token válido.
* **Testes Automatizados:** Suíte de testes unitários validando a regra de negócio do JWT.

---

## Como Rodar o Projeto com Docker

Certifique-se de ter o Docker e o Docker Compose instalados na sua máquina.

1. Clone o repositório:
   ```bash
   git clone [https://github.com/diogohr/auth-api.git](https://github.com/SEU-USUARIO/auth-api.git)
   cd auth-api