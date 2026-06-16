package com.sistemavendas.vendas.service.interfaces;

public interface IEstoqueService {
    void decrementarEstoque(Long produtoId, Integer quantidade);
}