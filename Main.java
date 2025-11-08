import screen.MainDisplay;
import utils.ThemeColor;

public class Main {
    public static void main(String[] args) {
        ThemeColor.applyThemeDark("utils/dark-ui.properties");
        MainDisplay mainDisplay = new MainDisplay();
        mainDisplay.run();
    }
}
