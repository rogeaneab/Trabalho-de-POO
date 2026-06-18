# 🛒 API de Vendas — Sistema REST com Spring Boot

> Projeto desenvolvido para a disciplina de **Orientação a Objetos Avançado** — Análise e Desenvolvimento de Sistemas · 4º Semestre.

---

## 📋 Sobre o Projeto

API RESTful para gerenciamento de um sistema de vendas de varejo, contemplando cadastro de clientes, gestão de produtos (com controle de estoque embutido) e registro de vendas com cálculo automático de valor total. A aplicação foi construída com **Java 17 + Spring Boot 3.5.15**, aplicando os cinco princípios **SOLID** na organização das camadas de Controller, Service e Repository.

---

## 🚀 Como Executar Localmente

### Pré-requisitos

- **Java 17** ou superior (JDK)
- **Maven 3.8+** (ou utilize o Maven Wrapper incluído no projeto — não exige instalação local)
- **IDE recomendada:** IntelliJ IDEA (Community ou Ultimate) — o projeto já inclui os arquivos de configuração do IntelliJ (pasta `.idea`), facilitando a importação direta como projeto Maven.

### Passos

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/Trabalho-de-POO.git
cd Trabalho-de-POO/Projetos/vendas

# Execute o projeto com o Maven Wrapper (Linux/Mac)
./mvnw spring-boot:run

# Ou, no Windows
mvnw.cmd spring-boot:run
```

### Executando pelo IntelliJ IDEA

1. Abra o IntelliJ IDEA e selecione **Open** (ou **File → Open**).
2. Aponte para a pasta `Projetos/vendas` (onde está o `pom.xml`).
3. Aguarde o IntelliJ importar as dependências Maven automaticamente.
4. Localize a classe `VendasApplication.java` (pacote `com.sistemavendas.vendas`).
5. Clique no ícone de execução (▶) ao lado do método `main` ou utilize o atalho `Shift + F10`.

A aplicação estará disponível em: `http://localhost:8080`

Documentação interativa Swagger UI: `http://localhost:8080/swagger-ui.html`

Console do banco H2 (desenvolvimento): `http://localhost:8080/h2-console`

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

**Exemplo — Cadastrar cliente:**
```http
POST /api/clientes
Content-Type: application/json

{
  "nome": "Maria Oliveira",
  "email": "maria.oliveira@email.com",
  "cpf": "123.456.789-00",
  "telefone": "(85) 99999-1234"
}
```

### Produtos (com controle de estoque embutido)
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/produtos` | Cadastrar novo produto (com quantidade inicial em estoque) |
| GET | `/api/produtos` | Listar todos os produtos |
| GET | `/api/produtos/{id}` | Buscar produto por ID |
| PUT | `/api/produtos/{id}` | Atualizar dados do produto |
| DELETE | `/api/produtos/{id}` | Remover produto |

**Exemplo — Cadastrar produto:**
```http
POST /api/produtos
Content-Type: application/json

{
  "nome": "Notebook Dell Inspiron",
  "preco": 3499.90,
  "categoria": "ELETRONICO",
  "quantidadeInicialEstoque": 50
}
```

> ℹ️ **Nota sobre o estoque:** nesta implementação, o controle de estoque não é um módulo/endpoint separado. A quantidade em estoque é um atributo do próprio `Produto` (`quantidadeEstoque`), exposto na resposta do cadastro/consulta de produtos. O decremento do estoque ocorre automaticamente durante o registro de uma venda, através da interface `IEstoqueService`, implementada por `ProdutoServiceImpl`.

### Vendas
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/vendas` | Registrar nova venda (com validação e baixa de estoque) |
| GET | `/api/vendas` | Listar todas as vendas |
| GET | `/api/vendas/{id}` | Buscar venda por ID |
| GET | `/api/vendas/cliente/{clienteId}` | Listar vendas de um cliente |

**Exemplo — Registrar venda:**
```http
POST /api/vendas
Content-Type: application/json

{
  "cliente": { "id": 1 },
  "itens": [
    { "produto": { "id": 1 }, "quantidade": 2 },
    { "produto": { "id": 3 }, "quantidade": 1 }
  ]
}
```

**Exemplo — Resposta de erro (estoque insuficiente, HTTP 400):**
```json
{
  "timestamp": "2026-06-18T10:30:00",
  "status": 400,
  "error": "Estoque insuficiente",
  "message": "Saldo insuficiente em estoque para o produto: Notebook Dell Inspiron (Disponível: 1, Solicitado: 2)",
  "path": "/api/vendas",
  "details": null
}
```

---

## 🧱 Princípios SOLID Aplicados

### S — Single Responsibility Principle (Responsabilidade Única)
Cada classe possui uma única razão para mudar:
- `ClienteController`, `ProdutoController`, `VendaController` — apenas recebem e respondem requisições HTTP, delegando toda a lógica às camadas de serviço.
- `ClienteServiceImpl`, `ProdutoServiceImpl`, `VendaServiceImpl` — concentram exclusivamente a lógica de negócio de cada domínio.
- `ClienteRepository`, `ProdutoRepository`, `VendaRepository` — responsáveis apenas pelo acesso a dados via Spring Data JPA.
- DTOs (`ClienteRequest`, `ProdutoRequest`, `ClienteResponse`, `ProdutoResponse`) — apenas transportam dados entre as camadas, sem lógica de negócio.
- `GlobalExceptionHandler` — centraliza exclusivamente o tratamento de erros e a padronização das respostas de exceção (`ErrorDetails`).

### O — Open/Closed Principle (Aberto para Extensão, Fechado para Modificação)
A interface `CalculadoraVenda` permite adicionar novas regras de cálculo do valor total sem alterar código já existente:
- `CalculadoraSemDesconto` (qualifier `"semDesconto"`) — implementação padrão, soma simples de preço × quantidade.
- `CalculadoraComDesconto10` (qualifier `"desconto10"`) — nova regra de negócio (desconto de 10%), adicionada como um novo `@Component` sem modificar a interface, o `VendaServiceImpl` ou a calculadora padrão.

Para trocar a regra de cálculo utilizada no fluxo de vendas, basta alterar o `@Qualifier` injetado em `VendaServiceImpl`, sem tocar nas implementações.

### L — Liskov Substitution Principle (Substituição de Liskov)
O sistema depende das abstrações `CalculadoraVenda` e `IEstoqueService` em vez de implementações concretas. Qualquer classe que implemente essas interfaces — como `CalculadoraSemDesconto`/`CalculadoraComDesconto10`, ou `ProdutoServiceImpl`/`EstoqueServiceMock` no caso de `IEstoqueService` — pode ser injetada no lugar da outra sem alterar o comportamento esperado por `VendaServiceImpl`, que depende apenas do contrato da interface.

### I — Interface Segregation Principle (Segregação de Interfaces)
Em vez de uma única interface "gorda" por domínio, os serviços de Cliente e Produto foram segregados por responsabilidade:
- `IClienteCadastroService` — apenas operações de escrita (cadastrar, atualizar, remover).
- `IClienteConsultaService` — apenas operações de leitura (listar, buscar por ID).
- `IProdutoCadastroService` — apenas operações de escrita de produtos.
- `IProdutoConsultaService` — apenas operações de leitura de produtos.
- `IEstoqueService` — interface mínima e coesa, com um único método (`decrementarEstoque`), consumida exclusivamente pelo fluxo de vendas.
- `IVendaService` — métodos pertinentes apenas ao registro e consulta de vendas.

Cada controller injeta somente as interfaces que efetivamente utiliza (ex.: `ClienteController` depende de `IClienteCadastroService` e `IClienteConsultaService`, nunca de uma interface única com todos os métodos).

### D — Dependency Inversion Principle (Inversão de Dependência)
Módulos de alto nível dependem de abstrações, não de implementações concretas:
- Os Controllers dependem das interfaces de Service (`IClienteCadastroService`, `IProdutoConsultaService`, `IVendaService`, etc.), nunca das classes `*ServiceImpl` diretamente.
- `VendaServiceImpl` depende da abstração `IEstoqueService` (e não diretamente de `ProdutoServiceImpl`), e da abstração `CalculadoraVenda` (e não de uma implementação concreta de cálculo).
- Os Services dependem das interfaces de Repository do Spring Data JPA (`JpaRepository`), nunca de implementações de acesso a dados feitas à mão.
- A injeção de dependência é gerenciada pelo Spring, via `@RequiredArgsConstructor` (Lombok) na maioria das classes e via construtor explícito com `@Qualifier` em `VendaServiceImpl`, para escolher qual implementação de `CalculadoraVenda` deve ser utilizada.

---

## 👥 Integrantes e Contribuições

| Integrante | Responsabilidades |
|-----------|-------------------|
| **Rogeane Alves Bezerra** | Módulo de Vendas e arquitetura geral do projeto — estruturação dos pacotes, definição das camadas, implementação do fluxo de registro de vendas (`VendaServiceImpl`) com validação e baixa de estoque, cálculo do valor total e aplicação do princípio OCP via `CalculadoraVenda`. |
| **Victor Hugo Sales Paz** | Módulo de Clientes e camada de DTOs — implementação dos endpoints de cadastro, listagem, atualização e remoção de clientes (`ClienteController`, `ClienteServiceImpl`), criação das classes `ClienteRequest`/`ClienteResponse` e dos mappers de conversão entre entidade e DTO. |
| **Miguel Pereira de Sousa Neto** | Módulo de Produtos e controle de estoque — implementação do CRUD completo de produtos (`ProdutoController`, `ProdutoServiceImpl`), gerenciamento do estoque embutido na entidade `Produto` com criação automática da quantidade inicial ao cadastrar, validação de estoque não negativo e decremento automático via `IEstoqueService` no momento da venda. |
| **Emanuel Carvalho Belarmino** | Tratamento de erros, documentação e gestão do repositório — implementação do `GlobalExceptionHandler` e do registro padronizado `ErrorDetails`, exceções customizadas (`EstoqueInsuficienteException`, `RecursoNaoEncontradoException`), elaboração deste README e organização do repositório Git. |

---

## ⚙️ Configuração — application.properties

```properties
# Configuração do Banco de Dados H2
spring.datasource.url=jdbc:h2:mem:vendasdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# Habilita o Console do H2 no navegador para ver as tabelas
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Mostra os comandos SQL que o Spring faz no terminal (ótimo para a apresentação)
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Atualiza a estrutura do banco automaticamente
spring.jpa.hibernate.ddl-auto=update

# Configurações do Springdoc OpenAPI (Swagger UI)
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

---

## 🗃️ Tecnologias e Ferramentas Utilizadas

- **Java 17**
- **Spring Boot 3.5.15** (`spring-boot-starter-parent`)
- **Spring Web** (`spring-boot-starter-web`) — exposição dos endpoints REST
- **Spring Data JPA / Hibernate** (`spring-boot-starter-data-jpa`) — persistência e mapeamento objeto-relacional
- **Spring Validation** (`spring-boot-starter-validation`) — validação dos DTOs de entrada (`@NotBlank`, `@Email`, `@Pattern`, `@Min`, `@NotNull`)
- **Banco de dados H2** (em memória, runtime) — simplicidade para desenvolvimento e testes
- **Lombok** — redução de boilerplate (`@Getter`, `@Setter`, `@Builder`, `@RequiredArgsConstructor`, `@Data`)
- **Springdoc OpenAPI / Swagger UI** (`springdoc-openapi-starter-webmvc-ui` 2.5.0) — documentação interativa da API
- **Maven** (com Maven Wrapper incluído — `mvnw` / `mvnw.cmd`) — build e gerenciamento de dependências
- **IntelliJ IDEA** — ambiente de desenvolvimento (IDE) utilizado pela equipe para escrever, depurar e executar o projeto. O repositório inclui os arquivos de configuração do IntelliJ (`.idea/`), permitindo a importação direta do projeto Maven sem configuração manual adicional.
- **Git / GitHub** — controle de versão e hospedagem do repositório

---

## 🗂️ Estrutura de Pacotes

```
src/main/java/com/sistemavendas/vendas/
├── controller/
│   ├── ClienteController.java
│   ├── ProdutoController.java
│   └── VendaController.java
├── service/
│   ├── interfaces/
│   │   ├── IClienteCadastroService.java
│   │   ├── IClienteConsultaService.java
│   │   ├── IProdutoCadastroService.java
│   │   ├── IProdutoConsultaService.java
│   │   ├── IEstoqueService.java
│   │   └── IVendaService.java
│   ├── ClienteServiceImpl.java
│   ├── ProdutoServiceImpl.java
│   ├── EstoqueServiceMock.java
│   ├── VendaServiceImpl.java
│   ├── CalculadoraVenda.java
│   ├── CalculadoraSemDesconto.java
│   └── CalculadoraComDesconto10.java
├── repository/
│   ├── ClienteRepository.java
│   ├── ProdutoRepository.java
│   └── VendaRepository.java
├── model/
│   ├── Cliente.java
│   ├── Produto.java
│   ├── Venda.java
│   └── Item.java
├── dto/
│   ├── request/
│   │   ├── ClienteRequest.java
│   │   └── ProdutoRequest.java
│   └── response/
│       ├── ClienteResponse.java
│       └── ProdutoResponse.java
├── exception/
│   ├── EstoqueInsuficienteException.java
│   ├── RecursoNaoEncontradoException.java
│   ├── ErrorDetails.java
│   └── GlobalExceptionHandler.java
├── config/
│   └── OpenApiConfig.java
└── VendasApplication.java
```

---

## 📝 Decisões do projeto

- **Estoque como atributo de Produto:** ao invés de criar uma entidade `Estoque` e um `EstoqueController` separados, optou-se por manter `quantidadeEstoque` como atributo direto da entidade `Produto`. Essa decisão simplifica o modelo de dados, já que, nas regras de negócio definidas, cada produto possui exatamente um saldo de estoque (relação 1:1 trivial), eliminando a necessidade de uma tabela e endpoints adicionais apenas para repassar o mesmo dado.
- **`IEstoqueService` implementada por `ProdutoServiceImpl`:** a interface foi mantida separada (ISP) mesmo sem um controller próprio, pois sua responsabilidade (decrementar saldo) é estritamente diferente de cadastro/consulta de produtos. Isso permite, inclusive, trocar a implementação real por um mock (`EstoqueServiceMock`, atualmente desativado) em cenários de teste, sem alterar `VendaServiceImpl` — demonstrando LSP e DIP na prática.
- **Cálculo do total da venda via `@Qualifier`:** `VendaServiceImpl` recebe a implementação de `CalculadoraVenda` por injeção qualificada (`@Qualifier("semDesconto")`), permitindo alternar a regra de cálculo (com ou sem desconto) trocando apenas o qualifier, sem alterar a lógica de registro de venda.
- **Validação de estoque não negativo:** centralizada em `ProdutoServiceImpl.decrementarEstoque`, lançando `EstoqueInsuficienteException` antes de qualquer persistência, garantindo que uma venda com itens sem saldo suficiente seja integralmente rejeitada.
- **Tratamento de erros padronizado:** todas as exceções de negócio (`RecursoNaoEncontradoException`, `EstoqueInsuficienteException`, `IllegalArgumentException`) e de validação (`MethodArgumentNotValidException`) são capturadas pelo `GlobalExceptionHandler` e convertidas em um corpo de resposta único (`ErrorDetails`), com timestamp, status HTTP, mensagem e caminho da requisição.
