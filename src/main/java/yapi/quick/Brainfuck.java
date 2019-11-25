package yapi.quick;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Brainfuck {

    private List<Long> tape = new ArrayList<>();
    private int pointer = 0;
    private char[] program;

    private boolean extended = false;
    private boolean output = false;
    private long max = (long)Math.pow(2, 8) - 1;

    private long slowdown = 0;
    private long executionTime = 0;

    private List<Character> charBuffer = new ArrayList<>();

    /**
     *
     * @since Version 1.1
     *
     * @param program
     */
    public Brainfuck(String program) {
        setProgram(program);
        tape.add(0L);
    }

    /**
     *
     * @since Version 1.1
     *
     * @param program
     * @param extended
     */
    public Brainfuck(String program, boolean extended) {
        this.extended = extended;
        setProgram(program);
        tape.add(0L);
    }

    /**
     *
     * @since Version 1.1
     *
     * @param program
     * @param i
     */
    public Brainfuck(String program, int i) {
        setProgram(program);
        tape.add(0L);
        max = (long)Math.max(2, i) - 1;
    }

    /**
     *
     * @since Version 1.1
     *
     * @param program
     * @param i
     * @param extended
     */
    public Brainfuck(String program, int i, boolean extended) {
        this.extended = extended;
        setProgram(program);
        tape.add(0L);
        max = (long)Math.max(2, i) - 1;
    }

    /**
     *
     * @since Version 1.1
     *
     * @param file
     */
    public Brainfuck(File file) {
        setProgram(load(file));
        tape.add(0L);
    }

    /**
     *
     * @since Version 1.1
     *
     * @param file
     * @param extended
     */
    public Brainfuck(File file, boolean extended) {
        this.extended = extended;
        setProgram(load(file));
        tape.add(0L);
    }

    /**
     *
     * @since Version 1.1
     *
     * @param file
     * @param i
     */
    public Brainfuck(File file, int i) {
        setProgram(load(file));
        tape.add(0L);
        max = (long)Math.max(2, i) - 1;
    }

    /**
     *
     * @since Version 1.1
     *
     * @param file
     * @param i
     * @param extended
     */
    public Brainfuck(File file, int i, boolean extended) {
        this.extended = extended;
        setProgram(load(file));
        tape.add(0L);
        max = (long)Math.max(2, i) - 1;
    }

    private String load(File file) {
        if (!file.exists()) {
            return "";
        }
        if (!file.getName().endsWith(".b") && !file.getName().endsWith(".be")) {
            return "";
        }
        boolean d = false;
        if (file.getName().endsWith(".be")) {
            extended = true;
            d = true;
        }
        try {
            InputStream inputStream = new FileInputStream(file);
            byte[] bytes = inputStream.readAllBytes();
            StringBuilder st = new StringBuilder();
            boolean comment = false;
            for (byte b : bytes) {
                if (((char)b) == '\n' && d) {
                    comment = false;
                }
                if (((char)b) == '/' && d) {
                    comment = !comment;
                }
                if (comment && d) {
                    continue;
                }
                st.append((char)b);
            }
            return st.toString();
        } catch (IOException e) {

        }
        return "";
    }

    private void setProgram(String program) {
        if (extended) {
            this.program = program.replaceAll("[^<>\\[\\]+\\-\\.,:={}]", "").toCharArray();
        } else {
            this.program = program.replaceAll("[^<>\\[\\]+\\-\\.,]", "").toCharArray();
        }
    }

    /**
     *
     * @since Version 1.1
     *
     * @param output
     */
    public void setOutput(boolean output) {
        this.output = output;
    }

    /**
     *
     * @since Version 1.1
     *
     * @param slowdown
     */
    public void setSlowdown(long slowdown) {
        this.slowdown = slowdown;
    }

    /**
     *
     * @since Version 1.1
     *
     * @return
     */
    public String getCode() {
        StringBuilder st = new StringBuilder();
        for (char c : program) {
            st.append(c);
        }
        return st.toString();
    }

    /**
     *
     * @since Version 1.1
     *
     * @return
     */
    public long getExecutionTime() {
        return executionTime;
    }

    /**
     *
     * @since Version 1.1
     *
     */
    public void run() {
        int i = 0;
        while (i < program.length) {
            if (output) {
                info(i);
            }
            long time = System.currentTimeMillis();
            if (program[i] == '<') {
                decrementPointer();
            } else if (program[i] == '>') {
                incrementPointer();
            } else if (program[i] == '+') {
                incrementCell();
            } else if (program[i] == '-') {
                decrementCell();
            } else if (program[i] == '.') {
                send();
            } else if (program[i] == ',') {
                read();
            } else if (program[i] == '[') {
                i = startBracket(i);
            } else if (program[i] == ']') {
                i = endBracket(i);
            } else if (program[i] == '=' && extended) {
                info(i);
            } else if (program[i] == ':' && extended) {
                send(true);
            } else if (program[i] == '{' && extended) {
                i = startEBracket(i);
            } else if (program[i] == '}' && extended) {
                i = endEBracket(i);
            }
            time -= System.currentTimeMillis();
            executionTime -= time;
            if (slowdown != 0) {
                try {
                    Thread.sleep(slowdown);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            i++;
        }
    }

    private void info(int index) {
        StringBuilder pad = new StringBuilder();
        for (int i = 0; i < index; i++) {
            pad.append(' ');
        }
        StringBuilder program = new StringBuilder();
        for (char c : this.program) {
            program.append(c);
        }
        System.out.println(pointer + " " + tape);
        StringBuilder charBuffer = new StringBuilder();
        for (char c : this.charBuffer) {
            charBuffer.append(c).append(" ");
        }
        System.out.println("(" + charBuffer + ")");
        System.out.println(program);
        System.out.println(pad + "^");
        System.out.println();
    }

    private void incrementPointer() {
        pointer++;
        while (pointer >= tape.size()) {
            tape.add(0L);
        }
    }

    private void decrementPointer() {
        pointer--;
        while (pointer < 0) {
            tape.add(0, 0L);
            pointer++;
        }
    }

    private void incrementCell() {
        tape.set(pointer, tape.get(pointer) + 1);
        if (tape.get(pointer) > max) {
            tape.set(pointer, 0L);
        }
    }

    private void decrementCell() {
        tape.set(pointer, tape.get(pointer) - 1);
        if (tape.get(pointer) < 0) {
            tape.set(pointer, max);
        }
    }

    private void send() {
        System.out.print((char)(long)tape.get(pointer));
    }

    private void send(boolean b) {
        if (b) {
            System.out.println(tape.get(pointer));
        } else {
            send();
        }
    }

    private void read() {
        if (!charBuffer.isEmpty()) {
            long l = charBuffer.remove(0);
            tape.set(pointer, l);
            return;
        }
        Scanner scanner = new Scanner(System.in);
        String s = scanner.next();
        for (char c : s.toCharArray()) {
            charBuffer.add(c);
        }
        read();
    }

    private int startBracket(int index) {
        if (tape.get(pointer) == 0) {
            int b = 0;
            for (int i = index; i < program.length; i++) {
                if (program[i] == '[') {
                    b++;
                }
                if (program[i] == ']') {
                    b--;
                }
                if (b == 0) {
                    return i;
                }
            }
        }
        return index;
    }

    private int startEBracket(int index) {
        if (tape.get(pointer) == 0) {
            int b = 0;
            for (int i = index; i < program.length; i++) {
                if (program[i] == '{') {
                    b++;
                }
                if (program[i] == '}') {
                    b--;
                }
                if (b == 0) {
                    return i;
                }
            }
        }
        return index;
    }

    private int endBracket(int index) {
        if (tape.get(pointer) != 0) {
            int b = 0;
            for (int i = index; i >= 0; i--) {
                if (program[i] == '[') {
                    b++;
                }
                if (program[i] == ']') {
                    b--;
                }
                if (b == 0) {
                    return i - 1;
                }
            }
        }
        return index;
    }

    private int endEBracket(int index) {
        if (tape.get(pointer) != 0) {
            int b = 0;
            for (int i = index; i >= 0; i--) {
                if (program[i] == '{') {
                    b++;
                }
                if (program[i] == '}') {
                    b--;
                }
                if (b == 0) {
                    return i - 1;
                }
            }
        }
        return index;
    }

}
