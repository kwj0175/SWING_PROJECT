package src.screen.utils;

import javax.swing.*;
import java.net.URL;

public class IconHelper {
    private static final String BASE_PATH = "/resources/icons/";

    private static final String FRIDGE_ON = "fridge_on.png";
    private static final String FAVORITE_ON  = "favorite_on.png";
    private static final String S_FAVORITE_ON  = "s_favorite_on.png";
    private static final String CALENDAR_ON = "calendar_on.png";
    private static final String S_CALENDAR_ON = "s_calendar_on.png";
    private static final String HOME_ON = "home_on.png";
    private static final String MENU_ON = "menu_on.png";
    private static final String SEARCH_ON = "search_on.png";

    private static final String FRIDGE_OFF = "fridge_off.png";
    private static final String FAVORITE_OFF  = "favorite_off.png";
    private static final String S_FAVORITE_OFF  = "s_favorite_off.png";
    private static final String CALENDAR_OFF = "calendar_off.png";
    private static final String S_CALENDAR_OFF = "s_calendar_off.png";
    private static final String HOME_OFF = "home_off.png";
    private static final String MENU_OFF = "menu_off.png";
    private static final String SEARCH_OFF = "search_off.png";

    private IconHelper() {
    }

    public static ImageIcon getFridgeOnIcon() {
        return loadIcon(BASE_PATH + FRIDGE_ON);
    }

    public static ImageIcon getFavoriteOnIcon() {
        return loadIcon(BASE_PATH + FAVORITE_ON);
    }

    public static ImageIcon getSFavoriteOnIcon() {
        return loadIcon(BASE_PATH + S_FAVORITE_ON);
    }

    public static ImageIcon getCalendarOnIcon() {
        return loadIcon(BASE_PATH + CALENDAR_ON);
    }

    public static ImageIcon getSCalendarOnIcon() {
        return loadIcon(BASE_PATH + S_CALENDAR_ON);
    }

    public static ImageIcon getHomeOnIcon() {
        return loadIcon(BASE_PATH + HOME_ON);
    }

    public static ImageIcon getMenuOnIcon() {
        return loadIcon(BASE_PATH + MENU_ON);
    }

    public static ImageIcon getSearchOnIcon() {
        return loadIcon(BASE_PATH + SEARCH_ON);
    }

    public static ImageIcon getSFridgeOffIcon() {
        return loadIcon(BASE_PATH + FRIDGE_OFF);
    }

    public static ImageIcon getFavoriteOffIcon() {
        return loadIcon(BASE_PATH + FAVORITE_OFF);
    }

    public static ImageIcon getSFavoriteOffIcon() {
        return loadIcon(BASE_PATH + S_FAVORITE_OFF);
    }

    public static ImageIcon getCalendarOffIcon() {
        return loadIcon(BASE_PATH + CALENDAR_OFF);
    }

    public static ImageIcon getSCalendarOffIcon() {
        return loadIcon(BASE_PATH + S_CALENDAR_OFF);
    }

    public static ImageIcon getHomeOffIcon() {
        return loadIcon(BASE_PATH + HOME_OFF);
    }

    public static ImageIcon getMenuOffIcon() {
        return loadIcon(BASE_PATH + MENU_OFF);
    }

    public static ImageIcon getSearchOffIcon() {
        return loadIcon(BASE_PATH + SEARCH_OFF);
    }

    private static ImageIcon loadIcon(String path) {
        try {
            URL url = IconHelper.class.getResource(path);
            if (url == null) {
                if (path.startsWith("/")) {
                    url = IconHelper.class.getResource(path.substring(1));
                }
            }
            return (url != null) ? new ImageIcon(url) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
