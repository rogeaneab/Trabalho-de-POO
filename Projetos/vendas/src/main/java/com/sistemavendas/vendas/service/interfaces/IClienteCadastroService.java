package com.sistemavendas.vendas.service.interfaces;

import com.sistemavendas.vendas.dto.request.ClienteRequest;
import com.sistemavendas.vendas.dto.response.ClienteResponse;

public interface IClienteCadastroService {
    ClienteResponse cadastrar(ClienteRequest request);
    ClienteResponse atualizar(Long id, ClienteRequest request);
    void remover(Long id);
}