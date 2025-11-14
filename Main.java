import screen.MainScreen;
import utils.ThemeColor;

public class Main {
    public static void main(String[] args) {
        ThemeColor.applyThemeDark("utils/dark-ui.properties");
        MainScreen mainScreen = new MainScreen();
        mainScreen.run();
    }
}
