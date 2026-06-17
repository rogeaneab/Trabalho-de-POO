package com.sistemavendas.vendas.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProdutoRequest {

    @NotBlank(message = "O nome do produto é obrigatório.")
    private String nome;

    @NotNull(message = "O preço é obrigatório.")
    @Min(value = 0, message = "O preço não pode ser negativo.")
    private BigDecimal preco;

    private String categoria;

    @NotNull(message = "A quantidade inicial de estoque é obrigatória.")
    @Min(value = 0, message = "O estoque inicial não pode ser negativo.")
    private Integer quantidadeInicialEstoque; // Atende à validação e criação automática
}