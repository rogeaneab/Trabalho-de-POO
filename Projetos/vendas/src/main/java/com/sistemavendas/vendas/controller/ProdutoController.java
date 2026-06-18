package com.sistemavendas.vendas.controller;


import com.sistemavendas.vendas.dto.request.ProdutoRequest;
import com.sistemavendas.vendas.dto.response.ProdutoResponse;
import com.sistemavendas.vendas.service.interfaces.IProdutoCadastroService;
import com.sistemavendas.vendas.service.interfaces.IProdutoConsultaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "Endpoints para gestão de produtos")
public class ProdutoController {

    private final IProdutoCadastroService cadastroService;
    private final IProdutoConsultaService consultaService;

    @Operation(summary = "Cadastrar um novo Produto", description = "Cria um Produto no sistema. Nome e quantidade.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto adicionado com sucesso",
                    content = @Content(schema = @Schema(implementation = ProdutoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados de requisição inválidos (validação falhou)"),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada (Produto já existente)")
    })
    @PostMapping
    public ResponseEntity<ProdutoResponse> cadastrar(@Valid @RequestBody ProdutoRequest request) {
        ProdutoResponse response = cadastroService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar todos os produtos", description = "Retorna uma lista com todos os produtos cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listarTodos() {
        return ResponseEntity.ok(consultaService.listarTodos());
    }

    @Operation(summary = "Buscar produto por ID", description = "Retorna os detalhes de um produto específico com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.buscarPorId(id));
    }

    @Operation(summary = "Atualizar dados do produto", description = "Atualiza as informações de um produto existente com base no ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto updated com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de requisição inválidos"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @PutMapping("/{id}")
    // CORRIGIDO: De ProdutoeResponse para ProdutoResponse
    public ResponseEntity<ProdutoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequest request) {
        return ResponseEntity.ok(cadastroService.atualizar(id, request));
    }

    @Operation(summary = "Remover Produto", description = "Apaga o registro de um produto do sistema com base no ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto removido com sucesso (Sem conteúdo)"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        cadastroService.remover(id);
        return ResponseEntity.noContent().build();
    }
}