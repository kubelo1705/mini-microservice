package service;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import config.Queues;
import model.Data;
import org.reflections.Reflections;
import pool.MainPool;
import rabbit.RabbitConfig;
import task.Task;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class ServiceCore {
    private static final String EXCHANGE = "exchange_microservice";
    private static final Map<String, Task> mapTasks = new HashMap<>();

    private RabbitConfig rabbitConfig;

    public void init() throws Exception {
        rabbitConfig = RabbitConfig.getInstance(EXCHANGE);

        initTask();
        initMainPool();
        initQueues();
    }

    private void initQueues() throws IOException, TimeoutException {
        Consumer consumer = new DefaultConsumer(rabbitConfig.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                MainPool.submitJob(properties.getAppId(), () -> {
                    try {
                        Data data = deserialize(body);
                        Task task = mapTasks.getOrDefault(properties.getAppId(), mapTasks.get("DEFAULT"));
                        task.execute(data);
                        AMQP.BasicProperties replyPros = new AMQP.BasicProperties()
                                .builder()
                                .correlationId(properties.getCorrelationId())
                                .build();
                        getChannel().basicPublish(envelope.getExchange(), properties.getReplyTo(), replyPros, data.getResponse().getBytes());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            getChannel().basicAck(envelope.getDeliveryTag(), false);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        for (Queues value : Queues.values()) {
            rabbitConfig.declareQueue(value.getName(), value.getRoutingKey());
            rabbitConfig.declareConsumer(value.getName(), false, consumer);
        }
    }

    private void initMainPool() {
        MainPool.init(Arrays.stream(Queues.values()).map(Queues::getName).collect(Collectors.toList()));
    }

    private Data deserialize(byte[] bytes) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        return (Data) ois.readObject();
    }

    private void initTask() throws Exception {
        Reflections reflection = new Reflections("task");
        for (Class<? extends Task> taskClass : reflection.getSubTypesOf(Task.class)) {
            Task task = taskClass.getConstructor().newInstance();
            mapTasks.put(task.getName(), task);
        }
    }
}
