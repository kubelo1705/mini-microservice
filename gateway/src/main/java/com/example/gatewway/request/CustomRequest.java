package com.example.gatewway.request;

import com.example.gatewway.config.Queues;
import model.Data;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CustomRequest implements Serializable {
    private final CompletableFuture<String> future = new CompletableFuture();
    private String result;
    private Data data;
    private String name;
    private final String correlationId = UUID.randomUUID().toString();
    private final Queues queue;

    public String getCorrelationId() {
        return correlationId;
    }

    public CustomRequest(Data data, Queues queue) {
        this.data = data;
        this.queue = queue;
        this.name = queue.getQueueSend();
    }

    public CompletableFuture getFuture() {
        return future;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void execute() {
        future.complete(null);
    }

    public String getQueue() {
        return queue.getQueueSend();
    }

    public String getRoutingKey() {
        return queue.getRoutingKeySend();
    }

    public String getRoutingKeyListen(){
        return queue.getRouting_key_listen();
    }

    public String getQueueReply() {
        return queue.getQueueListen();
    }
}
