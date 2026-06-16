package com.sistemavendas.vendas.service;

import com.sistemavendas.vendas.model.Item;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;

@Component("desconto10")
public class CalculadoraComDesconto10 implements CalculadoraVenda {
    @Override
    public BigDecimal calcularTotal(List<Item> itens) {
        if (itens == null || itens.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal subtotal = itens.stream()
            .map(i -> i.getProduto().getPreco().multiply(BigDecimal.valueOf(i.getQuantidade())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        return subtotal.multiply(BigDecimal.valueOf(0.90));
    }
}