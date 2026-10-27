package com.example.minipedidosadmin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ExchangeRequest(
        @NotBlank(message = "El nombre del exchange no puede estar vacío")
        @Pattern(regexp = "^[A-Za-z0-9._-]+$",
                 message = "El nombre solo puede tener letras, números, punto, guion y guion bajo")
        String name,
        @NotBlank(message = "El tipo es obligatorio")
        @Pattern(regexp = "direct|topic|fanout",
                 message = "El tipo debe ser direct, topic o fanout")
        String type,
        Boolean durable) {
}