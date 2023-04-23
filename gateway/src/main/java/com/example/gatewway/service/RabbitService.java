package com.example.gatewway.service;

import com.example.gatewway.config.Queues;
import com.example.gatewway.request.CustomRequest;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;
import pool.MainPool;
import rabbit.RabbitCache;
import rabbit.RabbitConfig;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
public class RabbitService {
    private static final String EXCHANGE = "exchange_microservice";

    private RabbitConfig rabbitConfig;

    @PostConstruct
    public void init() throws IOException, TimeoutException {
        this.rabbitConfig = RabbitConfig.getInstance(EXCHANGE);

        initQueues();
        initMainPool(Arrays.stream(Queues.values()).map(Queues::getQueueSend).collect(Collectors.toList()));
        initConsumer();
    }

    public void pushMessage(CustomRequest requestData) throws IOException, TimeoutException {
        AMQP.BasicProperties pros = new AMQP.BasicProperties().builder()
                .correlationId(requestData.getCorrelationId())
                .replyTo(requestData.getQueueReply())
                .appId(requestData.getName())
                .build();
        rabbitConfig.getChannel().basicPublish(EXCHANGE, requestData.getRoutingKey(), pros, SerializationUtils.serialize(requestData.getData()));
        RabbitCache.put(requestData.getCorrelationId(), requestData.getFuture());
    }

    private void initConsumer() throws IOException, TimeoutException {
        Consumer consumer = new DefaultConsumer(rabbitConfig.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String data = new String(body);
                if (RabbitCache.isExist(properties.getCorrelationId())) {
                    RabbitCache.getAndRemove(properties.getCorrelationId()).complete(data);
                }
                getChannel().basicAck(envelope.getDeliveryTag(), false);
            }
        };
        for (Queues value : Queues.values()) {
            rabbitConfig.declareConsumer(value.getQueueListen(), false, consumer);
        }
    }

    private void initMainPool(List<String> poolNames) {
        MainPool.init(poolNames);
    }

    private void initQueues() throws IOException {
        for (Queues value : Queues.values()) {
            rabbitConfig.declareQueue(value.getQueueListen());
        }
    }
}
