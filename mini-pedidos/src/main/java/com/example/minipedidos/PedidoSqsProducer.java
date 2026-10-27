package com.example.minipedidos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Service
public class PedidoSqsProducer {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;
    private final String queueUrl;

    public PedidoSqsProducer(
            SqsClient sqsClient,
            ObjectMapper objectMapper,
            @Value("${sqs.queue-url}") String queueUrl) {

        this.sqsClient = sqsClient;
        this.objectMapper = objectMapper;
        this.queueUrl = queueUrl;
    }

    public void enviarPedido(Pedido pedido) {

        try {

            String pedidoJson =
                    objectMapper.writeValueAsString(pedido);

            SendMessageRequest request =
                    SendMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .messageBody(pedidoJson)
                            .build();

            SendMessageResponse response =
                    sqsClient.sendMessage(request);

            System.out.println(
                    "Pedido enviado a SQS. MessageId: "
                    + response.messageId()
            );

        } catch (JacksonException e) {

            throw new RuntimeException(
                    "Error convirtiendo Pedido a JSON",
                    e
            );
        }
    }
}
