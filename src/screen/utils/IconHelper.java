package src.screen.utils;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class IconHelper {
    private static final String BASE_PATH = "/resources/icons/";
    private static final String FRIDGE_PNG = "fridge.png";
    private static final String FAVORITE_JPG  = "favorite.jpg";
    private static final String ADDRECIPE_JPG = "addrecipe.jpg";

    private IconHelper() {
    }

    public static ImageIcon getFridge() {
        ImageIcon icon = loadIcon(BASE_PATH + FRIDGE_PNG);
        if (icon != null) {
            Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
        return null;
    }
    
    public static ImageIcon getFavorite() {
        ImageIcon icon = loadIcon(BASE_PATH + FAVORITE_JPG);
        if (icon != null) {
            Image img = icon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
        return null;
    }
    
    public static ImageIcon getAddRecipe() {
        ImageIcon icon = loadIcon(BASE_PATH + ADDRECIPE_JPG);
        if (icon != null) {
            Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
        return null;
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
