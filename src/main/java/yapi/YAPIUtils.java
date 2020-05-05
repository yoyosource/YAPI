// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

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
        return "SPDX-License-Identifier: Apache-2.0\n" +
                "YAPI\n" +
                "Copyright (C) 2019,2020 yoyosource";
    }

    public static String license_Console() {
        return "SPDX-License-Identifier: Apache-2.0\n" +
                "YAPI\n" +
                "<WHITE:BRIGHT>(C)<WHITE> 2019,2020 <RED:BRIGHT>y<WHITE>oyosource Apache-2.0";
    }

    public static String license_OneLine() {
        return "Copyright (C) 2019,2020 yoyosource";
    }

    public static String license_OneLine_Console() {
        return "<WHITE:BRIGHT>(C)<WHITE> 2019,2020 <RED:BRIGHT>y<WHITE>oyosource Apache-2.0";
    }

    public static void license_Send() {
        new Console().send(ConsoleMessageBuilder.build(license_Console()));
    }

    public static String logo() {
        return "\\\\  \\¸/ /¯\\ |¯) ˙|˙  //\n" +
                "//   |  |¯| |¯  ¸|¸  \\\\";
    }

    public static String logo_Console() {
        return "<RED>\\\\\\\\  <RED:BRIGHT,BOLD>\\\\¸/<WHITE> /¯\\\\ |¯) ˙|˙</BOLD>  <RED>//\n" +
                "<RED>//  <RED:BRIGHT,BOLD> |<WHITE>  |¯| |¯  ¸|¸</BOLD>  <RED>\\\\\\\\";
    }

    public static void logo_Send() {
        new Console().send(ConsoleMessageBuilder.build(logo_Console()));
    }

    public static void logo_license_Send() {
        new Console().send(ConsoleMessageBuilder.build(logo_Console() + "\n\n" + license_OneLine_Console()));
    }

}