package com.example.minipedidosadmin.service;

import com.example.minipedidosadmin.dto.BindingRequest;
import com.example.minipedidosadmin.dto.ExchangeRequest;
import com.example.minipedidosadmin.dto.QueueRequest;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.stereotype.Service;

@Service
public class RabbitAdminService {

    private final AmqpAdmin amqpAdmin;

    public RabbitAdminService(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    public void crearCola(QueueRequest request) {
        amqpAdmin.declareQueue(new Queue(request.name(), esDurable(request.durable())));
    }

    public void eliminarCola(String nombre) {
        amqpAdmin.deleteQueue(nombre);
    }

    public void crearExchange(ExchangeRequest request) {
        boolean durable = esDurable(request.durable());
        Exchange exchange = switch (request.type()) {
            case "direct" -> new DirectExchange(request.name(), durable, false);
            case "topic" -> new TopicExchange(request.name(), durable, false);
            case "fanout" -> new FanoutExchange(request.name(), durable, false);
            default -> throw new IllegalArgumentException(
                    "Tipo de exchange inválido: " + request.type());
        };
        amqpAdmin.declareExchange(exchange);
    }

    public void eliminarExchange(String nombre) {
        amqpAdmin.deleteExchange(nombre);
    }

    public void crearBinding(BindingRequest request) {
        amqpAdmin.declareBinding(armarBinding(
                request.queue(), request.exchange(), request.routingKey()));
    }

    public void eliminarBinding(String cola, String exchange, String routingKey) {
        amqpAdmin.removeBinding(armarBinding(cola, exchange, routingKey));
    }

    private Binding armarBinding(String cola, String exchange, String routingKey) {
        return new Binding(cola, Binding.DestinationType.QUEUE, exchange, routingKey, null);
    }

    private boolean esDurable(Boolean durable) {
        return durable == null || durable;
    }
}