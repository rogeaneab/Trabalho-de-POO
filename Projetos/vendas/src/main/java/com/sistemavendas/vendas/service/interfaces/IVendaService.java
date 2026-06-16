package com.sistemavendas.vendas.service.interfaces;

import com.sistemavendas.vendas.model.Venda;
import java.util.List;

public interface IVendaService {
    Venda registrarVenda(Venda venda);
    List<Venda> listarTodas();
    Venda buscarPorId(Long id);
    List<Venda> listarPorCliente(Long clienteId);
}