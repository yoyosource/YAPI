package yapi.compression.image;

class CoordinateManager {

    private int x;
    private int y;
    private int count = 0;

    private final int mode;
    private final int maxX;
    private final int maxY;

    CoordinateManager(int mode, int maxX, int maxY) {
        switch (mode) {
            case 0:
            case 1:
                x = 0;
                y = 0;
                break;
            case 2:
            case 3:
                x = maxX - 1;
                y = 0;
                break;
            case 4:
            case 5:
                x = 0;
                y = maxY - 1;
                break;
            case 6:
            case 7:
                x = maxX - 1;
                y = maxY - 1;
                break;
            default:
                throw new UnsupportedOperationException();
        }
        this.mode = mode;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public boolean hasNext() {
        return count < maxX * maxY;
    }

    public void next() {
        if (!hasNext()) return;
        count++;

        switch (mode) {
            case 0:
                x++;
                if (x >= maxX) {
                    x = 0;
                    y++;
                }
                break;
            case 1:
                y++;
                if (y >= maxY) {
                    y = 0;
                    x++;
                }
                break;
            case 2:
                x--;
                if (x < 0) {
                    x = maxX - 1;
                    y++;
                }
                break;
            case 3:
                y++;
                if (y >= maxY) {
                    y = 0;
                    x--;
                }
                break;
            case 4:
                x++;
                if (x >= maxX) {
                    x = 0;
                    y--;
                }
                break;
            case 5:
                y--;
                if (y < 0) {
                    y = maxY - 1;
                    x++;
                }
                break;
            case 6:
                x--;
                if (x < 0) {
                    x = maxX - 1;
                    y--;
                }
                break;
            case 7:
                y--;
                if (y < 0) {
                    y = maxY - 1;
                    x--;
                }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
