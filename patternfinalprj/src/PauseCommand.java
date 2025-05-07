package command;

import javax.swing.*;

public class PauseCommand implements Command {
    private JButton button;
    private Runnable togglePause;

    public PauseCommand(JButton button, Runnable togglePause) {
        this.button = button;
        this.togglePause = togglePause;
    }

    @Override
    public void execute() {
        togglePause.run();
        button.setText(button.getText().equals("Pause") ? "Resume" : "Pause");
    }
}
