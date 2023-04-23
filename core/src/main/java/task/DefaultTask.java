package task;

import model.Data;

public class DefaultTask extends Task {
    public DefaultTask() {
        super("DEFAULT");
    }

    @Override
    public void execute(Data data) {
        data.setResponse("Do nothing");
    }
}
