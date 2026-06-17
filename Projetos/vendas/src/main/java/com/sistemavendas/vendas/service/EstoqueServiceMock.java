package com.sistemavendas.vendas.service;

import com.sistemavendas.vendas.service.interfaces.IEstoqueService;
import org.springframework.stereotype.Service;
//@Service // Isso avisa ao Spring que esse componente existe e resolve o erro de inicialização!"
public class EstoqueServiceMock implements IEstoqueService {
    
    @Override
    public void decrementarEstoque(Long produtoId, Integer quantidade) {
        // Por enquanto não faz nada, apenas simula que deu baixa com sucesso
        System.out.println("Mock Estoque: Baixando " + quantidade + " unidades do produto ID " + produtoId);
    }
}