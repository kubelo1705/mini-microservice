package com.example.gatewway.config;

public enum Queues {
    ADD("add_reply", "add_numbers", "add_reply", "add_numbers_reply"),
    SUBTRACT("subtract_reply", "subtract_numbers", "subtract_reply", "subtract_numbers_reply");

    Queues(String queueName, String routingKeySend, String queueListen, String routing_key_listen) {
        this.queueSend = queueName;
        this.routingKeySend = routingKeySend;
        this.queueListen = queueListen;
        this.routing_key_listen = routing_key_listen;
    }

    private String queueSend;
    private String routingKeySend;
    private String queueListen;
    private final String routing_key_listen;

    public String getQueueSend() {
        return queueSend;
    }

    public void setQueueSend(String queueSend) {
        this.queueSend = queueSend;
    }

    public String getRoutingKeySend() {
        return routingKeySend;
    }

    public void setRoutingKeySend(String routingKeySend) {
        this.routingKeySend = routingKeySend;
    }

    public String getQueueListen() {
        return queueListen;
    }

    public void setQueueListen(String queueListen) {
        this.queueListen = queueListen;
    }
}

