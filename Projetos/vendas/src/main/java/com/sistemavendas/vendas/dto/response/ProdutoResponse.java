package com.sistemavendas.vendas.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class ProdutoResponse {
    private Long id;
    private String nome;
    private BigDecimal preco;
    private String categoria;
    private Integer quantidadeEstoque;
}