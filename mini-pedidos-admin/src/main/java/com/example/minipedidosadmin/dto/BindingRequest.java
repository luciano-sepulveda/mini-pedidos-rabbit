package com.example.minipedidosadmin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BindingRequest(
        @NotBlank(message = "La cola no puede estar vacía") String queue,
        @NotBlank(message = "El exchange no puede estar vacío") String exchange,
        @NotNull(message = "La routingKey es obligatoria (puede ser texto vacío)") String routingKey) {
}