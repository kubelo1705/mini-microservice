package config;

public enum Queues {
    ADD("ADD", "add_numbers"),
    SUBTRACT("SUBTRACT", "subtract_numbers");

    private final String name;
    private final String routingKey;

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
