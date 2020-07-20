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

import java.io.*;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class ReadmeSystem {

    private static BigInteger lineCount = BigInteger.ZERO;
    private static BigInteger methodCount = BigInteger.ZERO;
    private static long fileCount = 0;

    public static void main(String[] args) {
        if (args.length < 2) {
            return;
        }

        String directory = args[0].substring(0, args[0].lastIndexOf('/')) + "/src/main/java/yapi";
        if (!new File(directory).exists()) {
            return;
        }
        if (!new File(directory).isDirectory()) {
            return;
        }

        file(new File(directory));

        try {
            File readme = new File(args[0].substring(0, args[0].lastIndexOf('/')) + "/README.md");
            List<String> strings = new BufferedReader(new FileReader(readme)).lines().collect(Collectors.toList());
            updateReadMe(strings, "Files", BigInteger.valueOf(fileCount), "inactive", "https://github.com/yoyosource/" + args[1]);
            updateReadMe(strings, "Methods", methodCount, "inactive", "https://github.com/yoyosource/" + args[1]);
            updateReadMe(strings, "Code Lines", lineCount, "inactive", "https://github.com/yoyosource/" + args[1]);
        } catch (IOException e) {

        }
    }

    private static void file(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                file(f);
            }
        } else if (file.isFile()) {
            fileCount++;
            if (file.getName().endsWith(".exclude")) {
                return;
            }

            try {
                new BufferedReader(new InputStreamReader(new FileInputStream(file))).lines().forEach(s -> {
                    lineCount = lineCount.add(BigInteger.ONE);
                    if (s.matches("public ((static )?|(final )?|(synchronized )?){0,3}[a-zA-Z\\[\\]<>]+ ([a-z][a-zA-Z0-9]+)\\(.*?\\) (throws .+? )?\\{" )) {
                        methodCount = methodCount.add(BigInteger.ONE);
                    }
                });
            } catch (IOException e) {

            }
        }
    }

    private static void updateReadMe(List<String> strings, String line, BigInteger value, String color, String link) {
        for (int i = 0; i < strings.size(); i++) {
            String s = strings.get(i);
            if (s.startsWith("#")) {
                break;
            }
            if (s.startsWith("[![" + line)) {
                StringBuilder st = new StringBuilder();
                st.append("[![").append(line).append(" ").append(value.toString()).append("](https://img.shields.io/badge/").append(line.replace("-", "--").replace(" ", "%20")).append("-").append(value.toString()).append("-").append(color).append(")](").append(link).append(")");
                strings.set(i, st.toString());
            }
        }
    }

}