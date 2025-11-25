import src.screen.MainScreen;
import src.theme.ThemeColor;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ThemeColor.applyThemeDark("dark-ui.properties");
            MainScreen mainScreen = new MainScreen();
            mainScreen.run();
        });
    }
}
