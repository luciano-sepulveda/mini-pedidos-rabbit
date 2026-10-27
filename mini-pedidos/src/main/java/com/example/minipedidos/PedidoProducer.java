package com.example.minipedidos;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PedidoProducer {

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    public PedidoProducer(
            RabbitTemplate rabbitTemplate,
            @Value("${rabbitmq.exchange.pedidos}") String exchange,
            @Value("${rabbitmq.routing-key.pedidos}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public void enviarPedido(Pedido pedido) {

        rabbitTemplate.convertAndSend(exchange, routingKey, pedido);

        System.out.println("Pedido enviado a RabbitMQ: " + pedido);
    }
}
