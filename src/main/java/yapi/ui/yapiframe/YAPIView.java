package yapi.ui.yapiframe;

import java.awt.*;

public class YAPIView {

    private static long currentID = 0;

    private static long createID() {
        return currentID++;
    }

    private static long id = 0;

    private final long getId() {
        return id;
    }

    public YAPIView() {
        id = createID();
    }

    public void paint(Graphics2D g) {

    }

}
