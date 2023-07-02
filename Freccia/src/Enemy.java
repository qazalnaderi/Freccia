import processing.core.PApplet;
import processing.core.PImage;

public class Enemy {
    private static PApplet p = Main.processing;
    public int X;
    public int Y=-160;
    private PImage image;
    private int resistance;

    public Enemy(PImage image,int X,int Y,int resistance) {
        this.image=image;
        this.X=X;
        this.Y=Y;
        this.resistance=resistance;
    }

    public void makeKnights(){
        for (int i=0;i<25;i++){

            if(i%6==0) {
                Main.knights.add(new Enemy(Main.knight2, X + 30, Y-100,2));
                Y -= 100;
            }
            else{
                Main.knights.add(new Enemy(Main.knight, X + 30, Y-100,1));
                Y -= 100;
            }
            if (i%4==0){
                Main.knights.add(new Enemy(Main.knight2,X+300,Y-60,2));
                Y-=100;
            }
            else{
                Main.knights.add(new Enemy(Main.knight,X+300,Y-60,1));
                Y-=100;
            }

            if(i%2==0)
                X+=100;
            else X-=100;
        }
    }

    public void makeFinalBoss(){
        for (int i=0;i<3;i++) {
            Main.boss.add(new Enemy(Main.finalBoss, 200, Y - 180, 20));
            Y-=550;
        }
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public PImage getImage() {
        return image;
    }

    public int getResistance() {
        return resistance;
    }

    public void setResistance(int resistance) {
        this.resistance = resistance;
    }

    public void setImage(PImage image) {
        this.image = image;
    }
}

