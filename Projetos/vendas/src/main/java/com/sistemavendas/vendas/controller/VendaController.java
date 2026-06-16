package com.sistemavendas.vendas.controller;

import com.sistemavendas.vendas.model.Venda;
import com.sistemavendas.vendas.service.interfaces.IVendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vendas")
@RequiredArgsConstructor
public class VendaController {

    private final IVendaService vendaService;

    @PostMapping
    public ResponseEntity<Venda> criar(@RequestBody Venda venda) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vendaService.registrarVenda(venda));
    }

    @GetMapping
    public ResponseEntity<List<Venda>> listarTodas() {
        return ResponseEntity.ok(vendaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venda> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vendaService.buscarPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Venda>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(vendaService.listarPorCliente(clienteId));
    }
}