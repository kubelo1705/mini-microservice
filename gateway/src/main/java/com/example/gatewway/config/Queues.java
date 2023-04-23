package com.example.gatewway.config;

public enum Queues {
    ADD("add_reply", "addnumbers", "add_reply"),
    SUBTRACT("subtract_reply", "subtractnumbers", "subtract_reply");

    Queues(String queueName, String routingKeySend, String queueListen) {
        this.queueSend = queueName;
        this.routingKeySend = routingKeySend;
        this.queueListen = queueListen;
    }

    private String queueSend;
    private String routingKeySend;
    private String queueListen;

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

