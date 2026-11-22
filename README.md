# Baozi Store – API REST

API REST simples para controle de **clientes**, **produtos** e **pedidos** da Baozi Store (loja de pão chinês).
Trabalho da disciplina Desenvolvimento Web Back-End.

**Aluna:** Melissa Mendes Coelho – RU 5598704

## Tecnologias
- Java 17
- Spring Boot (Spring Web MVC)
- Spring Data JPA / Hibernate
- Banco relacional H2 (arquivo local), com configuração alternativa para MySQL
- JSON em todos os endpoints
- Postman para os testes

## Estrutura (MVC)
```
src/main/java/com/baozistore
├── BaoziStoreApplication.java
├── model/        -> entidades JPA (Cliente, Produto, Pedido)
├── repository/   -> interfaces Spring Data JPA
└── controller/   -> endpoints REST
```

## Entidades

| Entidade | Campo | Tipo |
|---|---|---|
| Cliente | id | Long (PK, gerado) |
| | nome | String |
| | clienteDesde | LocalDate |
| Produto | id | Long (PK, gerado) |
| | nome | String |
| | preco | BigDecimal |
| | estoque | Boolean |
| Pedido | id | Long (PK, gerado) |
| | cliente (coluna `cliente_id`) | FK → Cliente |
| | produto (coluna `produto_id`) | FK → Produto |
| | quantidade | Integer |

Um cliente faz vários pedidos (1:N) e um produto pode estar em vários pedidos (1:N).

## Endpoints

| Método | URL | Descrição | Respostas |
|---|---|---|---|
| POST | /clientes | Cria um cliente | 201 |
| GET | /clientes | Lista todos os clientes | 200 |
| GET | /clientes/{id} | Consulta cliente por ID | 200 / 404 |
| PUT | /clientes/{id} | Atualiza cliente (opcional) | 200 / 404 |
| DELETE | /clientes/{id} | Apaga cliente | 204 / 404 / 409 se tiver pedidos |
| POST | /produtos | Cria um produto | 201 |
| GET | /produtos | Lista todos os produtos | 200 |
| GET | /produtos/{id} | Consulta produto por ID | 200 / 404 |
| PUT | /produtos/{id} | Atualiza produto (opcional) | 200 / 404 |
| DELETE | /produtos/{id} | Apaga produto | 204 / 404 / 409 se tiver pedidos |
| POST | /pedidos | Cria um pedido | 201 / 400 |
| GET | /pedidos | Lista todos os pedidos | 200 |
| GET | /pedidos/{id} | Consulta pedido por ID | 200 / 404 |
| PUT | /pedidos/{id} | Atualiza pedido (opcional) | 200 / 400 / 404 |
| DELETE | /pedidos/{id} | Apaga pedido | 204 / 404 |

### Exemplos de corpo (JSON)
```json
POST /clientes
{ "nome": "Melissa5598704", "clienteDesde": "2026-11-22" }

POST /produtos
{ "nome": "Baozi de Porco", "preco": 6.50, "estoque": true }

POST /pedidos
{ "clienteId": 1, "produtoId": 1, "quantidade": 10 }
```

## Como executar
```bash
# Windows
mvnw.cmd spring-boot:run
# Linux/Mac
./mvnw spring-boot:run
```
A API sobe em `http://localhost:8080`.
O console do H2 fica em `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:file:./data/baozistore`, usuário `sa`, sem senha).

Para usar MySQL, comente o bloco H2 e descomente o bloco MySQL em `src/main/resources/application.properties`.

## Postman
Importe `postman/BaoziStore.postman_collection.json` no Postman.
