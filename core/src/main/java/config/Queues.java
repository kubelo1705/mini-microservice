package config;

public enum Queues {
    ADD("ADD","addnumbers"),
    SUBTRACT("SUBTRACT","subtractnumbers");

    private String name;
    private String routingKey;

    Queues(String name, String routingKey) {
        this.name = name;
        this.routingKey = routingKey;
    }

    public String getName() {
        return name;
    }

    public String getRoutingKey() {
        return routingKey;
    }
}
