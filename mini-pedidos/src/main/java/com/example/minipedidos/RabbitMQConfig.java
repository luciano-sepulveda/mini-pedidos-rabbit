package com.example.minipedidos;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.pedidos}")
    private String exchangePedidos;

    @Value("${rabbitmq.queue.pedidos}")
    private String colaPedidos;

    @Value("${rabbitmq.routing-key.pedidos}")
    private String routingKeyPedidos;

    @Value("${rabbitmq.exchange.dlx}")
    private String exchangeDlx;

    @Value("${rabbitmq.queue.dlq}")
    private String colaDlq;

    @Value("${rabbitmq.routing-key.dlq}")
    private String routingKeyDlq;

    // ---------- Caso de uso: pedidos ----------
    @Bean
    public DirectExchange pedidosExchange() {
        return new DirectExchange(exchangePedidos, true, false);
    }

    @Bean
    public Queue pedidosQueue() {
        return new Queue(colaPedidos, true);
    }

    @Bean
    public Binding pedidosBinding() {
        return BindingBuilder.bind(pedidosQueue())
                .to(pedidosExchange())
                .with(routingKeyPedidos);
    }

    // ---------- Mensajes rechazados: DLX + DLQ ----------
    @Bean
    public DirectExchange pedidosDlxExchange() {
        return new DirectExchange(exchangeDlx, true, false);
    }

    @Bean
    public Queue pedidosDlq() {
        return new Queue(colaDlq, true);
    }

    @Bean
    public Binding pedidosDlqBinding() {
        return BindingBuilder.bind(pedidosDlq())
                .to(pedidosDlxExchange())
                .with(routingKeyDlq);
    }

    @Bean
    public ApplicationRunner inicializarRabbitMQ(AmqpAdmin amqpAdmin) {
        return args -> amqpAdmin.initialize();
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
