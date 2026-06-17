package com.sistemavendas.vendas.service.interfaces;

import com.sistemavendas.vendas.dto.request.ProdutoRequest;
import com.sistemavendas.vendas.dto.response.ProdutoResponse;

public interface IProdutoCadastroService {
    // Altere de ClienteRequest para ProdutoRequest aqui:
    ProdutoResponse cadastrar(ProdutoRequest request);

    ProdutoResponse atualizar(Long id, ProdutoRequest request);
    void remover(Long id);
}