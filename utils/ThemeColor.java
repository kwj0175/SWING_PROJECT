package utils;

import javax.swing.*;
import java.io.FileInputStream;
import java.util.Properties;

public final class ThemeColor {

    public static void applyThemeDark(String path) {
        try (FileInputStream in = new FileInputStream(path)) {
            Properties properties = new Properties();
            properties.load(in);
            properties.forEach((key, value) ->
                    UIManager.put((String) key, parseValue((String) value)));
        } catch (Exception ignored) {

        }
    }

    private static Object parseValue(String value) {
        value = value.trim();
        if (value.startsWith("#")) return java.awt.Color.decode(value);
        if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value))
            return Boolean.parseBoolean(value);
        return value;
    }
}
