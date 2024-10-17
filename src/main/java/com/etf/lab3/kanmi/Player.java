package com.etf.lab3.kanmi;

import com.etf.lab3.kanmi.objects.Obstacle;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Sphere;

public class Player extends Group implements EventHandler<Event>
{
    public static final double NEAR_CLIP = 0.1;
    public static final double FAR_CLIP = 10_000;
    public static final double FIELD_OF_VIEW = 60;
    public static final double PLAYER_SPEED = 0.8;
    public static final double PLAYER_RADIUS = 20;
    public static double PLAYER_SPEED_Y =0;
    public static double MAX_JUMP_HEIGHT = Obstacle.size/2;


    private final PerspectiveCamera camera;

    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean jumpStopped = false;

    private boolean rightPressed = false;
    private static boolean isGameActive = true;
    private static int jumpCnt = 0;

    public Player()
    {
        Sphere shape = new Sphere(PLAYER_RADIUS);
        shape.setVisible(false);

        camera = new PerspectiveCamera(true);
        camera.setNearClip(NEAR_CLIP);
        camera.setFarClip(FAR_CLIP);
        camera.setFieldOfView(FIELD_OF_VIEW);
       // camera.setTranslateZ(-50.0);

        setRotationAxis(new Point3D(0, 1, 0));
        this.getChildren().addAll(shape, camera);
    }

    @Override
    public void handle(Event event)
    {
        if (event instanceof KeyEvent keyEvent)
        {
            if (keyEvent.getCode() == KeyCode.ESCAPE && keyEvent.getEventType() == KeyEvent.KEY_PRESSED)
                System.exit(0);
            else
            {
                if (!isGameActive)
                {
                    upPressed = false;
                    downPressed = false;
                    rightPressed = false;
                    leftPressed = false;
                    PLAYER_SPEED_Y = 0;
                    jumpStopped = false;
                    return;
                }
                if(keyEvent.getCode() == KeyCode.Q){
                   jumpStopped = true;
                }
                if (keyEvent.getCode() == KeyCode.W || keyEvent.getCode() == KeyCode.UP)
                {
                    if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED)
                        upPressed = true;
                    else if (keyEvent.getEventType() == KeyEvent.KEY_RELEASED)
                        upPressed = false;
                }
                else if (keyEvent.getCode() == KeyCode.S || keyEvent.getCode() == KeyCode.DOWN)
                {
                    if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED)
                        downPressed = true;
                    else if (keyEvent.getEventType() == KeyEvent.KEY_RELEASED)
                        downPressed = false;
                }
                else if (keyEvent.getCode() == KeyCode.A || keyEvent.getCode() == KeyCode.LEFT)
                {
                    if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED)
                        leftPressed = true;
                    else if (keyEvent.getEventType() == KeyEvent.KEY_RELEASED)
                        leftPressed = false;
                }
                else if (keyEvent.getCode() == KeyCode.D || keyEvent.getCode() == KeyCode.RIGHT)
                {
                    if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED)
                        rightPressed = true;
                    else if (keyEvent.getEventType() == KeyEvent.KEY_RELEASED)
                        rightPressed = false;
                }
                else if (keyEvent.getCode() == KeyCode.SPACE )
                {
                    if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED ){
                        if(jumpCnt < 1){
                            PLAYER_SPEED_Y = Math.sqrt(2.0 * 9.81 * (MAX_JUMP_HEIGHT-getTranslateY())) ;
                            jumpCnt++;
                        }
                    }

                }
            }
        }
        else if (event instanceof MouseEvent mouseEvent)
        {
            if (MouseEvent.MOUSE_MOVED.equals(mouseEvent.getEventType()))
                setRotate(mouseEvent.getSceneX() * 390. / getScene().getWidth() - 195.);
        }
    }

    public Camera getCamera()
    {
        return camera;
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }
    public boolean isJumpStopped(){
        return jumpStopped;
    }
    public void setJumpStopped( boolean stop){
       jumpStopped =  stop;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public static boolean isGameActive() {
        return isGameActive;
    }

    public static void setGameActive(boolean gameActive) {
        isGameActive = gameActive;
    }

    public double getPlayerSpeed(){
        return PLAYER_SPEED_Y;
    }

    public void setPlayerSpeed(int i) {
        PLAYER_SPEED_Y = i ;
    }
    public void setJumpCnt (int i){
        jumpCnt = i;
    }
}
