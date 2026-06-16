package com.sistemavendas.vendas.service;

import com.sistemavendas.vendas.model.Item;
import java.math.BigDecimal;
import java.util.List;

public interface CalculadoraVenda {
    BigDecimal calcularTotal(List<Item> itens);
}