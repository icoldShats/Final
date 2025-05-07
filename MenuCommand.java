package command;

import java.awt.*;
import javax.swing.*;

public class MenuCommand implements Command {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MenuCommand(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
    }

    @Override
    public void execute() {
        cardLayout.show(mainPanel, "menu");
    }
}
