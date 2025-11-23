import screen.MainScreen;
import theme.ThemeColor;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        ThemeColor.applyThemeDark("theme/dark-ui.properties");
        SwingUtilities.invokeLater(() -> {
            MainScreen mainScreen = new MainScreen();
            mainScreen.run();
        });
    }
}
