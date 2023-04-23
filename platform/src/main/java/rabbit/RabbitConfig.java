package rabbit;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitConfig {
    private static final String HOST = "localhost";
    private static final String USER = "guest";
    private static final String PASSWORD = "guest";
    private static final int PORT = 5672;
    protected String exchange;

    protected Connection connection;
    protected Channel channel;

    private static RabbitConfig instance;

    public RabbitConfig(String exchange) throws IOException, TimeoutException {
        this.exchange = exchange;
        createConnection();
        createChannel();
    }

    public static RabbitConfig getInstance(String exchange) throws IOException, TimeoutException {
        if (instance == null)
            instance = new RabbitConfig(exchange);
        return instance;
    }

    private void createConnection() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(HOST);
        connectionFactory.setUsername(USER);
        connectionFactory.setPassword(PASSWORD);
        connectionFactory.setPort(PORT);
        connection = connectionFactory.newConnection();
    }

    private void createChannel() throws IOException {
        channel = connection.createChannel();
        channel.exchangeDeclare(exchange, "direct");
    }

    public Channel getChannel() throws IOException, TimeoutException {
        if (connection == null)
            createConnection();

        if (channel == null)
            createChannel();

        return channel;
    }

    public void declareQueue(String queueName, String routingKey) throws IOException {
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, exchange, routingKey);
    }

    public void declareQueue(String queueName) throws IOException {
        channel.queueDeclare(queueName, false, false, false, null);
    }

    public void declareConsumer(String name, boolean autoAck, Consumer consumer) throws IOException {
        channel.basicConsume(name, autoAck, consumer);
    }
}
