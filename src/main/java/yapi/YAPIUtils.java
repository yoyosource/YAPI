package yapi;

import yapi.ui.console.Console;
import yapi.ui.console.ConsoleMessageBuilder;

public class YAPIUtils {

    public static String link() {
        return "https://github.com/yoyosource/YAPI";
    }

    public static String link_Refined() {
        return "https://github.com/yoyosource/YAPI";
    }

    public static void link_Send() {
        new Console().send(ConsoleMessageBuilder.build(link_Refined()));
    }

    public static String licence() {
        return "";
    }

    public static String license_Refined() {
        return "";
    }

    public static String license_OneLine() {
        return "";
    }

    public static String license_OneLine_Refined() {
        return "";
    }

    public static void license_Send() {
        new Console().send(ConsoleMessageBuilder.build(license_Refined()));
    }

    public static String logo() {
        return "";
    }

    public static String logo_Refined() {
        return "";
    }

    public static void logo_Send() {
        new Console().send(ConsoleMessageBuilder.build(logo_Refined()));
    }

    public static void logo_license_Send() {
        new Console().send(ConsoleMessageBuilder.build(logo_Refined() + "\n\n" + license_OneLine_Refined()));
    }

}
