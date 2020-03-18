// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.yapion;

import yapi.internal.exceptions.objectnotation.YAPIONException;
import yapi.manager.yapion.internal.parser.YAPIONCurrentType;
import yapi.manager.yapion.internal.parser.YAPIONReader;
import yapi.manager.yapion.internal.parser.YAPIONTypeStack;
import yapi.manager.yapion.value.YAPIONArray;
import yapi.manager.yapion.value.YAPIONObject;
import yapi.manager.yapion.value.YAPIONPointer;
import yapi.manager.yapion.value.YAPIONValue;
import yapi.quick.Timer;

import java.io.*;

public class NYAPIONParser {

    public static void main(String[] args) {
        //NYAPIONParser.setPrintTree(true);
        //NYAPIONParser.setPrintValues(true);
        try {
            for (int i = 0; i < 100000; i++) {
                NYAPIONParser nyapionParser = new NYAPIONParser("{type(yapi.manager.yapion.Test)content{i(10.0)hugo{type(yapi.manager.yapion.Test$Hugo)content{i(0)t{type(yapi.manager.yapion.Test)content{i(10.0)hugo->0002Aa1elg0e9djItru(\"true\")hugo2->0002Aa1elg0e9djItest(0.0)}}hugo2{type(yapi.manager.yapion.Test$Hugo$Hugo2)content{i(0)this$1->0002Aa1elg0e9djI}}this$0{type(yapi.manager.yapion.Test)content{i(10.0)hugo->0002Aa1elg0e9djItru(\"true\")hugo2->0002Aa1elg0e9djItest(0.0)}}}}tru(\"true\")hugo2->0002FK9dGl9giCH0test(0.0)}}");
            }
            NYAPIONParser nyapionParser0 = new NYAPIONParser("{type(yapi.manager.yapion.Test)content{i(10.0)hugo{type(yapi.manager.yapion.Test$Hugo)content{i(0)t{type(yapi.manager.yapion.Test)content{i(10.0)hugo->0002Aa1elg0e9djItru(\"true\")hugo2->0002Aa1elg0e9djItest(0.0)}}hugo2{type(yapi.manager.yapion.Test$Hugo$Hugo2)content{i(0)this$1->0002Aa1elg0e9djI}}this$0{type(yapi.manager.yapion.Test)content{i(10.0)hugo->0002Aa1elg0e9djItru(\"true\")hugo2->0002Aa1elg0e9djItest(0.0)}}}}tru(\"true\")hugo2->0002FK9dGl9giCH0test(0.0)}}");
            System.out.println(nyapionParser0.getTime());
            // 2,000,000 ->   48509.93740 ns
            // 1,000,000 ->   55622.31082 ns
            //   100,000 ->   56068.14233 ns
            //    10,000 ->  149342.2181  ns
            //     1,000 ->  223065.57    ns
            //       100 -> 1511846.52    ns
            //        10 ->  539549.7     ns
            //         1 ->  600661       ns
            //System.out.println("\n\n\n\n");
            NYAPIONParser nyapionParser1 = new NYAPIONParser("{type(yapi.manager.yapion.Test)content{i(10.0)hugo{type(yapi.manager.yapion.Test$Hugo)content{i(0)t{type(yapi.manager.yapion.Test)content{i(10.0)hugo->0002Aa1elg0e9djItru(\"true\")hugo2->0002Aa1elg0e9djItest(0.0)}}hugo2{type(yapi.manager.yapion.Test$Hugo$Hugo2)content{i(0)this$1->0002Aa1elg0e9djI}}this$0{type(yapi.manager.yapion.Test)content{i(10.0)hugo->0002Aa1elg0e9djItru(\"true\")hugo2->0002Aa1elg0e9djItest(0.0)}}}}tru(\"true\")hugo2->0002FK9dGl9giCH0test(0.0)}Map<Test(Hello World)Hello(World)>}");
            System.out.println(nyapionParser1.getTime());
            //System.out.println("\n\n\n\n");
            NYAPIONParser nyapionParser2 = new NYAPIONParser("[Hello,TEst,Hugo,null,true,0,\"true\"]");
            System.out.println(nyapionParser2.getTime());
            //System.out.println("\n\n\n\n");
            //NYAPIONParser.setOutput(new File("/Users/jojo/IdeaProjects/YAPI/src/main/resources/YAPIONParserOutput.txt"));
            NYAPIONParser nyapionParser3 = new NYAPIONParser(new File("/Users/jojo/IdeaProjects/YAPI/src/main/resources/main/yapi.info"));
            System.out.println(nyapionParser3.getTime());
        } catch (IOException e) {

        }
    }

    private static boolean printTree = false;
    private static boolean printValues = false;
    private static PrintStream output = null;

    public static void setPrintTree(boolean printTree) {
        NYAPIONParser.printTree = printTree;
    }

    public static void setPrintValues(boolean printValues) {
        NYAPIONParser.printValues = printValues;
    }

    public static void setOutput(File file) {
        try {
            file.delete();
            file.createNewFile();
            NYAPIONParser.output = new PrintStream(new FileOutputStream(file), true);
        } catch (IOException e) {

        }
    }

    private YAPIONReader reader = null;
    private YAPIONTypeStack typeStack = new YAPIONTypeStack();
    private boolean escaped = false;
    private YAPIONType yapionType = null;
    int i = -1;

    private String key = "";
    private StringBuilder current = new StringBuilder();

    private Timer timer = new Timer();

    public String getTime() {
        return timer.getTimeFormatted("hF>h MF>M sF>s mF>m xF>x nF>n");
    }

    public long getRawTime() {
        return timer.getTime();
    }

    public NYAPIONParser(String s) throws IOException {
        timer.start();
        parse(new ByteArrayInputStream(s.getBytes()));
        timer.stop();
    }

    public NYAPIONParser(File f) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(f);
        timer.start();
        parse(fileInputStream);
        timer.stop();
    }

    public NYAPIONParser(File f, boolean fromResource) throws IOException {
        if (fromResource) {
            timer.start();
            parse(NYAPIONParser.class.getResourceAsStream(f.getName()));
            timer.stop();
        } else {
            FileInputStream fileInputStream = new FileInputStream(f);
            timer.start();
            parse(fileInputStream);
            timer.stop();
        }
    }

    public NYAPIONParser(InputStream inputStream) throws IOException {
        timer.start();
        parse(inputStream);
        timer.stop();
    }

    private void parse(InputStream inputStream) throws IOException {
        reader = new YAPIONReader(inputStream);
        YAPIONTypeStack.setCompact(2);
        char lastChar = '\u0000';
        char cur = '\u0000';

        do {
            if (!reader.hasNext()) {
                break;
            }
            i++;

            lastChar = cur;
            cur = reader.readChar();
            char c = cur;

            printTree(c);

            if (typeStack.isEmpty()) {
                if (c == '{') {
                    typeStack.push(YAPIONCurrentType.OBJECT, i);
                    yapionType = new YAPIONObject();
                } else if (c == '[') {
                    typeStack.push(YAPIONCurrentType.ARRAY, i);
                    yapionType = new YAPIONArray();
                } else {
                    yapionType = null;
                    break;
                }
                continue;
            }

            if (typeStack.peek() == YAPIONCurrentType.POINTER) {
                parsePointer(c);
                continue;
            } else if (typeStack.peek() == YAPIONCurrentType.OBJECT) {

            } else if (typeStack.peek() == YAPIONCurrentType.ARRAY) {
                parseArray(c);
                continue;
            } else if (typeStack.peek() == YAPIONCurrentType.VALUE) {
                parseValue(c);
                continue;
            } else if (typeStack.peek() == YAPIONCurrentType.MAP) {

            }

            if (!escaped) {
                if (c == '{') {
                    push(YAPIONCurrentType.OBJECT);
                    continue;
                } else if (c == '}') {
                    pop(YAPIONCurrentType.OBJECT);
                    continue;
                }
                if (c == '[') {
                    push(YAPIONCurrentType.ARRAY);
                    continue;
                }
                if (c == '(') {
                    push(YAPIONCurrentType.VALUE);
                    continue;
                }
                if (lastChar == '-' && c == '>') {
                    current.deleteCharAt(current.length() - 1);
                    push(YAPIONCurrentType.POINTER);
                    continue;
                }
                if (c == '<') {
                    push(YAPIONCurrentType.MAP);
                    continue;
                } else if (c == '>') {
                    pop(YAPIONCurrentType.MAP);
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
        } while (typeStack.hasValue());
    }

    private void push(YAPIONCurrentType type) {
        typeStack.push(type, i);
        key = current.toString();
        current = new StringBuilder();
    }

    private void pop(YAPIONCurrentType type) {
        YAPIONCurrentType currentType = typeStack.pop();
        if (currentType != type) {
            throw new YAPIONException();
        }
    }

    private void printTree(char c) {
        if (printTree) {
            if (output != null) {
                output.println(c + " -> " + typeStack.toString());
            } else {
                System.out.println(c + " -> " + typeStack.toString());
            }
        }
    }

    private void printValue(YAPIONVariable yapionVariable) {
        if (printValues) {
            System.out.println(yapionVariable);
        }
    }

    private void parseValue(char c) {
        if (!escaped && c == ')') {
            pop(YAPIONCurrentType.VALUE);
            YAPIONVariable yapionVariable = new YAPIONVariable(key, new YAPIONValue(current.toString()));
            current = new StringBuilder();
            key = "";
            printValue(yapionVariable);
        } else {
           current.append(c);
        }
    }

    private void parsePointer(char c) {
        current.append(c);
        if (current.length() == 16) {
            pop(YAPIONCurrentType.POINTER);
            YAPIONVariable yapionVariable = new YAPIONVariable(key, new YAPIONPointer(current.toString()));
            key = "";
            current = new StringBuilder();
            printValue(yapionVariable);
        }
    }

    private void parseArray(char c) {
        key = "";
        if (!escaped && c == ']') {
            pop(YAPIONCurrentType.ARRAY);
            YAPIONVariable yapionVariable = new YAPIONVariable(key, new YAPIONValue(current.toString()));
            current = new StringBuilder();
            printValue(yapionVariable);
            return;
        } else if (!escaped && c == ',') {
            YAPIONVariable yapionVariable = new YAPIONVariable(key, new YAPIONValue(current.toString()));
            current = new StringBuilder();
            printValue(yapionVariable);
            return;
        } else if (!escaped) {
            if (c == '{') {
                typeStack.push(YAPIONCurrentType.OBJECT, i);
                return;
            } else if (c == '[') {
                typeStack.push(YAPIONCurrentType.ARRAY, i);
                return;
            } else if (c == '<') {
                typeStack.push(YAPIONCurrentType.MAP, i);
                return;
            }
        }
        current.append(c);
    }
}