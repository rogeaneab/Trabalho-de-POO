package com.sistemavendas.vendas.service;

import com.sistemavendas.vendas.dto.request.ProdutoRequest;
import com.sistemavendas.vendas.dto.response.ProdutoResponse;
import com.sistemavendas.vendas.model.Produto;
import com.sistemavendas.vendas.repository.ProdutoRepository;
import com.sistemavendas.vendas.service.interfaces.IProdutoCadastroService;
import com.sistemavendas.vendas.service.interfaces.IProdutoConsultaService;
import com.sistemavendas.vendas.service.interfaces.IEstoqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements IProdutoCadastroService, IProdutoConsultaService, IEstoqueService {

    private final ProdutoRepository produtoRepository;

    @Override
    @Transactional
    public ProdutoResponse cadastrar(ProdutoRequest request) {
        if (produtoRepository.existsByNome(request.getNome())) {
            throw new IllegalArgumentException("Produto já cadastrado com este nome.");
        }

        // Gerenciamento e criação automática de estoque ao salvar o produto
        Produto produto = Produto.builder()
                .nome(request.getNome())
                .preco(request.getPreco())
                .categoria(request.getCategoria())
                .quantidadeEstoque(request.getQuantidadeInicialEstoque() != null ? request.getQuantidadeInicialEstoque() : 0)
                .build();

        Produto salvo = produtoRepository.save(produto);
        return mapToResponse(salvo);
    }

    @Override
    @Transactional
    public ProdutoResponse atualizar(Long id, ProdutoRequest request) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));

        produto.setNome(request.getNome());
        produto.setPreco(request.getPreco());
        produto.setCategoria(request.getCategoria());

        // Mantém ou atualiza a quantidade base se necessário
        if (request.getQuantidadeInicialEstoque() != null) {
            if (request.getQuantidadeInicialEstoque() < 0) {
                throw new IllegalArgumentException("O estoque não pode ser negativo.");
            }
            produto.setQuantidadeEstoque(request.getQuantidadeInicialEstoque());
        }

        Produto atualizado = produtoRepository.save(produto);
        return mapToResponse(atualizado);
    }

    @Override
    @Transactional
    public void remover(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new RuntimeException("Produto não encontrado com ID: " + id);
        }
        produtoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponse> listarTodos() {
        return produtoRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProdutoResponse buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));
        return mapToResponse(produto);
    }

    /**
     * Implementação real da interface IEstoqueService.
     * Decrementa automaticamente as unidades do estoque durante o fluxo de venda.
     */
    @Override
    @Transactional
    public void decrementarEstoque(Long produtoId, Integer quantidade) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto ID " + produtoId + " não existe no estoque."));

        int novoEstoque = produto.getQuantidadeEstoque() - quantidade;

        // Validação crucial de estoque não negativo
        if (novoEstoque < 0) {
            throw new IllegalArgumentException("Saldo insuficiente em estoque para o produto: " + produto.getNome()
                    + " (Disponível: " + produto.getQuantidadeEstoque() + ", Solicitado: " + quantidade + ")");
        }

        produto.setQuantidadeEstoque(novoEstoque);
        produtoRepository.save(produto); // Persiste a baixa
    }

    // Mapper auxiliar (SRP)
    private ProdutoResponse mapToResponse(Produto produto) {
        return ProdutoResponse.builder()
                .id(produto.getId())
                .nome(produto.getNome())
                .preco(produto.getPreco())
                .categoria(produto.getCategoria())
                .quantidadeEstoque(produto.getQuantidadeEstoque())
                .build();
    }
}