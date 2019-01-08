import java.util.Objects;
import java.awt.*;
import java.awt.event.InputEvent;

public class MoveCursor {

    public void mouseMove(int x, int y) {
        try {
            Robot r = new Robot();
            r.mouseMove(x, y);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void mouseGlide(int x, int y, int time, int steps) {
        Point actual = MouseInfo.getPointerInfo().getLocation();
        float xi = actual.x;
        float yi = actual.y;

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

    public void mouseClick(String action) {
        try {
            Robot r = new Robot();
            if (Objects.equals("left", action)) {
                //System.out.println("leftClick");
                // Left Click
                r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            } else if (Objects.equals("right", action)) {
                //System.out.println("rightClick");
                // Right Click
                r.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                r.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mouseMoveWheel(int direction) {
        try {
            Robot r = new Robot();
            r.mouseWheel(direction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
