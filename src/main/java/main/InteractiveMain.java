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

package main;

import yapi.file.FileUtils;
import yapi.manager.yapion.YAPIONParser;
import yapi.manager.yapion.value.YAPIONObject;
import yapi.string.StringSplitting;
import yapi.ui.console.Console;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InteractiveMain {

    private static List<String> messages = new ArrayList<>();

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
        if (true) {
            Console.main(args);
            //ProveOfWork.main(args);
            return;
        }

        /*
        try {
            byte[] bytes = InteractiveMain.class.getResourceAsStream("/yapi/conjecture/Collatz.class").readAllBytes();
            StringBuilder st = new StringBuilder();
            for (byte b : bytes) {
                st.append((char)b);
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