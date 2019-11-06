package yapi.utils;

import java.awt.*;

public class ColorUtils {

    private ColorUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     *
     * @since Version 1
     *
     * @param color
     * @return
     */
    public static Color getColor(String color) {
        if (color.length() != 8 && color.length() != 6) {
            return new Color(0, 0, 0);
        }
        try {
            if (color.length() == 6) {
                String r = color.substring(0, 1);
                String g = color.substring(2, 3);
                String b = color.substring(4, 6);
                return new Color(Integer.parseInt(r, 16), Integer.parseInt(g, 16), Integer.parseInt(b, 16));
            } else {
                String r = color.substring(0, 1);
                String g = color.substring(2, 3);
                String b = color.substring(4, 6);
                String a = color.substring(7, 8);
                return new Color(Integer.parseInt(r, 16), Integer.parseInt(g, 16), Integer.parseInt(b, 16), Integer.parseInt(a, 16));
            }
        } catch (NumberFormatException e) {
            return new Color(0, 0, 0);
        }
    }

}
