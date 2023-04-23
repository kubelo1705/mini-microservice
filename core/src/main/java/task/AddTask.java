package task;

import model.Data;

public class AddTask extends Task {
    public AddTask() {
        super("ADD");
    }

    @Override
    public void execute(Data data) {
        data.setResponse(String.valueOf((Integer.parseInt(data.getNumber1()) + Integer.parseInt(data.getNumber2()))));
    }
}
