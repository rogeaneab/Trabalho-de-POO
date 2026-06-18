package com.sistemavendas.vendas.controller;

import com.sistemavendas.vendas.model.Venda;
import com.sistemavendas.vendas.service.interfaces.IVendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vendas")
@RequiredArgsConstructor
@Tag(name = "Vendas", description = "Endpoints para registro e consulta de vendas")
public class VendaController {

    private final IVendaService vendaService;

    @Operation(summary = "Registrar uma nova venda", description = "Registra uma venda e realiza o decremento automático no estoque.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Venda criada com sucesso",
                content = @Content(schema = @Schema(implementation = Venda.class))),
        @ApiResponse(responseCode = "400", description = "Estoque insuficiente para um ou mais produtos ou dados inválidos")
    })
    @PostMapping
    public ResponseEntity<Venda> criar(@RequestBody Venda venda) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vendaService.registrarVenda(venda));
    }

    @Operation(summary = "Listar todas as vendas", description = "Retorna uma lista com todas as vendas registradas no sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de vendas retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<Venda>> listarTodas() {
        return ResponseEntity.ok(vendaService.listarTodas());
    }

    @Operation(summary = "Buscar venda por ID", description = "Retorna os detalhes de uma venda específica com base no ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venda encontrada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Venda não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Venda> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vendaService.buscarPorId(id));
    }

    @Operation(summary = "Listar vendas de um cliente", description = "Retorna a lista de todas as vendas associadas a um determinado cliente.")
    @ApiResponse(responseCode = "200", description = "Lista de vendas do cliente retornada com sucesso")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Venda>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(vendaService.listarPorCliente(clienteId));
    }
}