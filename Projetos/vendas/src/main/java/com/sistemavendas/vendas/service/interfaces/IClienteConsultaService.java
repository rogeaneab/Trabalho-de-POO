package com.sistemavendas.vendas.service.interfaces;

import com.sistemavendas.vendas.dto.response.ClienteResponse;
import java.util.List;

public interface IClienteConsultaService {
    List<ClienteResponse> listarTodos();
    ClienteResponse buscarPorId(Long id);
}