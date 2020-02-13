// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui;

import yapi.exceptions.WindowCreatorException;
import yapi.exceptions.YAPIException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class YAPIWindow extends JComponent {

    private File configuration;
    private long lastModified = 0;
    private boolean updateLife = false;

    private List<List<Token>> lines = new ArrayList<>();
    private List<Command> commands = new ArrayList<>();

    private Map<String, String> selectors = new HashMap<>();

    private int width = 0;
    private int height = 0;

    /**
     *
     * @since Version 1.1
     *
     * @param configuration
     */
    public YAPIWindow(File configuration) {
        if (!configuration.exists()) {
            throw new YAPIException();
        }
        if (!configuration.isFile()) {
            throw new YAPIException();
        }
        if (Files.isSymbolicLink(Path.of(configuration.getPath()))) {
            throw new YAPIException();
        }
        if (!configuration.getName().endsWith(".uiconfig")) {
            throw new YAPIException();
        }

        addCommand();

        this.configuration = configuration;
        this.lastModified = configuration.lastModified();
        update();

        new Thread(() -> {
            while (true) {
                repaint();
                try {
                    Thread.sleep(1000 / 60);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();

        MouseAdapter mouseAdapter = new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                clicked = true;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                hold = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                hold = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                point = e.getPoint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                point = new Point(-10000, -10000);
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                point = e.getPoint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                point = e.getPoint();
            }

        };
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    private void addCommand() {
        commands.add(new Command("color") {
            @Override
            public void run(String[] args, Graphics2D g) {
                if (args.length == 1) {
                    String s = get(args[0]);
                    g.setColor(Color.decode(s));
                } else if (args.length == 3) {
                    g.setColor(new Color(Integer.decode(get(args[0])), Integer.decode(get(args[1])), Integer.decode(get(args[2]))));
                } else {
                    throw new WindowCreatorException("Too many or too few arguments for color()");
                }
            }
        });
        commands.add(new Command("foreground") {
            @Override
            public void run(String[] args, Graphics2D g) {
                if (args.length == 1) {
                    String s = get(args[0]);
                    g.setColor(Color.decode(s));
                } else {
                    throw new WindowCreatorException("Too many or too few arguments for foreground()");
                }
            }
        });
        commands.add(new Command("size") {
            @Override
            public void run(String[] args, Graphics2D g) {
                if (args.length == 1) {
                    String s = get(args[0]);
                    g.setFont(g.getFont().deriveFont(Float.parseFloat(s)));
                } else {
                    throw new WindowCreatorException("Too many or too few arguments for size()");
                }
            }
        });
        commands.add(new Command("pos") {
            @Override
            public void run(String[] args, Graphics2D g) {
                if (args.length == 2) {
                    x = Integer.decode(get(args[0]));
                    y = Integer.decode(get(args[1]));
                } else {
                    throw new WindowCreatorException("Too many or too few arguments for pos()");
                }
            }
        });
        commands.add(new Command("mode") {
            @Override
            public void run(String[] args, Graphics2D g) {
                if (args.length == 1) {
                    String s = get(args[0]);
                    if (s.equals(".normal")) {
                        mode = 0;
                        return;
                    }
                    if (s.equals(".left")) {
                        mode = 0;
                        return;
                    }
                    if (s.equals(".center")) {
                        mode = 1;
                        return;
                    }
                    if (s.equals(".right")) {
                        mode = 2;
                        return;
                    }
                    throw new WindowCreatorException("Unknown mode. Allowed are .normal, .left, .center, .right");
                } else {
                    throw new WindowCreatorException("Too many or too few arguments for mode()");
                }
            }
        });
        commands.add(new Command("weight") {
            @Override
            public void run(String[] args, Graphics2D g) {
                if (args.length == 1) {
                    weight = Integer.decode(get(args[0]));
                } else {
                    throw new WindowCreatorException("Too many or too few arguments for weight()");
                }
            }
        });

        commands.add(new Command("*CLICK") {
            @Override
            public void run(String[] args, Graphics2D g) {
                if (args.length < 3) {
                    throw new WindowCreatorException("Too few arguments for action CLICK()");
                }
                if (!clicked) {
                    return;
                }
                int w = Integer.decode(get(args[0]));
                int h = Integer.decode(get(args[1]));

                StringBuilder st = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    st.append(args[i]);
                    st.append(' ');
                }

                if (mode == 0) {
                    if (point.getX() > x && point.getX() < x + w && point.getY() > y && point.getY() < y + h) {
                        executeLine(g, tokenizeLine(st.toString()));
                    }
                } else if (mode == 1) {
                    if (point.getX() > x - w / 2 && point.getX() < x + w / 2 && point.getY() > y - h / 2 && point.getY() < y + h / 2) {
                        executeLine(g, tokenizeLine(st.toString()));
                    }
                } else if (mode == 2) {
                    if (point.getX() > x - w && point.getX() < x && point.getY() > y - h && point.getY() < y) {
                        executeLine(g, tokenizeLine(st.toString()));
                    }
                }
            }
        });
        commands.add(new Command("*HOLD") {
            @Override
            public void run(String[] args, Graphics2D g) {
                if (args.length < 3) {
                    throw new WindowCreatorException("Too few arguments for action HOLD()");
                }
                if (!hold) {
                    return;
                }
                int w = Integer.decode(get(args[0]));
                int h = Integer.decode(get(args[1]));

                StringBuilder st = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    st.append(get(args[i]));
                    st.append(' ');
                }

                if (mode == 0) {
                    if (point.getX() > x && point.getX() < x + w && point.getY() > y && point.getY() < y + h) {
                        executeLine(g, tokenizeLine(st.toString()));
                    }
                } else if (mode == 1) {
                    if (point.getX() > x - w / 2 && point.getX() < x + w / 2 && point.getY() > y - h / 2 && point.getY() < y + h / 2) {
                        executeLine(g, tokenizeLine(st.toString()));
                    }
                } else if (mode == 2) {
                    if (point.getX() > x - w && point.getX() < x && point.getY() > y - h && point.getY() < y) {
                        executeLine(g, tokenizeLine(st.toString()));
                    }
                }
            }
        });
        commands.add(new Command("*HOVER") {
            @Override
            public void run(String[] args, Graphics2D g) {
                if (args.length < 3) {
                    throw new WindowCreatorException("Too few arguments for action HOVER()");
                }
                int w = Integer.decode(get(args[0]));
                int h = Integer.decode(get(args[1]));

                StringBuilder st = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    st.append(get(args[i]));
                    st.append(' ');
                }

                if (mode == 0) {
                    if (point.getX() > x && point.getX() < x + w && point.getY() > y && point.getY() < y + h) {
                        executeLine(g, tokenizeLine(st.toString()));
                    }
                } else if (mode == 1) {
                    if (point.getX() > x - w / 2 && point.getX() < x + w / 2 && point.getY() > y - h / 2 && point.getY() < y + h / 2) {
                        executeLine(g, tokenizeLine(st.toString()));
                    }
                } else if (mode == 2) {
                    if (point.getX() > x - w && point.getX() < x && point.getY() > y - h && point.getY() < y) {
                        executeLine(g, tokenizeLine(st.toString()));
                    }
                }
            }
        });
        commands.add(new Command("*IF") {
            @Override
            public void run(String[] args, Graphics2D g) {
                if (args.length < 3) {
                    throw new WindowCreatorException("Too few arguments for action IF()");
                }
                String ifString = get(args[0]);
                String checkString = get(args[1]);

                StringBuilder st = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    st.append(get(args[i]));
                    st.append(' ');
                }

                if (ifString.equals(checkString)) {
                    executeLine(g, tokenizeLine(st.toString()));
                }
            }
        });
        commands.add(new Command("*IFNOT") {
            @Override
            public void run(String[] args, Graphics2D g) {
                if (args.length < 3) {
                    throw new WindowCreatorException("Too few arguments for action IFNOT()");
                }
                String ifString = get(args[0]);
                String checkString = get(args[1]);

                StringBuilder st = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    st.append(get(args[i]));
                    st.append(' ');
                }

                if (!ifString.equals(checkString)) {
                    executeLine(g, tokenizeLine(st.toString()));
                }
            }
        });
        commands.add(new Command("*INIT") {
            @Override
            public void run(String[] args, Graphics2D g) {
                StringBuilder st = new StringBuilder();
                for (int i = 0; i < args.length; i++) {
                    st.append(get(args[i]));
                    st.append(' ');
                }

                if (init) {
                    executeLine(g, tokenizeLine(st.toString()));
                }
            }
        });
        commands.add(new Command("*TOUCH") {
            @Override
            public void run(String[] args, Graphics2D g) {
                if (args.length < 1) {
                    throw new WindowCreatorException("Too few arguments for action TOUCH()");
                }

                StringBuilder st = new StringBuilder();
                for (int i = 0; i < args.length; i++) {
                    st.append(get(args[i]));
                    st.append(' ');
                }

                int w = width;
                int h = height;

                if (mode == 0) {
                    if (point.getX() > x && point.getX() < x + w && point.getY() > y && point.getY() < y + h) {
                        executeLine(g, tokenizeLine(st.toString()));
                    }
                } else if (mode == 1) {
                    if (point.getX() > x - w / 2 && point.getX() < x + w / 2 && point.getY() > y - h / 2 && point.getY() < y + h / 2) {
                        executeLine(g, tokenizeLine(st.toString()));
                    }
                } else if (mode == 2) {
                    if (point.getX() > x - w && point.getX() < x && point.getY() > y - h && point.getY() < y) {
                        executeLine(g, tokenizeLine(st.toString()));
                    }
                }
            }
        });
        commands.add(new Command("*TOUCHNOT") {
            @Override
            public void run(String[] args, Graphics2D g) {
                if (args.length < 1) {
                    throw new WindowCreatorException("Too few arguments for action TOUCH()");
                }

                StringBuilder st = new StringBuilder();
                for (int i = 0; i < args.length; i++) {
                    st.append(get(args[i]));
                    st.append(' ');
                }

                int w = width;
                int h = height;

                if (mode == 0 && (!(point.getX() > x && point.getX() < x + w && point.getY() > y && point.getY() < y + h))) {
                    executeLine(g, tokenizeLine(st.toString()));
                } else if (mode == 1 && (!(point.getX() > x - w / 2 && point.getX() < x + w / 2 && point.getY() > y - h / 2 && point.getY() < y + h / 2))) {
                    executeLine(g, tokenizeLine(st.toString()));
                } else if (mode == 2 && (!(point.getX() > x - w && point.getX() < x && point.getY() > y - h && point.getY() < y))) {
                    executeLine(g, tokenizeLine(st.toString()));
                }
            }
        });

        commands.add(new Command("Background") {
            @Override
            public void run(String[] args, Graphics2D g) {
                g.fillRect(0, 0, getWidth(), getHeight());
                width = getWidth();
                height = getHeight();
            }
        });
        commands.add(new Command("Text") {
            @Override
            public void run(String[] args, Graphics2D g) {
                String text = get(Arrays.stream(args).collect(Collectors.joining(" ")));
                if (text.startsWith("\"") && text.endsWith("\"")) {
                    text = text.substring(1, text.length() - 1);
                }
                if (mode == 0) {
                    g.drawString(text, x, y);
                } else if (mode == 1) {
                    g.drawString(text, x - g.getFontMetrics().stringWidth(text) / 2, y);
                } else if (mode == 2) {
                    g.drawString(text, x - g.getFontMetrics().stringWidth(text), y);
                }
            }
        });
        commands.add(new Command("Label") {
            @Override
            public void run(String[] args, Graphics2D g) {
                String text = get(Arrays.stream(args).collect(Collectors.joining(" ")));
                if (text.startsWith("\"") && text.endsWith("\"")) {
                    text = text.substring(1, text.length() - 1);
                }
                if (mode == 0) {
                    g.drawString(text, x, y);
                } else if (mode == 1) {
                    g.drawString(text, x - g.getFontMetrics().stringWidth(text) / 2, y);
                } else if (mode == 2) {
                    g.drawString(text, x - g.getFontMetrics().stringWidth(text), y);
                }
            }
        });
        commands.add(new Command("Rect") {
            @Override
            public void run(String[] args, Graphics2D g) {
                if (args.length == 2) {
                    int w = Integer.decode(get(args[0]));
                    int h = Integer.decode(get(args[1]));
                    for (int i = 0; i < weight; i++) {
                        if (mode == 0) {
                            g.fillRect(x, y, w, h);
                        } else if (mode == 1) {
                            g.fillRect(x - w / 2, y - h / 2, w, h);
                        } else if (mode == 2) {
                            g.fillRect(x - w, y - h, w, h);
                        }
                        w -= 2;
                        h -= 2;
                    }
                    width = w;
                    height = h;
                } else {
                    throw new WindowCreatorException("Too many or too few arguments for Rect()");
                }
            }
        });
        commands.add(new Command("Rectangle") {
            @Override
            public void run(String[] args, Graphics2D g) {
                if (args.length == 2) {
                    int w = Integer.decode(get(args[0]));
                    int h = Integer.decode(get(args[1]));
                    for (int i = 0; i < weight; i++) {
                        if (mode == 0) {
                            g.fillRect(x, y, w, h);
                        } else if (mode == 1) {
                            g.fillRect(x - w / 2, y - h / 2, w, h);
                        } else if (mode == 2) {
                            g.fillRect(x - w, y - h, w, h);
                        }
                        w -= 2;
                        h -= 2;
                    }
                    width = w;
                    height = h;
                } else {
                    throw new WindowCreatorException("Too many or too few arguments for Rectangle()");
                }
            }
        });
        commands.add(new Command("Box") {
            @Override
            public void run(String[] args, Graphics2D g) {
                if (args.length == 2) {
                    int w = Integer.decode(get(args[0]));
                    int h = Integer.decode(get(args[1]));
                    for (int i = 0; i < weight; i++) {
                        if (mode == 0) {
                            g.drawRect(x, y, w, h);
                        } else if (mode == 1) {
                            g.drawRect(x - w / 2, y - h / 2, w, h);
                        } else if (mode == 2) {
                            g.drawRect(x - w, y - h, w, h);
                        }
                        w -= 2;
                        h -= 2;
                    }
                    width = w;
                    height = h;
                } else {
                    throw new WindowCreatorException("Too many or too few arguments for Box()");
                }
            }
        });
        commands.add(new Command("Line") {
            @Override
            public void run(String[] args, Graphics2D g) {
                if (args.length == 2) {
                    int x2 = Integer.decode(get(args[0]));
                    int y2 = Integer.decode(get(args[1]));

                    if (weight == 1) {
                        g.drawLine(x, y, x2, y2);
                    } else {
                        int dx = x2 - x;
                        int dy = y2 - y;
                        dx *= dx;
                        dy *= dy;

                        int steps = (int)Math.sqrt(dx + dy);
                        steps /= (weight / 10);

                        for (int i = 0; i < steps; i++) {
                            int nx = (int)(x + (x2 - x + 0.0) / steps * i);
                            int ny = (int)(y + (y2 - y + 0.0) / steps * i);
                            if (mode == 0) {
                                g.fillOval(nx, ny, weight, weight);
                            } else if (mode == 1) {
                                g.fillOval(nx - weight / 2, ny - weight / 2, weight, weight);
                            } else if (mode == 2) {
                                g.fillOval(nx - weight, ny - weight, weight, weight);
                            }
                        }
                    }
                } else {
                    throw new WindowCreatorException("Too many or too few arguments for Line()");
                }
            }
        });
        commands.add(new Command("Oval") {
            @Override
            public void run(String[] args, Graphics2D g) {
                if (args.length == 2) {
                    int w = Integer.decode(get(args[0]));
                    int h = Integer.decode(get(args[1]));
                    if (mode == 0) {
                        g.fillOval(x, y, w, h);
                    } else if (mode == 1) {
                        g.fillOval(x - w / 2, y - h / 2, w, h);
                    } else if (mode == 2) {
                        g.fillOval(x - w, y - h, w, h);
                    }
                    width = w;
                    height = h;
                } else {
                    throw new WindowCreatorException("Too many or too few arguments for Oval()");
                }
            }
        });
        commands.add(new Command("Circle") {
            @Override
            public void run(String[] args, Graphics2D g) {
                if (args.length == 1) {
                    int w = Integer.decode(get(args[0]));
                    if (mode == 0) {
                        g.fillOval(x, y, w, w);
                    } else if (mode == 1) {
                        g.fillOval(x - w / 2, y - w / 2, w, w);
                    } else if (mode == 2) {
                        g.fillOval(x - w, y - w, w, w);
                    }
                    width = w;
                    height = w;
                } else {
                    throw new WindowCreatorException("Too many or too few arguments for Circle()");
                }
            }
        });
    }

    /**
     *
     * @since Version 1.1
     *
     * @param updateLife
     */
    public void setUpdateLife(boolean updateLife) {
        this.updateLife = updateLife;
    }

    private void updateLife() {
        if (updateLife) {
            if (configuration.lastModified() > lastModified) {
                update();
                lastModified = configuration.lastModified();
            }
        }
    }

    private void update() {
        try (InputStream inputStream = new FileInputStream(configuration)) {
            byte[] bytes = inputStream.readAllBytes();
            StringBuilder st = new StringBuilder();
            for (byte b : bytes) {
                st.append((char)b);
            }
            tokenize(st.toString());
            init = true;
        } catch (IOException e) {

        }
    }

    private List<Token> tokenizeLine(String input) {
        char[] chars = input.toCharArray();
        StringBuilder st = new StringBuilder();
        List<Token> tokens = new ArrayList<>();

        boolean inString = false;

        for (char c : chars) {
            if (c == '"') {
                inString = !inString;
            }
            if (!inString && (c == '\n' || c == '(' || c == ')' || c == ' ')) {
                if (st.length() > 0) {
                    tokens.add(new Token(st));
                    st = new StringBuilder();
                }
                if (c != ' ' && c != '\n') {
                    tokens.add(new Token(c + ""));
                }
            } else {
                st.append(c);
            }
        }
        if (st.length() != 0) {
            tokens.add(new Token(st));
        }

        return tokens;
    }

    private void tokenize(String input) {
        char[] chars = input.toCharArray();

        StringBuilder st = new StringBuilder();
        List<List<Token>> lines = new ArrayList<>();
        List<Token> tokens = new ArrayList<>();

        boolean inString = false;

        for (char c : chars) {
            if (c == '"') {
                inString = !inString;
            }
            if (!inString && (c == '\n' || c == '(' || c == ')' || c == ' ')) {
                if (st.length() > 0) {
                    tokens.add(new Token(st));
                    st = new StringBuilder();
                }
                if (c == '\n') {
                    lines.add(tokens);
                    tokens = new ArrayList<>();
                }
                if (c != ' ' && c != '\n') {
                    tokens.add(new Token(c + ""));
                }
            } else {
                st.append(c);
            }
        }
        if (st.length() != 0) {
            tokens.add(new Token(st));
        }
        if (!tokens.isEmpty()) {
            lines.add(tokens);
        }

        this.lines = lines;
    }

    private boolean init = true;

    private int x, y = 0;
    private List<Error> errors = new ArrayList<>();
    private List<Warning> warnings = new ArrayList<>();
    private List<Warning> currentWarnings = new ArrayList<>();
    private List<String> selectorUsed = new ArrayList<>();
    private int line = 0;
    private int mode = 0;
    private int weight = 1;
    private boolean comment = false;

    private Point point = new Point(-10000, -10000);
    private boolean clicked = false;
    private boolean hold = false;

    @Override
    protected void paintComponent(Graphics g1) {
        Graphics2D g = (Graphics2D)g1;
        updateLife();

        x = 0;
        y = 0;

        List<Error> errors = new ArrayList<>();
        List<Long> timing = new ArrayList<>();

        line = 0;
        currentWarnings = new ArrayList<>();
        selectorUsed = new ArrayList<>();
        for (List<Token> tokens : lines) {
            long t = System.currentTimeMillis();
            line++;
            if (tokens.isEmpty()) {
                continue;
            }
            try {
                executeLine(g, tokens);
            } catch (Exception e) {
                if (!updateLife) {
                    continue;
                }
                errors.add(new Error(e.getMessage(), line));
            }
            timing.add(System.currentTimeMillis() - t);
        }

        clicked = false;
        init = false;

        if (!updateLife) {
            return;
        }
        int li = 0;
        for (long l : timing) {
            li++;
            if (l > 50) {
                currentWarnings.add(new Warning("Long render time on line " + li + ": ", (int)l));
            }
        }
        for (Error e : this.errors) {
            if (!errors.contains(e)) {
                System.out.println("[Resolved] " + e.getLine() + " -> " + e.getErrorString());
            }
        }
        for (Error e : errors) {
            if (!this.errors.contains(e)) {
                System.out.println("[Error]    " + e.getLine() + " -> " + e.getErrorString());
            }
        }
        if (selectorUsed.size() != selectors.size()) {
            for (Map.Entry<String, String> entry : selectors.entrySet()) {
                if (!selectorUsed.contains(entry.getKey())) {
                    currentWarnings.add(new Warning("Unused selector: @" + entry.getKey(), -1));
                }
            }
        }
        for (Warning e : warnings) {
            if (!currentWarnings.contains(e)) {
                if (e.getLine() > 0) {
                    System.out.println("[Resolved] " + e.getWarningString() + " " + e.getLine());
                } else {
                    System.out.println("[Resolved] " + e.getWarningString());
                }
            }
        }
        for (Warning e : currentWarnings) {
            if (!warnings.contains(e)) {
                if (e.getLine() > 0) {
                    System.out.println("[Warning]  " + e.getWarningString() + " " + e.getLine());
                } else {
                    System.out.println("[Warning]  " + e.getWarningString());
                }
            }
        }
        this.warnings = currentWarnings;
        this.errors = errors;

    }

    private void executeLine(Graphics2D g, List<Token> tokens) {
        if (tokens.isEmpty()) {
            return;
        }
        if (tokens.get(0).getTokenString().startsWith("#")) {
            return;
        }
        if (tokens.get(0).getTokenString().startsWith("//")) {
            return;
        }
        if (tokens.get(0).getTokenString().startsWith("/*")) {
            comment = true;
        }
        if (tokens.get(tokens.size() - 1).getTokenString().endsWith("*/")) {
            comment = false;
            return;
        }
        if (comment) {
            return;
        }

        if (tokens.size() < 3) {
            throw new WindowCreatorException("Unknown Line Pattern");
        }
        if (tokens.get(0).getTokenString().startsWith("?")) {
            if (!init) {
                return;
            }
            if (!tokens.get(1).getTokenString().equals("(")) {
                throw new WindowCreatorException("Missing open bracket");
            }
            if (!tokens.get(tokens.size() - 1).getTokenString().equals(")")) {
                throw new WindowCreatorException("Missing closing bracket");
            }
            List<String> strings = new ArrayList<>();
            for (int i = 2; i < tokens.size() - 1; i++) {
                strings.add(tokens.get(i).getTokenString());
            }
            addSelector(tokens.get(0).getTokenString().substring(1), get(strings.stream().collect(Collectors.joining(" "))));
            return;
        }
        if (tokens.get(0).getTokenString().startsWith("@") || tokens.get(0).getTokenString().startsWith("$")) {
            if (!tokens.get(1).getTokenString().equals("(")) {
                throw new WindowCreatorException("Missing open bracket");
            }
            if (!tokens.get(tokens.size() - 1).getTokenString().equals(")")) {
                throw new WindowCreatorException("Missing closing bracket");
            }
            List<String> strings = new ArrayList<>();
            for (int i = 2; i < tokens.size() - 1; i++) {
                strings.add(tokens.get(i).getTokenString());
            }
            addSelector(tokens.get(0).getTokenString().substring(1), get(strings.stream().collect(Collectors.joining(" "))));
            return;
        }
        for (Command command : commands) {
            if (command.getName().equals(tokens.get(0).getTokenString())) {
                if (!tokens.get(1).getTokenString().equals("(")) {
                    throw new WindowCreatorException("Missing open bracket");
                }
                if (!tokens.get(tokens.size() - 1).getTokenString().equals(")")) {
                    throw new WindowCreatorException("Missing closing bracket");
                }
                List<String> strings = new ArrayList<>();
                for (int i = 2; i < tokens.size() - 1; i++) {
                    strings.add(tokens.get(i).getTokenString());
                }
                command.run(strings.toArray(new String[0]), g);
                return;
            }
        }
        throw new WindowCreatorException("Unknown Token: " + tokens.get(0).getTokenString());
    }

    /**
     *
     * @since Version 1.1
     *
     * @param input
     * @return
     */
    public String get(String input) {
        if (input.startsWith("@")) {
            if (selectors.containsKey(input.substring(1))) {
                selectorUsed.add(input.substring(1));
                return selectors.get(input.substring(1));
            } else {
                if (updateLife) {
                    currentWarnings.add(new Warning("Selector not found: " + input, line));
                }
            }
        }
        if (input.endsWith("%W")) {
            return (int)(getWidth() * (Integer.decode(input.substring(0, input.length() - 2)) / 100.0)) + "";
        }
        if (input.endsWith("%H")) {
            return (int)(getHeight() * (Integer.decode(input.substring(0, input.length() - 2)) / 100.0)) + "";
        }
        if (input.endsWith("%X")) {
            return (int)(x * (Integer.decode(input.substring(0, input.length() - 2)) / 100.0)) + "";
        }
        if (input.endsWith("%Y")) {
            return (int)(y * (Integer.decode(input.substring(0, input.length() - 2)) / 100.0)) + "";
        }

        if (input.endsWith("+W")) {
            return getWidth() + Integer.decode(input.substring(0, input.length() - 2)) + "";
        }
        if (input.endsWith("+H")) {
            return getHeight() + Integer.decode(input.substring(0, input.length() - 2)) + "";
        }
        if (input.endsWith("+X")) {
            return x + Integer.decode(input.substring(0, input.length() - 2)) + "";
        }
        if (input.endsWith("+Y")) {
            return y + Integer.decode(input.substring(0, input.length() - 2)) + "";
        }

        if (input.endsWith("-W")) {
            return getWidth() - Integer.decode(input.substring(0, input.length() - 2)) + "";
        }
        if (input.endsWith("-H")) {
            return getHeight() - Integer.decode(input.substring(0, input.length() - 2)) + "";
        }
        if (input.endsWith("-X")) {
            return x - Integer.decode(input.substring(0, input.length() - 2)) + "";
        }
        if (input.endsWith("-Y")) {
            return y - Integer.decode(input.substring(0, input.length() - 2)) + "";
        }

        if (input.equals(".w") || input.equals(".width")) {
            return getWidth() + "";
        }
        if (input.equals(".h") || input.equals(".height")) {
            return getHeight() + "";
        }
        if (input.equals(".x")) {
            return x + "";
        }
        if (input.equals(".y")) {
            return y + "";
        }

        if (input.equals(".mouseX")) {
            return (int)point.getX() + "";
        }
        if (input.equals(".mouseY")) {
            return (int)point.getY() + "";
        }
        if (input.equals(".clicked")) {
            return clicked + "";
        }
        if (input.equals(".hold")) {
            return hold + "";
        }
        if (input.equals(".init")) {
            return init + "";
        }
        return input;
    }

    /**
     *
     * @since Version 1.1
     *
     * @param name
     * @param value
     */
    public void addSelector(String name, String value) {
        if (value.length() == 0) {
            selectors.remove(name);
            return;
        }
        if (!selectors.containsKey(name)) {
            selectors.put(name, value);
        } else {
            selectors.replace(name, value);
        }
    }

    private class Warning {

        private int line;
        private String warningString;

        public Warning(String warning, int line) {
            this.line = line;
            this.warningString = warning;
        }

        public int getLine() {
            return line;
        }

        public String getWarningString() {
            return warningString;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Warning)) return false;
            Warning warning1 = (Warning) o;
            return Objects.equals(warningString, warning1.warningString);
        }

        @Override
        public int hashCode() {
            return Objects.hash(warningString);
        }

    }

    private class Error {

        private int line;
        private String errorString;

        public Error(String error, int line) {
            this.line = line;
            this.errorString = error;
        }

        public int getLine() {
            return line;
        }

        public String getErrorString() {
            return errorString;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Error)) return false;
            Error error1 = (Error) o;
            return Objects.equals(errorString, error1.errorString);
        }

        @Override
        public int hashCode() {
            return Objects.hash(errorString);
        }

    }

    private class Token {

        private String tokenString = "";

        public Token(String token) {
            this.tokenString = token;
        }

        public Token(StringBuilder token) {
            this.tokenString = token.toString();
        }

        public String getTokenString() {
            return tokenString;
        }

        @Override
        public String toString() {
            if (tokenString.equals("\n")) {
                return "Token{" +
                        "token='\\n" + '\'' +
                        '}';
            }
            return "Token{" +
                    "token='" + tokenString + '\'' +
                    '}';
        }

    }

    private abstract class Command {

        private String name = "";

        public Command(String name) {
            this.name = name;
        }

        public final String getName() {
            return name;
        }

        public void run(String[] args, Graphics2D g) {

        }
    }
}