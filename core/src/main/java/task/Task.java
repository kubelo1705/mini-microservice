package task;

import model.Data;

public abstract class Task {
    protected String name;

    public Task(String name) {
        this.name = name;
    }

    public abstract void execute(Data data);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
