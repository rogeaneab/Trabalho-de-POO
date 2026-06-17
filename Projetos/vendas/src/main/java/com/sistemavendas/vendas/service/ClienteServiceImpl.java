package com.sistemavendas.vendas.service;

import com.sistemavendas.vendas.dto.request.ClienteRequest;
import com.sistemavendas.vendas.dto.response.ClienteResponse;
import com.sistemavendas.vendas.model.Cliente;
import com.sistemavendas.vendas.repository.ClienteRepository;
import com.sistemavendas.vendas.service.interfaces.IClienteCadastroService;
import com.sistemavendas.vendas.service.interfaces.IClienteConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Aplica o DIP injetando o repositório por construtor
public class ClienteServiceImpl implements IClienteCadastroService, IClienteConsultaService {

    private final ClienteRepository clienteRepository;

    @Override
    @Transactional
    public ClienteResponse cadastrar(ClienteRequest request) {
        if (clienteRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }
        if (clienteRepository.existsByCpf(request.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }

        Cliente cliente = Cliente.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .cpf(request.getCpf())
                .telefone(request.getTelefone())
                .build();

        Cliente salvo = clienteRepository.save(cliente);
        return mapToResponse(salvo);
    }

    @Override
    @Transactional
    public ClienteResponse atualizar(Long id, ClienteRequest request) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + id));

        cliente.setNome(request.getNome());
        cliente.setEmail(request.getEmail());
        cliente.setCpf(request.getCpf());
        cliente.setTelefone(request.getTelefone());

        Cliente atualizado = clienteRepository.save(cliente);
        return mapToResponse(atualizado);
    }

    @Override
    @Transactional
    public void remover(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente não encontrado com ID: " + id);
        }
        clienteRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponse> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + id));
        return mapToResponse(cliente);
    }

    // Método mapeador (SRP - Mappers)
    private ClienteResponse mapToResponse(Cliente cliente) {
        return ClienteResponse.builder()
                .id(cliente.getId())
                .nome(cliente.getNome())
                .email(cliente.getEmail())
                .cpf(cliente.getCpf())
                .telefone(cliente.getTelefone())
                .dataCadastro(cliente.getDataCadastro())
                .build();
    }
}