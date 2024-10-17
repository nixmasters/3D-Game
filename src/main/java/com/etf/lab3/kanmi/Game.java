package com.etf.lab3.kanmi;

import com.etf.lab3.kanmi.HUD;
import com.etf.lab3.kanmi.Player;
import com.etf.lab3.kanmi.World;
import com.etf.lab3.kanmi.objects.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

public class Game
        extends Application {
    public static final double WINDOW_WIDTH = 800.0;
    public static final double WINDOW_HEIGHT = 600.0;

    private static final Color DEFAULT_BACKGROUND_COLOR = Color.CADETBLUE;
    private static final double HEDGEHOG_SPEED = 0.3;
    private static long coinTimestamp = 0L;
    private static long healthTimestamp = 0L;
    private static long energyTimestamp = 0L;
    private static long hedgehogTimestamp = 0L;
    private static long immunityTimestamp = 0L;
    private static long jokerTimestamp = 0L;
    private static long immunityTimerTimestamp = 0L;

    private static long cannonFire = 0L;

    private static boolean immunity = false;
    private static boolean frozen = false;
    private static long freezeTimestamp = 0L;
    private static long jumpTimestamp = 0L;
    private Group objects;
    private Scene scene;
    private Stage stage;
    private Player player;
    private World world;
    private HUD hud;
    private Ghost ghost;
    private final ArrayList<Hedgehog> hedgehogs = new ArrayList();
    private ArrayList<Cannon> cannonsList = new ArrayList();
    private ArrayList<CannonBall> cannonBalls = new ArrayList();
    private final UpdateTimer timer = new UpdateTimer();

    private void setupScene() {
        Group mainRoot = new Group();
        Group root = new Group();
        this.objects = new Group();
        this.scene = new Scene(mainRoot, 800.0, 600.0, true, SceneAntialiasing.DISABLED);
        this.scene.setCursor(Cursor.NONE);
        this.player = new Player();
        SubScene scene3d = new SubScene(root, 800.0, 600.0, true, SceneAntialiasing.DISABLED);
        scene3d.setFill(DEFAULT_BACKGROUND_COLOR);
        scene3d.setCamera(this.player.getCamera());
        this.scene.setOnMouseMoved(this.player);
        this.scene.setOnKeyPressed(this.player);
        this.scene.setOnKeyReleased(this.player);
        this.world = new World();
        AmbientLight ambientLight = new AmbientLight(Color.DARKGRAY);
        ambientLight.setOpacity(0.2);
        ambientLight.setBlendMode(BlendMode.SOFT_LIGHT);
        PointLight pointLight = new PointLight(Color.WHITESMOKE);
        pointLight.setTranslateY(-100.0);
        this.ghost = new Ghost();
        this.ghost.setTranslateX(200.0);
        this.ghost.setTranslateZ(200.0);
        this.objects.getChildren().add(this.ghost);
        this.generateObstacles();
        this.generateCannons();

        this.generateHedgehogs();
        root.getChildren().addAll((Node[])new Node[]{this.world, this.player, ambientLight, pointLight, this.objects});
        this.hud = new HUD(800.0, 600.0);
        mainRoot.getChildren().addAll((Node[])new Node[]{scene3d, this.hud});
    }

    private void showStage() {
        this.stage.setTitle("Kanmi");
        this.stage.setScene(this.scene);
        this.stage.setResizable(false);
        this.stage.sizeToScene();
        this.stage.show();
        this.timer.start();
    }

    private void updateGhost(long now) {
        double oldX = this.ghost.getTranslateX();
        double oldZ = this.ghost.getTranslateZ();
        double r = Math.sqrt(Math.pow(oldX - this.player.getTranslateX(), 2.0) + Math.pow(oldZ - this.player.getTranslateZ(), 2.0));
        if (oldZ > this.player.getTranslateZ()) {
            this.ghost.setRotate(Math.toDegrees(Math.asin((oldX - this.player.getTranslateX()) / r)));
        } else {
            this.ghost.setRotate(90.0 + Math.toDegrees(Math.acos((oldX - this.player.getTranslateX()) / r)));
        }
        this.ghost.setTranslateX(oldX + 0.32 * Math.cos(Math.toRadians(this.ghost.getRotate() + 90.0)));
        this.ghost.setTranslateZ(oldZ - 0.32 * Math.sin(Math.toRadians(this.ghost.getRotate() + 90.0)));
        for (int i = 0; i < this.objects.getChildren().size(); ++i) {
            Node node = (Node)this.objects.getChildren().get(i);
            if (node instanceof Coin || node instanceof Energy || node instanceof Freeze || node instanceof Health || node instanceof Immunity || node instanceof Joker) {
                if (!node.getBoundsInParent().intersects(this.ghost.getBoundsInParent())) continue;
                this.objects.getChildren().remove(node);
                --i;
                continue;
            }
            if ((!(node instanceof Obstacle) && !(node instanceof LBlock) && !(node instanceof PBlock)) || !node.getBoundsInParent().intersects(this.ghost.getBoundsInParent())) continue;
            this.ghost.setTranslateX(oldX);
            this.ghost.setTranslateZ(oldZ);
        }
    }

    private void updateHedgehog(long now) {
        if (now - hedgehogTimestamp >= 2000000000L) {
            hedgehogTimestamp = now;
            for (Hedgehog h : this.hedgehogs) {
                h.setRotate(new Random().nextDouble() * 360.0);
            }
        }
        for (Hedgehog h : this.hedgehogs) {
            double oldX = h.getTranslateX();
            double oldZ = h.getTranslateZ();
            h.setTranslateX(h.getTranslateX() + 0.3 * Math.cos(Math.toRadians(h.getRotate() - 90.0)));
            h.setTranslateZ(h.getTranslateZ() - 0.3 * Math.sin(Math.toRadians(h.getRotate() - 90.0)));
            if (h.getTranslateX() + 8.0 >= this.world.getBoundsInLocal().getMaxX() || h.getTranslateX() - 8.0 <= this.world.getBoundsInLocal().getMinX()) {
                h.setTranslateX(oldX);
            }
            if (h.getTranslateZ() + 8.0 >= this.world.getBoundsInLocal().getMaxZ() || h.getTranslateZ() - 8.0 <= this.world.getBoundsInLocal().getMinZ()) {
                h.setTranslateZ(oldZ);
            }
            for (int i = 0; i < this.objects.getChildren().size(); ++i) {
                Node node = (Node)this.objects.getChildren().get(i);
                if (node instanceof Coin || node instanceof Energy || node instanceof Freeze || node instanceof Health || node instanceof Immunity || node instanceof Joker) {
                    if (!node.getBoundsInParent().intersects(h.getBoundsInParent())) continue;
                    this.objects.getChildren().remove(node);
                    --i;
                    continue;
                }
                if ((!(node instanceof Obstacle) && !(node instanceof LBlock) && !(node instanceof PBlock)) || !node.getBoundsInParent().intersects(h.getBoundsInParent())) continue;
                h.setTranslateX(oldX);
                h.setTranslateZ(oldZ);
            }
        }
    }

    private void updatePlayer(long now) {
        if (frozen && now - freezeTimestamp >= 10000000000L) {
            frozen = false;

        }
        //this.hud.updateFreezeTimer(now);
        double oldX = this.player.getTranslateX();
        double oldZ = this.player.getTranslateZ();
        double oldY = this.player.getTranslateY();
        boolean moved = false;

        boolean jumpStarted = false;
        if ( jumpTimestamp == 0 && player.getPlayerSpeed() >0 ) {
            jumpTimestamp = now;
            jumpStarted = true;
            moved = true;
        }
        if(jumpTimestamp != 0 ) {

            long dt = now - jumpTimestamp;
            double ds = player.getPlayerSpeed()/300000000L * dt - 0.5*9.81 * dt *dt/300000000L/300000000L ;
            if(this.player.isJumpStopped()){
                this.player.setJumpStopped(false);
                this.player.setTranslateY(0);

            }
            else {
                this.player.setTranslateY(-ds);
            }


            if(player.getTranslateY() >= 0 && !jumpStarted){
                player.setTranslateY(0);
                player.setPlayerSpeed(0);
                jumpTimestamp = 0;
                player.setJumpCnt(0);
            }
        }

        if (this.player.isUpPressed()) {
            moved = true;
            this.player.setTranslateX(this.player.getTranslateX() + this.hud.getEnergy() / 100.0 * 0.8 * Math.cos(Math.toRadians(this.player.getRotate() - 90.0)));
            this.player.setTranslateZ(this.player.getTranslateZ() - this.hud.getEnergy() / 100.0 * 0.8 * Math.sin(Math.toRadians(this.player.getRotate() - 90.0)));
        }
        if (this.player.isDownPressed()) {
            moved = true;
            this.player.setTranslateX(this.player.getTranslateX() + this.hud.getEnergy() / 100.0 * 0.8 * Math.cos(Math.toRadians(this.player.getRotate() + 90.0)));
            this.player.setTranslateZ(this.player.getTranslateZ() - this.hud.getEnergy() / 100.0 * 0.8 * Math.sin(Math.toRadians(this.player.getRotate() + 90.0)));
        }
        if (this.player.isLeftPressed()) {
            moved = true;
            this.player.setTranslateX(this.player.getTranslateX() - this.hud.getEnergy() / 100.0 * 0.8 * Math.cos(Math.toRadians(this.player.getRotate())));
            this.player.setTranslateZ(this.player.getTranslateZ() + this.hud.getEnergy() / 100.0 * 0.8 * Math.sin(Math.toRadians(this.player.getRotate())));
        }
        if (this.player.isRightPressed()) {
            moved = true;
            this.player.setTranslateX(this.player.getTranslateX() + this.hud.getEnergy() / 100.0 * 0.8 * Math.cos(Math.toRadians(this.player.getRotate())));
            this.player.setTranslateZ(this.player.getTranslateZ() - this.hud.getEnergy() / 100.0 * 0.8 * Math.sin(Math.toRadians(this.player.getRotate())));
        }
        if (this.player.getTranslateX() + 10.0 >= this.world.getBoundsInLocal().getMaxX() || this.player.getTranslateX() - 10.0 <= this.world.getBoundsInLocal().getMinX()) {
            this.player.setTranslateX(oldX);
            if (this.player.getTranslateZ() + 10.0 >= this.world.getBoundsInLocal().getMaxZ() || this.player.getTranslateZ() - 10.0 <= this.world.getBoundsInLocal().getMinZ()) {
                moved = false;
            }
        }
        if (this.player.getTranslateZ() + 10.0 >= this.world.getBoundsInLocal().getMaxZ() || this.player.getTranslateZ() - 10.0 <= this.world.getBoundsInLocal().getMinZ()) {
            this.player.setTranslateZ(oldZ);
        }
        for (int i = 0; i < this.objects.getChildren().size(); ++i) {
            Node node = (Node)this.objects.getChildren().get(i);
            if (node instanceof Coin) {
                if (!node.getBoundsInParent().intersects(this.player.getBoundsInParent())) continue;
                this.objects.getChildren().remove(node);
                if (((Coin)node).getKind().equals((Object)Coin.Kind.GOLD)) {
                    this.hud.incScore(1);
                } else if (((Coin)node).getKind().equals((Object)Coin.Kind.GREEN)) {
                    this.hud.incScore(3);
                } else {
                    this.hud.incScore(5);
                }
                --i;
                continue;
            }
            if (node instanceof Energy) {
                if (!node.getBoundsInParent().intersects(this.player.getBoundsInParent())) continue;
                this.objects.getChildren().remove(node);
                this.hud.updateEnergy(33.0);
                --i;
                continue;
            }
            if(node instanceof CannonBall){
                if (!node.getBoundsInParent().intersects(this.player.getBoundsInParent())) continue;
                this.objects.getChildren().remove(node);
                this.hud.updateHP(-15.0);
                --i;
                continue;

            }
            if (node instanceof Freeze) {
                if (!node.getBoundsInParent().intersects(this.player.getBoundsInParent())) continue;
                this.objects.getChildren().remove(node);
                frozen = true;
                freezeTimestamp = now;
                this.hud.resetFreezeTimer();

                --i;
                continue;
            }
            if (node instanceof Obstacle) {
                if (!node.getBoundsInParent().intersects(this.player.getBoundsInParent())) {
                    continue;
                }

                this.player.setTranslateX(oldX);
                this.player.setTranslateZ(oldZ);
                this.player.setTranslateY(oldY);
                moved = false;
                continue;
            }

            if (node instanceof Health) {
                if (!node.getBoundsInParent().intersects(this.player.getBoundsInParent())) continue;
                this.objects.getChildren().remove(node);

                this.hud.updateHP(25.0);

                --i;
                continue;
            }
            if (node instanceof Immunity) {
                if (!node.getBoundsInParent().intersects(this.player.getBoundsInParent())) continue;
                this.objects.getChildren().remove(node);

                immunity = true;
                immunityTimerTimestamp = now;
                this.hud.resetImmunityTimer();

                --i;
                continue;
            }
            if(node instanceof PBlock) {
                if (!node.getBoundsInParent().intersects(this.player.getBoundsInParent())) {
                    continue;
                }

                this.player.setTranslateX(oldX);
                this.player.setTranslateZ(oldZ);
                this.player.setTranslateY(oldY);
                moved = false;
                continue;
            }
            if(node instanceof LBlock) {
                if (!node.getBoundsInParent().intersects(this.player.getBoundsInParent())) {
                    continue;
                }

                this.player.setTranslateX(oldX);
                this.player.setTranslateZ(oldZ);
                this.player.setTranslateY(oldY);
                moved = false;
                continue;
            }
            if (node instanceof Joker){
                if (!node.getBoundsInParent().intersects(this.player.getBoundsInParent())) continue;
                this.objects.getChildren().remove(node);
                double rand = Math.random();

                if(rand <=0.4){
                    double randPoen = Math.random();

                    if(randPoen <=0.1){
                        this.hud.incScore(1);
                        System.out.println("Poeni "+ 1);
                    }

                    else if (randPoen>0.1 && randPoen <=0.2){
                        this.hud.incScore(2);
                        System.out.println("Poeni "+ 2);
                    }

                    else if (randPoen>0.2 && randPoen <=0.3){
                        this.hud.incScore(3);
                        System.out.println("Poeni "+ 3);
                    }

                    else if (randPoen>0.3 && randPoen <=0.4){
                        this.hud.incScore(4);
                        System.out.println("Poeni "+ 4);
                    }

                    else if (randPoen>0.4 && randPoen <=0.5){
                        this.hud.incScore(5);
                        System.out.println("Poeni "+ 5);
                    }

                    else if (randPoen>0.5 && randPoen <=0.6){
                        this.hud.incScore(6);
                        System.out.println("Poeni "+ 6);
                    }

                    else if (randPoen>0.6 && randPoen <=0.7){
                        this.hud.incScore(7);
                        System.out.println("Poeni "+ 7);
                    }

                    else if (randPoen>0.7 && randPoen <=0.8){
                        this.hud.incScore(8);
                        System.out.println("Poeni "+ 8);
                    }

                    else if (randPoen>0.8 && randPoen <=0.9){
                        this.hud.incScore(9);
                        System.out.println("Poeni "+ 9);
                    }

                    else{
                        this.hud.incScore(10);
                        System.out.println("Poeni "+ 10);
                    }


                } else if (rand>0.4 && rand <=0.6) {
                    this.hud.updateEnergy(-20);
                    this.hud.updateHP(20);
                    System.out.println("Izgubljeno energije "+ 20 + " Dobijeno zivotnih poena " + 20);

                } else if (rand>0.6 && rand <=0.8) {
                    this.hud.updateEnergy(20);
                    this.hud.updateHP(-20);
                    System.out.println("Izgubljeno zivotnih poena "+ 20 + " Dobijeno energije " + 20);
                } else if (rand >0.8 && rand <=0.9) {
                    frozen = true;
                    freezeTimestamp = now;
                    this.hud.resetFreezeTimer();
                    System.out.println("Zapocet freeze");
                }
                else {
                    immunity = true;
                    immunityTimerTimestamp = now;
                    this.hud.resetImmunityTimer();
                    System.out.println("Zapocet immunity");
                }
                --i;
                continue;
            }

            if (!(node instanceof Hedgehog) && !(node instanceof Ghost) || !node.getBoundsInParent().intersects(this.player.getBoundsInParent()) || immunity == true) continue;
            else {
                this.hud.updateHP(-0.1);
                if(this.hud.getHP() == 0.0){
                    Player.setGameActive(false);
                    this.hud.end();
                }
            }

        }
        if (moved) {
            this.hud.updateEnergy(-0.02);
        } else {
            this.hud.updateEnergy(0.03);
        }
    }
    boolean leftWallChosen,rightWallChosen,upWallChosen,downWallChosen;
    private void generateCannons() {
        double rand = Math.random();

        if(rand < 0.25){
             leftWallChosen = true;
        } else if (rand>=0.25 && rand < 0.5) {
             rightWallChosen = true;
        } else if (rand>=0.5 && rand < 0.75) {
            upWallChosen = true;
        }
        else {
            downWallChosen = true;
        }
        for (int i = 0; i < 2; ++i) {
            double xPosition;
            double yPosition;
            if (leftWallChosen) {
                xPosition = -300.0;
            } else if (rightWallChosen) {
                xPosition = 300.0;
            } else {
                xPosition = -100.0 + i * 200.0;
            }
            if (upWallChosen) {
                yPosition = -300.0;
            } else if (downWallChosen) {
                yPosition = 300.0;
            } else {
                yPosition = -100.0 + i * 200.0;
            }
            Cannon cannon = new Cannon(xPosition, yPosition);
            this.objects.getChildren().add(cannon);
            this.cannonsList.add(cannon);
        }


    }

    private void cannonFire(long now) {
        if (now - cannonFire >= 5000000000L) { // 5 seconds have passed
            cannonFire = now;
            for (int i = 0; i < cannonsList.size(); i++) {
                Cannon cannon = cannonsList.get(i);
                CannonBall cannonBall = cannon.fire(); // fire() returns a single CannonBall
                cannonBall.translate();
                Game.this.objects.getChildren().add(cannonBall); // Add to scene graph
                Game.this.cannonBalls.add(cannonBall); // Add to list of cannonballs
            }
        }
    }

    private void generateEnergy(long now) {
        if (now - energyTimestamp >= 12000000000L) {
            energyTimestamp = now;
            boolean ok = false;
            block0: while (!ok) {
                double x = new Random().nextDouble() * 550.0 - 300.0 + 20.0;
                double z = new Random().nextDouble() * 550.0 - 300.0 + 20.0;
                Energy energy = new Energy(x, z);
                this.objects.getChildren().add(energy);
                ok = true;
                for (int i = 0; i < this.objects.getChildren().size(); ++i) {
                    Node node = (Node)this.objects.getChildren().get(i);
                    if (node.equals(energy) || !node.getBoundsInParent().intersects(energy.getBoundsInParent())) continue;
                    this.objects.getChildren().remove(energy);
                    ok = false;
                    continue block0;
                }
            }
        }
    }

    private void generateCoinAndFreeze(long now) {
        if (now - coinTimestamp >= 5000000000L) {
            Node node;
            int i;
            coinTimestamp = now;
            boolean ok = false;
            block0: while (!ok) {
                double x = new Random().nextDouble() * 550.0 - 300.0 + 20.0;
                double z = new Random().nextDouble() * 550.0 - 300.0 + 20.0;
                double kind = new Random().nextDouble();
                Coin coin = kind < 0.2 ? new Coin(x, z, Coin.Kind.BLUE) : (kind < 0.5 ? new Coin(x, z, Coin.Kind.GREEN) : new Coin(x, z, Coin.Kind.GOLD));
                this.objects.getChildren().add(coin);
                ok = true;
                for (i = 0; i < this.objects.getChildren().size(); ++i) {
                    node = (Node)this.objects.getChildren().get(i);
                    if (node.equals(coin) || !node.getBoundsInParent().intersects(coin.getBoundsInParent())) continue;
                    this.objects.getChildren().remove(coin);
                    ok = false;
                    continue block0;
                }
            }
            double generateFreeze = new Random().nextDouble();
            if (generateFreeze < 0.3) {
                ok = false;
                block2: while (!ok) {
                    double x = new Random().nextDouble() * 550.0 - 300.0 + 20.0;
                    double z = new Random().nextDouble() * 550.0 - 300.0 + 20.0;
                    Freeze freeze = new Freeze(x, z);
                    this.objects.getChildren().add(freeze);
                    ok = true;
                    for (i = 0; i < this.objects.getChildren().size(); ++i) {
                        node = (Node)this.objects.getChildren().get(i);
                        if (node.equals(freeze) || !node.getBoundsInParent().intersects(freeze.getBoundsInParent())) continue;
                        this.objects.getChildren().remove(freeze);
                        ok = false;
                        continue block2;
                    }
                }
            }



        }
    }
    private void generateHealth(long now){
        if(now - healthTimestamp >= 10000000000L){
            Node node;
            int i;
            healthTimestamp = now;
            boolean ok = false ;

            block: while(!ok){
                double generateHealth = new Random().nextDouble();
                if (generateHealth <= 0.2) {
                    ok=false;

                        double x = new Random().nextDouble() * 550.0 - 300.0 + 20.0;
                        double z = new Random().nextDouble() * 550.0 - 300.0 + 20.0;
                         Health health = new Health(x, z);
                        this.objects.getChildren().add(health);
                        ok = true;
                        for (i = 0; i < this.objects.getChildren().size(); ++i) {
                            node = (Node)this.objects.getChildren().get(i);
                            if (node.equals(health) || !node.getBoundsInParent().intersects(health.getBoundsInParent())) continue;
                            this.objects.getChildren().remove(health);
                            ok = false;
                            continue block;
                        }

                }
            }

        }

    }

    private void generateImmunity(long now){
        if(now - immunityTimestamp >= 10000000000L){
            Node node;
            int i;

            immunityTimestamp = now;
            boolean ok = false ;

            block: while(!ok){
                double generateImmunity = new Random().nextDouble();

                if (generateImmunity <= 0.1) {
                    ok=false;

                    double x = new Random().nextDouble() * 550.0 - 300.0 + 20.0;
                    double z = new Random().nextDouble() * 550.0 - 300.0 + 20.0;
                    Immunity immunity1 = new Immunity(x, z);
                    this.objects.getChildren().add(immunity1);
                    ok = true;
                    for (i = 0; i < this.objects.getChildren().size(); ++i) {
                        node = (Node)this.objects.getChildren().get(i);
                        if (node.equals(immunity1) || !node.getBoundsInParent().intersects(immunity1.getBoundsInParent())) continue;
                        this.objects.getChildren().remove(immunity1);
                        ok = false;
                        continue block;
                    }

                }
            }

        }

    }
    private void generateJoker(long now){
        if(now - jokerTimestamp >= 15000000000L){
            Node node;
            int i;

            jokerTimestamp = now;
            boolean ok = false ;

            block: while(!ok){
                double generateJoker = new Random().nextDouble();

                if (generateJoker <= 1) {
                    ok=false;

                    double x = new Random().nextDouble() * 550.0 - 300.0 + 20.0;
                    double z = new Random().nextDouble() * 550.0 - 300.0 + 20.0;
                    Joker joker = new Joker(x, z);
                    this.objects.getChildren().add(joker);
                    ok = true;
                    for (i = 0; i < this.objects.getChildren().size(); ++i) {
                        node = (Node)this.objects.getChildren().get(i);
                        if (node.equals(joker) || !node.getBoundsInParent().intersects(joker.getBoundsInParent())) continue;
                        this.objects.getChildren().remove(joker);
                        ok = false;
                        continue block;
                    }

                }
            }

        }

    }
    private void generateObstacles() {
        PBlock pBlock = new PBlock(50, 50);
        this.objects.getChildren().add(pBlock);
        for (int j = 0; j < 15; ++j) {
            if (Math.random() <= 0.333) {
                boolean ok = false;
                block1:
                while (!ok) {
                    double x = new Random().nextDouble() * (600.0 - Obstacle.size) - 300.0 + 20.0;
                    double z = new Random().nextDouble() * (600.0 - Obstacle.size) - 300.0 + 20.0;
                    Obstacle obstacle = new Obstacle(x, z);
                    this.objects.getChildren().add(obstacle);
                    ok = true;
                    for (int i = 0; i < this.objects.getChildren().size(); ++i) {
                        Node node = (Node) this.objects.getChildren().get(i);
                        if (node.equals(obstacle) || !node.getBoundsInParent().intersects(obstacle.getBoundsInParent()))
                            continue;
                        this.objects.getChildren().remove(obstacle);
                        ok = false;
                        continue block1;
                    }
                }
            } else if (Math.random() < 0.666) {
                boolean ok = false;
                block2:
                while (!ok) {
                    double x = new Random().nextDouble() * (600.0 - Obstacle.size) - 300.0 + 20.0;
                    double z = new Random().nextDouble() * (600.0 - Obstacle.size) - 300.0 + 20.0;
                    LBlock box1 = new LBlock(x, z);
                    this.objects.getChildren().add(box1);
                    ok = true;
                    for (int i = 0; i < this.objects.getChildren().size(); ++i) {
                        Node node = (Node) this.objects.getChildren().get(i);
                        if (node.equals(box1) || !node.getBoundsInParent().intersects(box1.getBoundsInParent()))
                            continue;
                        this.objects.getChildren().remove(box1);
                        ok = false;
                        continue block2;
                    }
                }
            } else {
                boolean ok = false;
                block3:
                while (!ok) {
                    double x = new Random().nextDouble() * (600.0 - Obstacle.size) - 300.0 + 20.0;
                    double z = new Random().nextDouble() * (600.0 - Obstacle.size) - 300.0 + 20.0;
                    PBlock box1 = new PBlock(x, z);
                    this.objects.getChildren().add(box1);
                    ok = true;
                    for (int i = 0; i < this.objects.getChildren().size(); ++i) {
                        Node node = (Node) this.objects.getChildren().get(i);
                        if (node.equals(box1) || !node.getBoundsInParent().intersects(box1.getBoundsInParent()))
                            continue;
                        this.objects.getChildren().remove(box1);
                        ok = false;
                        continue block3;
                    }
                }

            }
        }
    }

    private void generateHedgehogs() {
        for (int j = 0; j < 3; ++j) {
            boolean ok = false;
            while (!ok) {
                double x = new Random().nextDouble() * 550.0 - 300.0 + 20.0;
                double z = new Random().nextDouble() * 550.0 - 300.0 + 20.0;
                Hedgehog hedgehog = new Hedgehog(x, z);
                hedgehog.setRotationAxis(new Point3D(0.0, 1.0, 0.0));
                this.objects.getChildren().add(hedgehog);
                ok = true;
                for (int i = 0; i < this.objects.getChildren().size(); ++i) {
                    Node node = (Node)this.objects.getChildren().get(i);
                    if (node.equals(hedgehog) || !node.getBoundsInParent().intersects(hedgehog.getBoundsInParent())) continue;
                    this.objects.getChildren().remove(hedgehog);
                    ok = false;
                    break;
                }
                if (!ok) continue;
                this.hedgehogs.add(hedgehog);
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        this.setupScene();
        this.showStage();
    }

    public static void main(String[] args) {
        Game.launch(args);
    }

    private class UpdateTimer
            extends AnimationTimer {
        private UpdateTimer() {
        }

        @Override
        public void handle(long now) {
            if (Player.isGameActive()) {
                Game.this.generateCoinAndFreeze(now);
                Game.this.generateEnergy(now);
                Game.this.generateHealth(now);
                Game.this.generateImmunity(now);
                Game.this.generateJoker(now);
                Game.this.cannonFire(now);

                for (CannonBall cannonBall : Game.this.cannonBalls) {
                    cannonBall.translate(); // Move the cannonball
                    // Add collision detection or boundary checks here if needed
                }

                if (!frozen) {
                   Game.this.updateHedgehog(now);
                   Game.this.updateGhost(now);
                }
                if(freezeTimestamp!=0){
                   Game.this.hud.updateFreezeTimer(now);
                }
                if(immunityTimerTimestamp!=0){
                    Game.this.hud.updateImmunityTimer(now);
                }

                if (now - immunityTimerTimestamp >= 10000000000L ){

                    immunity = false;


                }

                Game.this.updatePlayer(now);
                Game.this.hud.updateTime(now);
            }
        }
    }



    private void updateObjectsDown() {
        for (int i = 0; i < this.objects.getChildren().size(); ++i) {
            Node node = (Node) this.objects.getChildren().get(i);
            if (node instanceof Coin){
                node.setTranslateY(0);

            }
        }

        this.player.setTranslateY(0);
    }
}