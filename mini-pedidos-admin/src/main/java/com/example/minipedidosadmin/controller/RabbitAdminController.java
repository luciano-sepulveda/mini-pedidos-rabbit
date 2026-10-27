package com.example.minipedidosadmin.controller;

import com.example.minipedidosadmin.dto.BindingRequest;
import com.example.minipedidosadmin.dto.ExchangeRequest;
import com.example.minipedidosadmin.dto.QueueRequest;
import com.example.minipedidosadmin.service.RabbitAdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class RabbitAdminController {

    private final RabbitAdminService service;

    public RabbitAdminController(RabbitAdminService service) {
        this.service = service;
    }

    @PostMapping("/queues")
    @ResponseStatus(HttpStatus.CREATED)
    public String crearCola(@Valid @RequestBody QueueRequest request) {
        service.crearCola(request);
        return "Cola creada: " + request.name();
    }

    @DeleteMapping("/queues/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarCola(@PathVariable String name) {
        service.eliminarCola(name);
    }

    @PostMapping("/exchanges")
    @ResponseStatus(HttpStatus.CREATED)
    public String crearExchange(@Valid @RequestBody ExchangeRequest request) {
        service.crearExchange(request);
        return "Exchange creado: " + request.name();
    }

    @DeleteMapping("/exchanges/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarExchange(@PathVariable String name) {
        service.eliminarExchange(name);
    }

    @PostMapping("/bindings")
    @ResponseStatus(HttpStatus.CREATED)
    public String crearBinding(@Valid @RequestBody BindingRequest request) {
        service.crearBinding(request);
        return "Binding creado: " + request.exchange() + " -> " + request.queue();
    }

    @DeleteMapping("/bindings")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarBinding(@RequestParam String queue,
                                @RequestParam String exchange,
                                @RequestParam String routingKey) {
        service.eliminarBinding(queue, exchange, routingKey);
    }
}