import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

public class Arrow extends PApplet {
    static int speedY;
    public int Y;
    public int X;
    private static PImage image;

    private static PApplet pApplet = Main.pApplet;

    public Arrow(int X, int Y) {
        this.X = X;
        this.Y = Y;
    }

    public void makeArrow() {
//        for (int i=0 ; i<20 ; i++) {
        Main.arrows.add(new Arrow(pApplet.mouseX,520));
//        }
    }

    public static void showArrow(ArrayList<Arrow> knives) {
        for (Arrow arrow : knives) {
            Main.pApplet.image(image, arrow.getX(), arrow.getY(),45,45);
        }
    }

    public static void moveArrow(int speedY) {
        for (Arrow arrow : Main.arrows) {
            arrow.Y -= speedY;
        }
    }

    public static void arrowFace() {
        image = Main.pApplet.loadImage("arrow.png");
    }

    public int getX() {
        return X ;
    }

    public int getY() {
        return Y;
    }
}
