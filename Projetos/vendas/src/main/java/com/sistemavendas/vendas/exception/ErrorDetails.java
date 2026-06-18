package com.sistemavendas.vendas.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorDetails(
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path,
    List<String> details
) {}
