package com.example.minipedidos;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PedidoConsumer {

    private static final Logger log = LoggerFactory.getLogger(PedidoConsumer.class);

    @RabbitListener(queues = "${rabbitmq.queue.pedidos}")
    public void procesarPedido(
            Pedido pedido,
            Channel channel,
            Message message) throws IOException {

        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            log.info("CONSUMIDOR RABBITMQ - Pedido recibido: {}", pedido);

            if (pedido.getCantidad() == null || pedido.getCantidad() <= 0) {
                throw new IllegalArgumentException(
                        "Cantidad invalida: debe ser mayor que cero");
            }

            channel.basicAck(deliveryTag, false);
            log.info("ACK enviado. Pedido {} procesado correctamente", pedido.getId());

        } catch (IllegalArgumentException e) {
            // Error NO recuperable: reintentar no sirve. Va directo a la DLQ.
            log.error("Pedido rechazado (no recuperable): {}", e.getMessage());
            channel.basicNack(deliveryTag, false, false);
            log.error("NACK enviado. Requeue=false. El mensaje va a pedidos.dlx -> pedidos.dlq");

        } catch (Exception e) {
            // Error posiblemente recuperable: un solo reintento.
            boolean yaFueReintentado = message.getMessageProperties().isRedelivered();
            if (!yaFueReintentado) {
                log.warn("Error inesperado, se reintenta una vez: {}", e.getMessage());
                channel.basicNack(deliveryTag, false, true);
            } else {
                log.error("Error persistente, se envia a DLQ: {}", e.getMessage());
                channel.basicNack(deliveryTag, false, false);
            }
        }
    }
}
