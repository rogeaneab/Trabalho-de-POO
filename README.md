# 🛒 API de Vendas — Sistema REST com Spring Boot

> Projeto desenvolvido para a disciplina de **Orientação a Objetos Avançado** — Análise e Desenvolvimento de Sistemas · 4º Semestre.

---

## 📋 Sobre o Projeto

API RESTful para gerenciamento de um sistema de vendas de varejo, contemplando cadastro de clientes, gestão de produtos, controle de estoque e registro de vendas. A aplicação foi construída com **Java 17 + Spring Boot 3.x**, aplicando obrigatoriamente os cinco princípios **SOLID**.

---

## 🚀 Como Executar Localmente

### Pré-requisitos

- Java 17 ou superior
- Maven 3.8+

### Passos

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/api-vendas.git
cd api-vendas

# Execute o projeto
./mvnw spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

Documentação Swagger (se habilitada): `http://localhost:8080/swagger-ui.html`

---

## 🗂️ Endpoints Disponíveis

### Clientes
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/clientes` | Cadastrar novo cliente |
| GET | `/api/clientes` | Listar todos os clientes |
| GET | `/api/clientes/{id}` | Buscar cliente por ID |
| PUT | `/api/clientes/{id}` | Atualizar dados do cliente |
| DELETE | `/api/clientes/{id}` | Remover cliente |

### Produtos
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/produtos` | Cadastrar novo produto |
| GET | `/api/produtos` | Listar todos os produtos |
| GET | `/api/produtos/{id}` | Buscar produto por ID |
| PUT | `/api/produtos/{id}` | Atualizar dados do produto |
| DELETE | `/api/produtos/{id}` | Remover produto |

### Estoque
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/estoque` | Listar todos os estoques |
| GET | `/api/estoque/{produtoId}` | Consultar estoque de um produto |
| PUT | `/api/estoque/{produtoId}` | Atualizar quantidade em estoque |

### Vendas
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/vendas` | Registrar nova venda |
| GET | `/api/vendas` | Listar todas as vendas |
| GET | `/api/vendas/{id}` | Buscar venda por ID |
| GET | `/api/vendas/cliente/{clienteId}` | Listar vendas de um cliente |

---

## 🧱 Princípios SOLID Aplicados

### S — Single Responsibility Principle (Responsabilidade Única)
Cada classe possui uma única razão para mudar:
- `ClienteController`, `ProdutoController`, `EstoqueController`, `VendaController` — apenas recebem e respondem requisições HTTP.
- `ClienteServiceImpl`, `ProdutoServiceImpl`, `EstoqueServiceImpl`, `VendaServiceImpl` — apenas contêm a lógica de negócio.
- `ClienteRepository`, `ProdutoRepository`, `EstoqueRepository`, `VendaRepository` — apenas acessam o banco de dados.
- DTOs (`ClienteRequest`, `VendaResponse`, etc.) — apenas transportam dados entre camadas.
- `GlobalExceptionHandler` — centraliza exclusivamente o tratamento de erros.

### O — Open/Closed Principle (Aberto para Extensão, Fechado para Modificação)
A interface `CalculadoraVenda` permite adicionar novas regras de cálculo sem modificar código existente:
- `CalculadoraSemDesconto` — implementação padrão.
- `CalculadoraComDesconto10` — nova regra adicionada sem alterar a interface ou as classes existentes.

### L — Liskov Substitution Principle (Substituição de Liskov)
As subclasses `ProdutoPerecivel` e `ProdutoNaoPerecivel` estendem `Produto` e podem ser utilizadas em qualquer lugar onde um `Produto` é esperado, sem quebrar o comportamento do sistema.

### I — Interface Segregation Principle (Segregação de Interfaces)
Interfaces coesas e específicas por contexto:
- `IClienteService` — métodos pertinentes apenas à gestão de clientes.
- `IProdutoService` — métodos pertinentes apenas à gestão de produtos.
- `IEstoqueService` — métodos pertinentes apenas ao controle de estoque.
- `IVendaService` — métodos pertinentes apenas ao registro de vendas.

### D — Dependency Inversion Principle (Inversão de Dependência)
Módulos de alto nível dependem de abstrações, não de implementações:
- Controllers dependem das interfaces de Service (`IClienteService`, `IVendaService`, etc.).
- Services dependem das interfaces de Repository (Spring Data JPA).
- A injeção de dependência é gerenciada pelo Spring via `@RequiredArgsConstructor`.

---

## 👥 Integrantes e Contribuições

| Integrante | Responsabilidades |
|-----------|-------------------|
| **Rogeane Alves Bezerra** | Módulo de Vendas e Arquitetura geral do projeto — estruturação dos pacotes, definição das camadas, implementação do fluxo de registro de vendas com validação de estoque, cálculo do valor total e aplicação do princípio OCP via `CalculadoraVenda`. |
| **Victor Hugo Sales Paz** | Módulo de Clientes e camada de DTOs — implementação dos endpoints de cadastro, listagem, atualização e remoção de clientes, criação das classes de request e response, além dos mappers de conversão entre entidades e DTOs. |
| **Miguel Pereira de Sousa Neto** | Módulo de Produtos e Controle de Estoque — implementação dos endpoints de produtos (CRUD completo), gerenciamento do estoque com criação automática ao cadastrar produto, validação de estoque não negativo e decremento automático na venda. |
| **Emanuel Carvalho Belarmino** | Tratamento de erros, documentação e gestão do repositório — implementação do `GlobalExceptionHandler`, exceções customizadas (`EstoqueInsuficienteException`, `RecursoNaoEncontradoException`), configuração do Swagger UI via Springdoc OpenAPI, elaboração do README e organização do repositório Git. |

---

## ⚙️ Configuração — application.properties

```properties
# Banco de dados H2 em memória
spring.datasource.url=jdbc:h2:mem:vendas_db
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA / Hibernate
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# Console H2 (desenvolvimento)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

---

## 🗃️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.x**
- **Spring Data JPA / Hibernate**
- **Banco H2 (em memória)**
- **Lombok**
- **Springdoc OpenAPI (Swagger UI)**
- **Maven**
