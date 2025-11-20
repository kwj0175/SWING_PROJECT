import screen.MainScreen;
import theme.ThemeColor;

public class Main {
    public static void main(String[] args) {
        ThemeColor.applyThemeDark("theme/dark-ui.properties");
        MainScreen mainScreen = new MainScreen();
    }
}
