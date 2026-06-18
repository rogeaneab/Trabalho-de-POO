package com.sistemavendas.vendas.controller;

import com.sistemavendas.vendas.dto.request.ClienteRequest;
import com.sistemavendas.vendas.dto.response.ClienteResponse;
import com.sistemavendas.vendas.service.interfaces.IClienteCadastroService;
import com.sistemavendas.vendas.service.interfaces.IClienteConsultaService;
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
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Endpoints para gestão de clientes")
public class ClienteController {

    private final IClienteCadastroService cadastroService;
    private final IClienteConsultaService consultaService;

    @Operation(summary = "Cadastrar um novo cliente", description = "Cria um cliente no sistema. E-mail e CPF devem ser únicos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso",
                content = @Content(schema = @Schema(implementation = ClienteResponse.class))),
        @ApiResponse(responseCode = "400", description = "Dados de requisição inválidos (validação falhou)"),
        @ApiResponse(responseCode = "422", description = "Regra de negócio violada (E-mail ou CPF já existentes)")
    })
    @PostMapping
    public ResponseEntity<ClienteResponse> cadastrar(@Valid @RequestBody ClienteRequest request) {
        ClienteResponse response = cadastroService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar todos os clientes", description = "Retorna uma lista com todos os clientes cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listarTodos() {
        return ResponseEntity.ok(consultaService.listarTodos());
    }

    @Operation(summary = "Buscar cliente por ID", description = "Retorna os detalhes de um cliente específico com base no ID fornecido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.buscarPorId(id));
    }

    @Operation(summary = "Atualizar dados do cliente", description = "Atualiza as informações de um cliente existente com base no ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados de requisição inválidos"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> atualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.ok(cadastroService.atualizar(id, request));
    }

    @Operation(summary = "Remover cliente", description = "Apaga o registo de um cliente do sistema com base no ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cliente removido com sucesso (Sem conteúdo)"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        cadastroService.remover(id);
        return ResponseEntity.noContent().build();
    }
}