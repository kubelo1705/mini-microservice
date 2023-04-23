package service;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.impl.AMQImpl;
import config.Queues;
import model.RequestData;
import pool.MainPool;
import rabbit.RabbitCache;
import rabbit.RabbitConfig;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class ServiceCore {
    private static final String EXCHANGE = "exchange_microservice";

    private RabbitConfig rabbitConfig;

    public void init() throws IOException, TimeoutException {
        rabbitConfig=new RabbitConfig(EXCHANGE);

        initMainPool();
        initQueues();
    }

    private void initQueues() throws IOException, TimeoutException {
        Consumer consumer = new DefaultConsumer(rabbitConfig.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    RequestData data=deserialize(body);
                    getChannel().basicAck(envelope.getDeliveryTag(), false);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
        for (Queues value : Queues.values()) {
            rabbitConfig.declareQueue(value.getName(),value.getRoutingKey());
            rabbitConfig.declareConsumer(value.getName(),false,consumer);
        }
    }

    private void initMainPool(){
        MainPool.init(Arrays.stream(Queues.values()).map(queue->queue.getName()).collect(Collectors.toList()));
    }

    private RequestData deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis=new ByteArrayInputStream(bytes);
        ObjectInputStream ois=new ObjectInputStream(bis);
        return (RequestData) ois.readObject();
    }
}
