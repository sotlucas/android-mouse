import java.awt.*;

public class MoveCursor {
    /*
    public static void main(String[] args) throws AWTException {
        Robot robot = new Robot();
        while (true) {
            new MoveCursor().mouseGlide(600, 400, 1000, 30);
        }
    }
    */

    public void mouseMove(int x, int y) {
        try {
            Robot r = new Robot();
            r.mouseMove(x, y);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void mouseGlide(int x, int y, int time, int steps) {
        float xi = MouseInfo.getPointerInfo().getLocation().x;
        float yi = MouseInfo.getPointerInfo().getLocation().y;

        try {
            Robot r = new Robot();
            double dx = (x - xi) / ((double) steps);
            double dy = (y - yi) / ((double) steps);
            double dt = time / ((double) steps);
            for (int step = 1; step <= steps; step++) {
                Thread.sleep((int) dt);
                r.mouseMove((int) (xi + dx * step), (int) (yi + dy * step));
            }
        } catch (AWTException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
