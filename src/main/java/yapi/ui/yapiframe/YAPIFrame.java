package yapi.ui.yapiframe;

import yapi.ui.yapiframe.handler.YAPIKeyHandler;
import yapi.ui.yapiframe.handler.YAPIMouseHandler;

import javax.swing.*;
import java.awt.*;

public class YAPIFrame {

    public static void main(String[] args) {
        YAPIFrame yapiFrame = new YAPIFrame();
    }

    private JFrame frame;
    private YAPIComponent component = new YAPIComponent(this);

    private YAPIKeyHandler keyHandler = new YAPIKeyHandler(component);
    private YAPIMouseHandler mouseHandler = new YAPIMouseHandler(component);

    private boolean open = true;

    public YAPIFrame() {
        this(0.5, 0.5);
    }

    public YAPIFrame(int width, int height) {
        initialize();
        setSize(width, height);
    }

    public YAPIFrame(double ratio) {
        this(ratio, ratio);
    }

    public YAPIFrame(double ratioWidth, double ratioHeight) {
        initialize();
        setSize(Toolkit.getDefaultToolkit().getScreenSize().width * ratioWidth, Toolkit.getDefaultToolkit().getScreenSize().height * ratioHeight);
    }

    private void initialize() {
        frame = new JFrame();
        visible();
        resizable();
        exitOnClose();

        frame.addKeyListener(keyHandler);
        frame.addMouseListener(mouseHandler);
        frame.addMouseMotionListener(mouseHandler);
        frame.addMouseWheelListener(mouseHandler);

        frame.setContentPane(component);

        frame.invalidate();

        Runnable runnable = () -> {
            while (open) {
                component.repaint();
                try {
                    Thread.sleep(1000/60);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.setName("Repaint Thread");
        thread.start();
    }

    public void dispose() {
        frame.dispose();
        open = false;
    }

    public void setSize(double width, double height) {
        frame.setSize((int)width, (int)height);
    }

    public void setTitle() {
        setTitle("");
    }

    public void setTitle(String s) {
        frame.setTitle(s);
    }

    public void show() {
        visible();
    }

    public void hide() {
        setVisible(false);
    }

    public void visible() {
        setVisible(true);
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    public void resizable() {
        setResizable(true);
    }

    public void setResizable(boolean resizable) {
        frame.setResizable(resizable);
    }

    public void resetCursor() {
        setCursor(Cursor.getDefaultCursor());
    }

    public void setCursor(Cursor cursor) {
        frame.setCursor(cursor);
    }

    public void decorate() {
        setDecorated(true);
    }

    public void setDecorated(boolean decorated) {
        setUnDecorated(!decorated);
    }

    public void setUnDecorated(boolean unDecorated) {
        frame.setUndecorated(unDecorated);
    }

    public void enableFocusTraversalKeys() {
        setFocusTraversalKeysEnabled(true);
    }

    public void disableFocusTraversalKeys() {
        setFocusTraversalKeysEnabled(false);
    }

    public void setFocusTraversalKeysEnabled(boolean focusTraversalKeysEnabled) {
        frame.setFocusTraversalKeysEnabled(focusTraversalKeysEnabled);
    }

    public void exitOnClose() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void closeToExit() {
        exitOnClose();
    }

    public void hideOnClose() {
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }

    public void closeToHide() {
        hideOnClose();
    }

    public void doNothingOnClose() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    public void closeToDoNothing() {
        doNothingOnClose();
    }

    public void disposeOnClose() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void closeToDispose() {
        disposeOnClose();
    }

    public void defaultCloseOperation() {
        exitOnClose();
    }

    public void setDefaultCloseOperation(int i) {
        frame.setDefaultCloseOperation(i);
    }

}
