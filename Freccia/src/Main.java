import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class Main extends PApplet {
    public static PApplet processing;
    public static PApplet pApplet;
    String url = "jdbc:mysql://localhost:3306/freccia_";
    String user = "root";
    String password = "19911994";
    public static int scoreNumber;
    int startTime;
    static int arrowSpeed=5;
    static int arrowNumbers=20;
    static int arrowNumbers2 = 420;
    static int addX = 2;
    static int bossWin = 0;
    public static PImage knight;
    public static PImage knight2;
    public static PImage injuredKnight;
    public static PImage injuredKnight2;
    public static PImage finalBoss;
    public static PImage bow;
    public static PImage arrow;
    public static PImage quiver;
    public static PImage gameOver;
    public static PImage won;
    public static PImage wallPaper1;
    public static PImage wallPaper2;
    public static PImage settingWallpaper;
    private static int count=0;
//    private static int countExtra=0;
    public static ArrayList<Enemy> knights = new ArrayList<>();
    public static ArrayList<Arrow> arrows = new ArrayList<>();
    public static ArrayList<Extra> extras = new ArrayList<>();
    public static ArrayList<Enemy> boss = new ArrayList<>();
    public static Enemy horseRider;
    private static boolean shoot=true;
//    private static boolean arrowShoot=true;
//    private static boolean knightColor=true;
    int highest1 = 0;
    int highest2 = 0;
    int highest3 = 0;
    int Score;
    int elapsedTime;
    int seconds;
    int minutes;

//    float x = width/2;
//    float y = height/2;
//    long lastTime = millis();

    public void highestScores() {

//            Search in 'data' table
        try {
            Connection connection = DriverManager.getConnection(url,user,password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select Score From data");

//                Putting top three scores in int variables
            while (resultSet.next()) {
                Score = resultSet.getInt("Score");
                if (Score > highest1) {
                    highest3 = highest2;
                    highest2 = highest1;
                    highest1 = Score;
                } else if (Score > highest2) {
                    highest3 = highest2;
                    highest2 = Score;
                } else if (Score > highest3) {
                    highest3 = Score;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void inserT() {
        try {
            Connection connection = DriverManager.getConnection(url,user,password);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO data (Score) VALUES (?)");
            preparedStatement.setInt(1,scoreNumber);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    Color color = new Color(171, 190, 199);

    public static void main(String[] args) {
        PApplet.main("Main", args);
    }

    @Override
    public void setup() {
        processing = this;
        pApplet = this;
        knights.clear();
        arrows.clear();
        extras.clear();
        if(scoreNumber != 0) {
            inserT();
            scoreNumber = 0;
        }
        arrowSpeed = 5;
        arrowNumbers = 20;
        arrowNumbers2 = 420;
        count = 0;
        highest1 = 0;
        highest2 = 0;
        highest3 = 0;
        knight = loadImage("knight.png");
        injuredKnight = loadImage("injured-knight.png");
        knight2 = loadImage("knight2.png");
        injuredKnight2 = loadImage("injured-knight2.png");
        finalBoss=loadImage("finalBoss.png");
        bow = loadImage("bow.png");
        arrow = loadImage("arrow.png");
        quiver = loadImage("quiver.png");
        gameOver = loadImage("gameOver.png");
        won = loadImage("won.png");
        wallPaper1 = loadImage("wallPaper1.jpg");
        wallPaper2 = loadImage("wallPaper2.jpg");
        settingWallpaper = loadImage("setting.jpg");
        Enemy enemy = new Enemy(null, 0, 0, 0);
        enemy.makeKnights();
        Enemy enemy2=new Enemy(null,0,0,0);
        enemy2.makeFinalBoss();
        Extra extra=new Extra(0,0,null);
        extra.makeExtra();
        elapsedTime = millis() - startTime;
        seconds = elapsedTime / 1000;
        minutes = seconds / 60;
//        PFont Font1 = createFont("Arial Bold", 18);
        highestScores();
    }

    @Override
    public void settings() {
        size(500, 700);
    }

    boolean menu = true;
    boolean description = false;
    boolean game = false;
    boolean lost = false;
    boolean win = false;
    boolean setting = false;
    boolean wallPaper = true;
    boolean score = false;
    boolean pressKey = true;

    @Override
    public void draw() {
        if (menu) {
            if (wallPaper) {
                background(wallPaper1);
            } else {
                background(wallPaper2);
            }
            noStroke();
            fill(225);
            rect(45,175,95,30);
            rect(45,275,135,30);
            rect(45,375,205,30);
            rect(45,475,85,30);

            fill(0);
            PFont Font1 = createFont("Arial Bold", 25);
            textFont(Font1);
            text("1. Start",  50, 200);
            text("2. Settings",  50, 300);
            text("3. Highest Score", 50, 400);
            text("4. Exit", 50,500);

            if (mousePressed) {
                if (mouseX > 45 && mouseX < 150 && mouseY > 100 && mouseY < 200) {
                    // Start game
                    menu = false;
                    game = true;
                } else if (mouseX > 45 && mouseX <170 && mouseY > 280 && mouseY < 330) {
                    // Open settings
                    menu = false;
                    setting = true;
                } else if (mouseX > 45 && mouseX <200 && mouseY > 360 && mouseY < 430) {
                    // Show the highest score
                    menu = false;
                    score = true;
                } else if (mouseX > 45 && mouseX <140 && mouseY > 450 && mouseY < 500) {
                    // Exit program
                    exit();
                }
            }

        }

        if (description) {
            Color color = new Color(66, 78, 89);
            background(66, 78, 89);
            fill(225);
//            text("");
        }

        if (game){
            background(228, 247, 255);
            if (knights.size()==0){
                moveFinalBoss();
                for (Enemy e : boss) {
                    showFinalBoss(e.getImage(), e.getX(), e.getY());
                }
                checkCrushedWithHorseRider();
            }
            if (knights.size() > 0 && knights.get(0).getY() >= 600) {
                knights.remove(0);
            }
            if (arrowNumbers == 0) {
                game = false;
                lost = true;
            }

            moveKnights();
            for (Enemy e : knights) {
                showKnights(e.getImage(), e.getX(), e.getY());
            }
            moveExtra();
            for (Extra a : extras) {
                showExtra(a.getImage(), a.getX(), a.getY());
            }
            Arrow.showArrow(arrows);
            Arrow.moveArrow(arrowSpeed);

//            Checking shooting mode
            if (pressKey) {
                if (keyPressed) {
                    if (arrowNumbers == 0) {
                        game = false;
                        lost = true;
                    } else {
                        if(shoot) {
                            Arrow arrow = new Arrow(mouseX, 0);
                            arrow.makeArrow();
                            Arrow.arrowFace();
                            arrowNumbers--;
                        }
                        shoot = false;
                        count++;
                        if (count == 10) {
                            shoot = true;
                            count = 0;
                        }
                    }
                }
            } else {
                if (arrowNumbers2 == 0 ) {
                    game = false;
                    lost = true;
                } else {
                    if(shoot) {
                        Arrow arrow = new Arrow(mouseX, 0);
                        arrow.makeArrow();
                        Arrow.arrowFace();
                        arrowNumbers2--;
                    }
                    shoot = false;
                    count++;
                    if (count == 10) {
                        shoot = true;
                        count = 0;
                    }
                }

            }
            checkCrushedWithKnights();

            if (game) {
                int bowX = mouseX;
                if(bowX>435) bowX = 435;
                image(bow, bowX, 565);
                fill(119, 158, 176);
                stroke(119, 158, 176);
                strokeWeight(10);
                rect(0, 640, 500, 700);
                checkCrushedWithExtra();
                fill(0);
                if (pressKey) {
                    text("Number Of Arrows:" + arrowNumbers, 260, 670);
                } else {
                    text("Number Of Arrows:" + arrowNumbers2, 260, 670);
                }
                text("Score: " + scoreNumber, 40, 660);

                textSize(20);
                text("Time Played: "+minutes+":"+ seconds,40,690);
            }
        }

        if (lost) {
            background(187, 205, 213);

            image(gameOver,50,40,400,250);
            fill(245, 92, 71);
            PFont Font1 = createFont("Arial Bold", 30);
            textFont(Font1);
            text("Final Score: " + scoreNumber, 145, 380);

            text("Replay", 195, 460);
            text("Return to menu", 130, 540);
            text("Exit", 215, 620);

//            setup();

            if (mousePressed) {
                if (mouseX > 170 && mouseX < 360 && mouseY > 430 && mouseY < 490) {
                    setup();

                    lost = false;
                    game = true;
                }
                if (mouseX > 110 && mouseX < 400 && mouseY > 520 && mouseY < 580) {
                    setup();

                    lost = false;
                    menu = true;
                }
                if (mouseX > 190 && mouseX < 340 && mouseY > 600 && mouseY < 660) {
                    exit();
                }
            }
        }

        if (win) {
            background(152, 224, 203);
            Color color = new Color(218, 117, 142);

            image(won,100,40,300,250);
            fill(218, 117, 142);
            PFont Font1 = createFont("Arial Bold", 30);
            textFont(Font1);
            text("Final Score: " + scoreNumber, 145, 380);

            text("Replay", 195, 460);
            text("Return to menu", 130, 540);
            text("Exit", 215, 620);

//            setup();

            if (mousePressed) {
                if (mouseX > 170 && mouseX < 360 && mouseY > 430 && mouseY < 490) {
                    setup();

                    win = false;
                    game = true;
                }
                if (mouseX > 110 && mouseX < 400 && mouseY > 520 && mouseY < 580) {
                    setup();

                    win = false;
                    menu = true;
                }
                if (mouseX > 190 && mouseX < 340 && mouseY > 600 && mouseY < 660) {
                    exit();
                }
            }
        }

        if (setting) {
            background(settingWallpaper);

//            Default form of shooting is by pressing key
//            Here is another option that can be chosen witch is automatic
            fill(225);
            textSize(30);
            text("Mode",200,160);
            textSize(25);
            if (pressKey) {
                text("Automatic",85,230);
                fill(246, 141, 131);
            } else {
                fill(246, 141, 131);
                text("Automatic",85,230);
                fill(225);
            }
            text("Press Key",290,230);

            textSize(30);
            fill(225);
            text("Background",165,400);
            textSize(25);
            if (wallPaper) {
                fill(246, 141, 131);
                text("Background 1",55,470);
                fill(225);
            } else {
                text("Background 1",55,470);
                fill(246, 141, 131);
            }
            text("Background 2",290,470);

            fill(225);
            textSize(30);
            text("back",220,610);

            if (mousePressed) {
                if (mouseX > 70 && mouseX < 220 && mouseY > 200 && mouseY <260){
                    //Choose mode
                    pressKey = false;
                }
                if (mouseX > 280 && mouseX < 400 && mouseY > 200 && mouseY < 260){
                    //Choose mode
                    pressKey = true;
                }
                if (mouseX > 45 && mouseX < 220 && mouseY > 420 && mouseY <490) {
                    //change background
                    wallPaper = true;
                }
                if (mouseX > 285 && mouseX < 440 && mouseY > 420 && mouseY <490) {
                    //change background
                    wallPaper = false;
                }
                if (mouseX > 200 && mouseX < 290 && mouseY > 590 && mouseY <650) {
                    setting = false;
                    menu = true;
                }
            }
        }

        if (score) {
            if (wallPaper) {
                background(wallPaper1);
            } else {
                background(wallPaper2);
            }
            fill(225);
            rect(45,155,400,35);

            fill(0);
            text("Your Three Highest Scores Are :",50,180);

            if (highest1==0 || highest2==0 || highest3==0) {
//                You haven't played enough!
            } else {
                fill(225);
                rect(140,255,80,35);
                rect(140,305,80,35);
                rect(140,355,80,35);

                fill(0);
                text("1) "+highest1,150,280);
                text("2) "+highest2,150,330);
                text("3) "+highest3,150,380);
            }



            textSize(25);
            fill(225);
            rect(210,575,75,35);

            fill(0);
            text("back",220,600);
            if (mousePressed) {
                if (mouseX > 200 && mouseX < 290 && mouseY > 580 && mouseY <640) {
                    score = false;
                    menu = true;
                }
            }
        }
    }

    public void moveFinalBoss() {
        for (Enemy e : boss) {
            e.setY(e.getY() + 1);
            if (e.getX() == 380) {
                addX = -2;
            }
            if (e.getX() == 8) {
                addX = 2;
            }
            if (e.getX() <= 380 && e.getX() >= 8) {
                e.setX(e.getX() + addX);
            }
        }
    }

    public void showFinalBoss(PImage image,int X,int Y){
        image(image,X,Y);
    }

//    Make knights move!
    public void moveKnights() {
        for (Enemy e : knights) {
            e.setY(e.getY() + 2);
        }
    }

    public void showKnights(PImage image, int X, int Y) {
        image(image, X, Y);
    }
    
//    Make extras move!
    public void moveExtra() {
        for (Extra a : extras) {
            a.setY(a.getY() + 2);
        }
    }

    public void showExtra(PImage image, int X, int Y) {
        image(image, X, Y);
    }

//    Remove knights and also Arrows if they crash
    public static void checkCrushedWithKnights() {
//        Two loops for checking each item X,Y
        for (int i=0;i<Main.arrows.size();i++) {
            for(int j=0;j<Main.knights.size();j++){
                if (Main.arrows.get(i).X+45 >= Main.knights.get(j).X && Main.arrows.get(i).getX() <= Main.knights.get(j).getX() + 45 &&
                        Main.arrows.get(i).Y >= Main.knights.get(j).getX() && Main.arrows.get(i).Y <= Main.knights.get(j).getY() + 45) {
//                    Check the resistance and then remove 
                        if (knights.get(j).getResistance()==2) {
                            Main.arrows.remove(Main.arrows.get(i));
                            knights.get(j).setImage(injuredKnight2);
                            knights.get(j).setResistance(1);
                            scoreNumber++;
                        } else if (knights.get(j).getResistance()==1){
                            Main.arrows.remove(Main.arrows.get(i));
                            Main.knights.remove(Main.knights.get(j));
                            scoreNumber++;
                        }
                    break;
                }
            }
        }
    }

//    Collect scores by catching extras and also make them disappear
    public void checkCrushedWithExtra() {
        for (int i=0 ; i<Main.extras.size() ; i++) {
            if (Main.extras.get(i).X - mouseX < 15 && mouseX -  Main.extras.get(i).X < 15 && Main.extras.get(i).Y > 535 && Main.extras.get(i).Y < 630){
                Main.extras.remove(Main.extras.get(i));
                    arrowNumbers+=10;
            }
        }
    }

    public void checkCrushedWithHorseRider() {
        if (Main.boss.get(0).Y >= 510) {
            Main.boss.remove(0);
        }
        for (int i = 0; i < Main.arrows.size(); i++) {
            if (Main.arrows.get(i).X + 25 >= boss.get(0).X && Main.arrows.get(i).getX() + 25 <= boss.get(0).X + 124 &&
                    Main.arrows.get(i).Y >= boss.get(0).Y && Main.arrows.get(i).Y <= boss.get(0).Y + 124) {
                Main.arrows.remove(Main.arrows.get(i));
                if (boss.get(0).getResistance() == 0) {
                    bossWin++;
                    Main.boss.remove(0);
                    scoreNumber += 10;
                    break;
                } else {
                    boss.get(0).setResistance(boss.get(0).getResistance() - 1);
                }

            }

        }
        if (Main.boss.size() == 1) {
            if (Main.boss.get(0).getResistance() == 0) {
                if (bossWin == 2) {
                    game = false;
                    win = true;
                }
                else {
                    game = false;
                    lost = true;
                }
            }
        }
        if (Main.boss.get(Main.boss.size() - 1).Y >= 510) {
            game = false;
            lost = true;
        }
    }
}

