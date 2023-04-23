package task;

import model.Data;

public class SubtractTask extends Task {

    public SubtractTask() {
        super("SUBTRACT");
    }

    @Override
    public void execute(Data data) {
        data.setResponse(String.valueOf((Integer.parseInt(data.getNumber1()) - Integer.parseInt(data.getNumber2()))));
    }
}
