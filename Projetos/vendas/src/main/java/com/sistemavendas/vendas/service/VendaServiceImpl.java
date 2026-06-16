package com.sistemavendas.vendas.service;

import com.sistemavendas.vendas.model.Venda;
import com.sistemavendas.vendas.model.Item;
import com.sistemavendas.vendas.repository.VendaRepository;
import com.sistemavendas.vendas.service.interfaces.IVendaService;
import com.sistemavendas.vendas.service.interfaces.IEstoqueService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VendaServiceImpl implements IVendaService {

    private final VendaRepository vendaRepository;
    private final IEstoqueService estoqueService;
    private final CalculadoraVenda calculadoraVenda;

    // Construtor explícito para evitar qualquer confusão de compilação do Lombok
    public VendaServiceImpl(
            VendaRepository vendaRepository, 
            IEstoqueService estoqueService,
            @Qualifier("semDesconto") CalculadoraVenda calculadoraVenda) {
        this.vendaRepository = vendaRepository;
        this.estoqueService = estoqueService;
        this.calculadoraVenda = calculadoraVenda;
    }

    @Override
    @Transactional
    public Venda registrarVenda(Venda venda) {
        for (Item item : venda.getItens()) {
            estoqueService.decrementarEstoque(item.getProduto().getId(), item.getQuantidade());
        }
        venda.setValorTotal(calculadoraVenda.calcularTotal(venda.getItens()));
        venda.setDataVenda(LocalDateTime.now());
        return vendaRepository.save(venda);
    }

    @Override
    public List<Venda> listarTodas() { 
        return vendaRepository.findAll(); 
    }

    @Override
    public Venda buscarPorId(Long id) {
        return vendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada."));
    }

    @Override
    public List<Venda> listarPorCliente(Long clienteId) {
        return vendaRepository.findByClienteId(clienteId);
    }
}