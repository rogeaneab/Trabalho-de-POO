package com.sistemavendas.vendas.service.interfaces;

import com.sistemavendas.vendas.dto.response.ProdutoResponse;
import java.util.List;

public interface IProdutoConsultaService {
    List<ProdutoResponse> listarTodos();
    ProdutoResponse buscarPorId(Long id);
}