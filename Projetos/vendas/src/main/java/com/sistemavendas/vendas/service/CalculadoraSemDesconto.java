package com.sistemavendas.vendas.service;

import com.sistemavendas.vendas.model.Item;
import org.springframework.stereotype.Component; // Verifique se tem esse import
import java.math.BigDecimal;
import java.util.List;

@Component("semDesconto") // <-- ESSA LINHA É OBRIGATÓRIA
public class CalculadoraSemDesconto implements CalculadoraVenda {
    @Override
    public BigDecimal calcularTotal(List<Item> itens) {
        if (itens == null || itens.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return itens.stream()
            .map(i -> i.getProduto().getPreco().multiply(BigDecimal.valueOf(i.getQuantidade())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}