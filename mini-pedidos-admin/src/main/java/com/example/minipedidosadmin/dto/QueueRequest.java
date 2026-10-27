package com.example.minipedidosadmin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record QueueRequest(
        @NotBlank(message = "El nombre de la cola no puede estar vacio")
        @Pattern(regexp = "^[A-Za-z0-9._-]+$",
                 message = "El nombre solo puede tener letras, numeros, punto, guion y guion bajo")
        String name,
        Boolean durable) {
}