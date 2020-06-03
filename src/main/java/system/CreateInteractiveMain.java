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

import yapi.manager.yapion.YAPIONVariable;
import yapi.manager.yapion.value.YAPIONObject;
import yapi.manager.yapion.value.YAPIONValue;
import yapi.file.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CreateInteractiveMain {

    private static List<String> exclude = new ArrayList<>();
    private static BigInteger lineCount = BigInteger.ZERO;
    private static BigInteger charCount = BigInteger.ZERO;

    private static BigInteger methodCount = BigInteger.ZERO;
    private static BigInteger initCount = BigInteger.ZERO;

    private static final int[] ints = new int[256];

    private static long fileCount = 0;

    public static void main(String[] args) {
        if (args.length < 1) {
            return;
        }
        exclude = Arrays.asList(args);
        String directory = args[0].substring(0, args[0].lastIndexOf('/')) + "/src/main/java/yapi";
        if (!new File(directory).exists()) {
            return;
        }
        if (!new File(directory).isDirectory()) {
            return;
        }
        File[] files = new File(directory).listFiles();
        YAPIONObject yapionObject = new YAPIONObject();
        for (File f : files) {
            fillObject(yapionObject, f);
        }

        String infoFile = args[0].substring(0, args[0].lastIndexOf('/')) + "/src/main/resources/main/yapi.info";
        try {
            FileUtils.dump(new File(infoFile), yapionObject.toString());
            System.out.println("> Stats :YAPI");
            System.out.println("  > Files:      " + fileCount);
            System.out.println("  > Inits:      " + initCount.toString());
            System.out.println("  > Methods:    " + methodCount.toString());
            System.out.println("  > Lines:      " + lineCount.toString());
            System.out.println("  > Chars:      " + charCount.toString());
            System.out.println("  > Lines/File: " + new BigDecimal(lineCount).divide(new BigDecimal(fileCount), new MathContext(8)).toPlainString());
            System.out.println("  > Chars/File: " + new BigDecimal(charCount).divide(new BigDecimal(fileCount), new MathContext(8)).toPlainString());
            System.out.println("  > Chars/Line: " + new BigDecimal(charCount).divide(new BigDecimal(lineCount), new MathContext(8)).toPlainString());

            File readme = new File(args[0].substring(0, args[0].lastIndexOf('/')) + "/README.md");
            List<String> strings = new BufferedReader(new FileReader(readme)).lines().collect(Collectors.toList());
            updateReadMe(strings, "Files", BigInteger.valueOf(fileCount), "inactive", "https://github.com/yoyosource/YAPI");
            updateReadMe(strings, "Methods", methodCount, "inactive", "https://github.com/yoyosource/YAPI");
            updateReadMe(strings, "Code Lines", lineCount, "inactive", "https://github.com/yoyosource/YAPI");
            FileUtils.dump(readme, String.join("\n", strings));

            String s1 = createString(ints, 20, null);
            String s2 = createString(ints, 20, "qwertzuiopüasdfghjklöäyxcvbnmQWERTZUIOPÜASDFGHJKLÖÄYXCVBNM");

            System.out.println(s1);
            System.out.println(s2);

            //[![Methods 695](https://img.shields.io/badge/Methods-695-inactive)](https://github.com/yoyosource/YAPI)
            //[![Code Lines 15542](https://img.shields.io/badge/Code%20Lines-15542-inactive)](https://github.com/yoyosource/YAPI)
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String createString(int[] ints, int size, String ignore) {
        if (ignore == null) {
            ignore = "";
        }
        int[] highest = highest(ints, size, ignore.toCharArray());
        StringBuilder st = new StringBuilder();
        for (int i = 0; i < highest.length; i++) {
            if (i != 0) {
                st.append(", ");
            }
            st.append("'").append((char)highest[i]).append("'").append(":").append(ints[highest[i]]);
        }
        return st.toString();
    }

    private static int[] highest(int[] ints, int size, char... ignore) {
        int[] highest = new int[size];
        Arrays.fill(highest, 0);
        for (int i = 0; i < ints.length; i++) {
            boolean b = true;
            for (int k = 0; k < ignore.length; k++) {
                if (i == ignore[k]) {
                    b = false;
                }
            }
            if (!b) {
                continue;
            }
            int value = ints[i];
            int j = 0;
            int index = -1;
            while (j < highest.length) {
                if (ints[highest[j]] < value) {
                    index++;
                }
                j++;
            }
            if (index > -1) {
                for (int k = 0; k < index; k++) {
                    highest[k] = highest[k + 1];
                }
                highest[index] = i;
            }
        }
        return highest;
    }

    private static void updateReadMe(List<String> strings, String line, BigInteger value, String color, String link) {
        for (int i = 0; i < strings.size(); i++) {
            String s = strings.get(i);
            if (s.startsWith("# YAPI")) {
                break;
            }
            if (s.startsWith("[![" + line)) {
                StringBuilder st = new StringBuilder();
                st.append("[![").append(line).append(" ").append(value.toString()).append("](https://img.shields.io/badge/").append(line.replace("-", "--").replace(" ", "%20")).append("-").append(value.toString()).append("-").append(color).append(")](").append(link).append(")");
                strings.set(i, st.toString());
            }
        }
    }

    private static void fillObject(YAPIONObject yapionObject, File file) {
        if (file.isDirectory()) {
            if (exclude.contains("/" + file.getName())) {
                return;
            }
            YAPIONObject directoryObject = new YAPIONObject();
            yapionObject.add(new YAPIONVariable(file.getName(), directoryObject));
            File[] files = file.listFiles();
            for (File f : files) {
                fillObject(directoryObject, f);
            }
        } else if (file.isFile()) {
            if (file.getName().endsWith(".exclude")) {
                return;
            }
            YAPIONObject fileObject = new YAPIONObject();
            yapionObject.add(new YAPIONVariable(file.getName(), fileObject));
            fillFileObject(fileObject, file);
        }
    }

    private static void fillFileObject(YAPIONObject yapionObject, File file) {
        String[] strings = FileUtils.fileContentAsString(file);

        lineCount = lineCount.add(new BigInteger(strings.length + ""));
        fileCount++;
        YAPIONObject init    = new YAPIONObject();
        YAPIONObject methods = new YAPIONObject();

        int initCount = 0;
        int methodCount = 0;

        for (String s : strings) {
            s = s.trim();
            for (char c : s.toCharArray()) {
                if (c < 256) {
                    ints[c]++;
                }
            }
            charCount = charCount.add(new BigInteger(s.length() + ""));
            if (s.startsWith("public class " + FileUtils.getName(file) + " extends ")) {
                String ext = s.substring(("public class " + FileUtils.getName(file) + " extends ").length());
                yapionObject.add(new YAPIONVariable("extends", new YAPIONValue(ext.substring(0, ext.length() - 2))));
            }
            if (FileUtils.getName(file).endsWith("Exception")) {
                continue;
            }
            if (s.startsWith("public " + FileUtils.getName(file) + "(")) {
                String ini = s.substring(("public " + FileUtils.getName(file) + "(").length());
                if (ini.endsWith("{}")) {
                    ini = ini.substring(0, ini.length() - 2);
                } else if (ini.endsWith("{")) {
                    ini = ini.substring(0, ini.length() - 1);
                }
                ini = ini.trim();
                if (ini.endsWith(")")) {
                    ini = ini.substring(0, ini.length() - 1);
                }
                init.add(new YAPIONVariable("init-" + initCount++, new YAPIONValue(ini)));
            } else if (s.matches("public ((static )?|(final )?|(synchronized )?){0,3}[a-zA-Z\\[\\]<>]+ ([a-z][a-zA-Z0-9]+)\\(.*?\\) (throws .+? )?\\{")) {
                boolean isStatic = false;
                boolean isFinal = false;
                boolean isSynchronized = false;
                s = s.substring("public ".length());
                while (s.startsWith("static ") || s.startsWith("final ") || s.startsWith("synchronized ")) {
                    if (s.startsWith("static ")) {
                        isStatic = true;
                        s = s.substring("static ".length());
                    }
                    if (s.startsWith("final ")) {
                        isFinal = true;
                        s = s.substring("final ".length());
                    }
                    if (s.startsWith("synchronized ")) {
                        isSynchronized = true;
                        s = s.substring("synchronized ".length());
                    }
                }

                String returnValue = s.substring(0, s.indexOf(' '));
                s = s.substring(s.indexOf(' ') + 1);
                if (s.endsWith("{}")) {
                    s = s.substring(0, s.length() - 2);
                }
                if (s.endsWith("{")) {
                    s = s.substring(0, s.length() - 1);
                }
                s = s.trim();
                String methodName = s.substring(0, s.indexOf('('));
                String parameters = s.substring(s.indexOf('(') + 1, s.lastIndexOf(')'));
                s = s.substring(s.lastIndexOf(')'));
                String exceptions = "";
                if (s.startsWith("throws")) {
                    exceptions = s.substring("throws".length());
                }

                if (methodName.equals("main") && isStatic && returnValue.equals("void") && parameters.equals("String[] args")) {
                    continue;
                }

                YAPIONObject method = new YAPIONObject();
                methods.add(new YAPIONVariable("method-" + methodCount++, method));
                method.add(new YAPIONVariable("name", new YAPIONValue(methodName)));
                method.add(new YAPIONVariable("returns", new YAPIONValue(returnValue)));
                method.add(new YAPIONVariable("parameters", new YAPIONValue(parameters)));
                method.add(new YAPIONVariable("throws", new YAPIONValue(exceptions)));
                method.add(new YAPIONVariable("static", new YAPIONValue(isStatic + "")));
                method.add(new YAPIONVariable("final", new YAPIONValue(isFinal + "")));
                method.add(new YAPIONVariable("synchronized", new YAPIONValue(isSynchronized + "")));

            }
        }

        CreateInteractiveMain.methodCount = CreateInteractiveMain.methodCount.add(BigInteger.valueOf(methodCount));
        CreateInteractiveMain.initCount = CreateInteractiveMain.initCount.add(BigInteger.valueOf(initCount));

        yapionObject.add(new YAPIONVariable("init", init));
        yapionObject.add(new YAPIONVariable("methods", methods));
    }

}