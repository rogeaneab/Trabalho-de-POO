package com.sistemavendas.vendas.repository;

import com.sistemavendas.vendas.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    boolean existsByNome(String nome);
}