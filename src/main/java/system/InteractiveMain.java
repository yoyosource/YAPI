/**
 * Copyright 2019,2020 yoyosource
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package system;

import yapi.file.FileUtils;
import yapi.manager.yapion.YAPIONParser;
import yapi.manager.yapion.value.YAPIONObject;
import yapi.math.NumberUtils;
import yapi.runtime.Hook;
import yapi.runtime.TerminalUtils;
import yapi.string.StringSplitting;
import yapi.ui.console.Console;
import yapi.ui.console.ConsoleMessageBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InteractiveMain {

    private static final List<String> messages = new ArrayList<>();

    private static void send() {
        if (messages.isEmpty()) {
            return;
        }
        String message = messages.stream().collect(Collectors.joining("\n"));
        messages.clear();
        System.out.println(message);
    }

    private static void send(String s) {
        while (s.endsWith(" ")) {
            s = s.substring(0, s.length() - 1);
        }
        messages.add(s);
    }

    private static void send(StringBuilder s) {
        send(s.toString());
    }

    public static void main(String[] args) {
        if (false) {
            System.out.println(NumberUtils.factorial(BigInteger.valueOf(1000)));
            return;
        }
        if (false) {
            Console.main(args);
            //ProveOfWork.main(args);
            return;
        }
        if (true) {
            Console console = new Console();
            TerminalUtils.addResizeHook(new Hook() {
                @Override
                public void run() {
                    System.out.println();
                }
            });
            console.send(ConsoleMessageBuilder.build( "" +
                    "<BLACK:bg>    <RED:bg>    <GREEN:bg>    <YELLOW:bg>    <BLUE:bg>    <MAGENTA:bg>    <CYAN:bg>    <WHITE:bg>    <DEFAULT>\n" +
                    "<BLACK:bg>    <RED:bg>    <GREEN:bg>    <YELLOW:bg>    <BLUE:bg>    <MAGENTA:bg>    <CYAN:bg>    <WHITE:bg>    <DEFAULT>\n" +
                    "<BLACK:bright:bg>    <RED:bright:bg>    <GREEN:bright:bg>    <YELLOW:bright:bg>    <BLUE:bright:bg>    <MAGENTA:bright:bg>    <CYAN:bright:bg>    <WHITE:bright:bg>    <DEFAULT>\n" +
                    "<BLACK:bright:bg>    <RED:bright:bg>    <GREEN:bright:bg>    <YELLOW:bright:bg>    <BLUE:bright:bg>    <MAGENTA:bright:bg>    <CYAN:bright:bg>    <WHITE:bright:bg>    <DEFAULT>\n" +
                    "<FAINT><BLACK>████<RED>████<GREEN>████<YELLOW>████<BLUE>████<MAGENTA>████<CYAN>████<WHITE>████<DEFAULT>\n" +
                    "<FAINT><BLACK:bright>████<RED:bright>████<GREEN:bright>████<YELLOW:bright>████<BLUE:bright>████<MAGENTA:bright>████<CYAN:bright>████<WHITE:bright>████<DEFAULT>\n" +
                    "<LEFT>LEFT <CENTER>CENTER <RIGHT> RIGHT<DEFAULT:alignment>" +
                    "<BOLD>BOLD<DEFAULT:attribute> <FAINT>FAINT<DEFAULT:attribute> <ITALIC>ITALIC<DEFAULT:attribute> <UNDERLINE>UNDERLINE<DEFAULT:attribute> <UNDERLINE:double>UNDERLINE:double<DEFAULT:attribute> <NEGATIVE>NEGATIVE<DEFAULT:attribute> <CONCEAL>CONCEAL<DEFAULT:attribute> <STRIKETHROUGH>STRIKETHROUGH<DEFAULT:attribute>"));
            console.send(ConsoleMessageBuilder.build("<BOLD>" +
                    "      .:'\n" +
                    "   _ :'_\n" +
                    ".'\\`_\\`-'_\\`\\`.\n" +
                    ":________.-'\n" +
                    ":_______:\n" +
                    " :_______\\`-;\n" +
                    "  \\`._.-._.'"));
            console.send(ConsoleMessageBuilder.build("<FAINT><BLUE>redfaint"));

            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                String s;
                while ((s = bufferedReader.readLine()) != null) {
                    System.out.println("Message:");
                    console.send(ConsoleMessageBuilder.build(s));
                }
            } catch (IOException e) {

            }
            return;
        }

        //      .:'
        //   _ :'_
        //.'\`_\`-'_\`\`.
        //:________.-'
        //:_______:
        // :_______\`-;
        //  \`._.-._.'

        /*
        try {
            byte[] bytes = InteractiveMain.class.getResourceAsStream("/yapi/conjecture/Collatz.class").readAllBytes();
            StringBuilder st = new StringBuilder();
            for (byte b : bytes) {
                st._append((char)b);
            }
            System.out.println(st);
        } catch (IOException e) {

        }
        */
        YAPIONObject yapionObject = YAPIONParser.parseObject(StringSplitting.merge(FileUtils.fileContentFromResourceAsString("main/yapi.info"), "\n"));
        System.out.println("YAPI");
        version(yapionObject, 0, new ArrayList<>());
    }

    private static void version(YAPIONObject yapionObject, int recursion, List<Integer> remove) {
        List<String> strings = yapionObject.getKeys();
        for (int i = 0; i < strings.size(); i++) {
            String s = strings.get(i);

            if (s.endsWith(".java")) {
                System.out.println(lines(recursion, i, strings.size(), remove) + s);
                System.out.println(lines(recursion + 1, 0, 1, remove) + yapionObject.getValue(s).toString());
                remove.remove((Integer)(recursion + 1));
            } else {
                System.out.println(lines(recursion, i, strings.size(), remove) + "/" + s);
                version(yapionObject.getObject(s), recursion + 1, remove);
            }
        }
        remove.remove((Integer)recursion);
    }

    private static String lines(int recursion, int index, int size, List<Integer> remove) {
        StringBuilder st = new StringBuilder();
        for (int i = 0; i < recursion; i++) {
            if (remove.contains(i)) {
                st.append("  ");
                continue;
            }
            st.append("│ ");
        }
        if (index != size - 1) {
            st.append("├ ");
        } else {
            remove.add(recursion);
            st.append("└ ");
        }
        return st.toString();
    }


}