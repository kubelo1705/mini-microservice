package com.example.gatewway.request;

import com.example.gatewway.config.Queues;
import model.RequestData;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CustomRequest implements Serializable {
    private CompletableFuture<String> future = new CompletableFuture();
    private String result;
    private RequestData requestData;
    private String workerName;
    private String correlationId = UUID.randomUUID().toString();
    private Queues queue;

    public String getCorrelationId() {
        return correlationId;
    }

    public CustomRequest(RequestData requestData, Queues queue) {
        this.requestData = requestData;
        this.queue = queue;
        this.workerName = queue.getQueueSend();
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

    public RequestData getData() {
        return requestData;
    }

    public void setData(RequestData requestData) {
        this.requestData = requestData;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
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

    public String getQueueReply() {
        return queue.getQueueListen();
    }
}
