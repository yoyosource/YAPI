// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.yapion;

import yapi.datastructures.CircularBuffer;
import yapi.file.FileUtils;
import yapi.internal.exceptions.objectnotation.YAPIONException;
import yapi.manager.yapion.internal.YAPIONCurrentType;
import yapi.manager.yapion.internal.YAPIONReader;
import yapi.manager.yapion.internal.YAPIONTypeStack;
import yapi.manager.yapion.value.YAPIONArray;
import yapi.manager.yapion.value.YAPIONObject;
import yapi.manager.yapion.value.YAPIONValue;

import java.io.*;

public class NYAPIONParser {

    // Todo: Delete unix path
    public static void main(String[] args) {
        //YAPIONType yapionType = parse(new File(FileUtils.getUserHome() + "/Documents/YAPIONTest/sample.yapion"));
        parse("{project(yapi)contributors[{name(yoyosource)owner(true)},{name(chaoscaot444)owner(false)}]reference->0000000000000000}");
    }

    public static synchronized YAPIONType parse(String s) {
        try {
            return parse(new ByteArrayInputStream(s.getBytes()));
        } catch (IOException e) {
            throw new YAPIONException(e.getMessage());
        }
    }

    public static synchronized YAPIONType parse(File f) {
        try {
            FileInputStream fileInputStream = new FileInputStream(f);
            return parse(fileInputStream);
        } catch (IOException e) {
            throw new YAPIONException(e.getMessage());
        }
    }

    public static synchronized YAPIONType parseFromResource(String s) {
        try {
            return parse(NYAPIONParser.class.getResourceAsStream(s));
        } catch (IOException e) {
            throw new YAPIONException(e.getMessage());
        }
    }

    private static YAPIONType parse(InputStream inputStream) throws IOException {
        YAPIONReader reader = new YAPIONReader(inputStream);
        YAPIONTypeStack typeStack = new YAPIONTypeStack();
        YAPIONTypeStack.setCompact(true);
        boolean escaped = false;

        YAPIONType yapionType = null;
        CircularBuffer<Character> characters = new CircularBuffer<>(2);

        String key = "";
        StringBuilder current = new StringBuilder();

        int i = -1;
        do {
            if (!reader.hasNext()) {
                break;
            }

            i++;
            char c = reader.readChar();
            characters.add(c);
            char lastChar = '\u0000';
            if (characters.list().get(0) != null) {
                lastChar = characters.list().get(0);
            }

            if (typeStack.isEmpty()) {
                if (c == '{') {
                    typeStack.push(YAPIONCurrentType.OBJECT, i);
                    yapionType = new YAPIONObject();
                } else if (c == '[') {
                    typeStack.push(YAPIONCurrentType.ARRAY, i);
                    yapionType = new YAPIONArray();
                } else {
                    break;
                }
                continue;
            }

            if (typeStack.peek() == YAPIONCurrentType.POINTER) {
                if (current.length() == 16) {
                    key = "";
                    current = new StringBuilder();
                    YAPIONCurrentType currentType = typeStack.pop();
                    if (currentType != YAPIONCurrentType.POINTER) {
                        throw new YAPIONException();
                    }
                }
            }

            if (!escaped) {
                if (c == '{') {
                    typeStack.push(YAPIONCurrentType.OBJECT, i);
                    key = current.toString();
                    current = new StringBuilder();
                    continue;
                } else if (c == '}') {
                    YAPIONCurrentType currentType = typeStack.pop();
                    if (currentType != YAPIONCurrentType.OBJECT) {
                        throw new YAPIONException();
                    }
                }
                if (c == '[') {
                    typeStack.push(YAPIONCurrentType.ARRAY, i);
                    key = current.toString();
                    current = new StringBuilder();
                    continue;
                } else if (c == ']') {
                    YAPIONCurrentType currentType = typeStack.pop();
                    if (currentType != YAPIONCurrentType.ARRAY) {
                        throw new YAPIONException();
                    }
                }
                if (c == '(') {
                    typeStack.push(YAPIONCurrentType.VALUE, i);
                    key = current.toString();
                    current = new StringBuilder();
                    continue;
                } else if (c == ')') {
                    YAPIONCurrentType currentType = typeStack.pop();
                    if (currentType != YAPIONCurrentType.VALUE) {
                        throw new YAPIONException();
                    }
                    String value = current.toString();
                    YAPIONVariable yapionVariable = new YAPIONVariable(key, new YAPIONValue(value));
                    current = new StringBuilder();
                    key = "";
                    System.out.println(yapionVariable);
                    continue;
                }
                if (lastChar == '-' && c == '>') {
                    current.deleteCharAt(current.length() - 1);
                    typeStack.push(YAPIONCurrentType.POINTER, i);
                    key = current.toString();
                    current = new StringBuilder();
                    continue;
                }
            }
            current.append(c);

            if (escaped) {
                escaped = false;
            }
            if (c == '\\') {
                escaped = true;
            }

            System.out.println(typeStack + " -> " + c + " '" + current + "' '" + key + "'");
        } while (typeStack.hasValue());

        return yapionType;
    }

}