package com.etf.lab3.kanmi;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;


public class HUD extends SubScene {
    private final Text time;
    private final Text immunityTimer;
    private final Text freezeTimer;
    private long currTimeSlice = 0L;
    private long currFTimerSlice = 0L;
    private long currITimerSlice = 0L;
    private int currSeconds = 0;
    private int FcurrSeconds = 10;
    private int IcurrSeconds = 10;
    private final Text score;
    private int currScore = 0;
    private final Text message;
    private final ProgressBar energy;
    private final ProgressBar HP;

    public HUD(double width, double height) {
        super(new Group(), width, height, true, SceneAntialiasing.BALANCED);
        Group root = (Group)super.getRoot();
        double textSize = 0.06 * height;
        this.time = new Text("00:00");
        this.score = new Text("Poeni: 0");
        this.immunityTimer = new Text("00:10");
        this.immunityTimer.setFill(Color.PURPLE);
        this.immunityTimer.setFont(Font.font(textSize));
        this.immunityTimer.setTranslateX(this.score.getBoundsInParent().getWidth() / 2.0 + 50);
        this.immunityTimer.setTranslateY(this.score.getTranslateY() + this.score.getBoundsInParent().getHeight()+60);
        this.immunityTimer.setVisible(false);
        this.freezeTimer = new Text("00:10");
        this.freezeTimer.setFill(Color.BLUE);
        this.freezeTimer.setFont(Font.font(textSize));
        this.freezeTimer.setTranslateX(width - 2.0 * this.time.getBoundsInParent().getWidth() - 113);
        this.freezeTimer.setTranslateY(this.time.getTranslateY() + this.time.getBoundsInParent().getHeight()+60);
        this.freezeTimer.setVisible(false);
        this.time.setFill(Color.RED);
        this.time.setFont(Font.font(textSize));
        this.time.setTranslateX(width - 2.0 * this.time.getBoundsInParent().getWidth());
        this.time.setTranslateY(textSize);

        this.score.setFill(Color.BLUE);
        this.score.setFont(Font.font(textSize));
        this.score.setTranslateX(this.score.getBoundsInParent().getWidth() / 2.0);
        this.score.setTranslateY(textSize);
        this.energy = new ProgressBar(133.33333333333334, 30.0, 333.3333333333333, 10.0);
        this.HP = new ProgressBar(133.33333333333334, 30.0, 333.3333333333333, 50.0 , Color.DARKRED);

        this.message = new Text();
        this.message.setFill(Color.DARKRED);
        this.message.setFont(Font.font("", FontWeight.EXTRA_BOLD, textSize * 2.0));
        this.message.setTranslateX(400.0 - this.score.getBoundsInParent().getWidth() * 1.9);
        this.message.setTranslateY(300.0);
        root.getChildren().addAll((Node[])new Node[]{this.time, this.score, this.energy,this.HP,this.freezeTimer,this.immunityTimer});
    }

    public void end() {
        this.message.setText("KRAJ (" + this.currScore + " poena)");
        ((Group)super.getRoot()).getChildren().add(this.message);
    }

    public void updateEnergy(double increment) {
        double newVal = this.energy.getProgressEnergy() + increment;
        if (newVal < 0.0) {
            newVal = 0.0;
        } else if (newVal > 100.0) {
            newVal = 100.0;
        }
        this.energy.setProgressEnergy(newVal);
    }

    public void updateHP(double increment) {
        double newVal = this.HP.getProgressHP() + increment;
        if (newVal <= 0.0) {
            Player.setGameActive(false);
            end();
        } else if (newVal > 100.0) {
            newVal = 100.0;
        }
        this.HP.setProgressHP(newVal);
    }



    public double getEnergy() {
        return this.energy.getProgressEnergy();
    }
    public double getHP() {
        return this.energy.getProgressHP();
    }

    public void updateTime(long now) {
        if (this.currTimeSlice == 0L) {
            this.currTimeSlice = now;
        } else if ((double)(now - this.currTimeSlice) >= 1.0E9) {
            this.currTimeSlice = now;
            ++this.currSeconds;
            this.time.setText(String.format("%02d:%02d", this.currSeconds / 60, this.currSeconds % 60));
        }
    }

    public void updateFreezeTimer(long now) {
        this.freezeTimer.setVisible(true);
        if (this.currFTimerSlice == 0L) {
            return;
        }
        long elapsedNanoTime =now - this.currFTimerSlice;
        long elapsedTime = elapsedNanoTime / 1_000_000_000;

        if (elapsedTime >= 1) {
            this.FcurrSeconds -= elapsedTime;
            /*if (this.FcurrSeconds < 0) {
                this.FcurrSeconds = 0;
                this.freezeTimer.setVisible(false);

            }*/
            //System.out.println("Freeze : " + this.FcurrSeconds);
            this.freezeTimer.setText(String.format("%02d:%02d", this.FcurrSeconds / 60, this.FcurrSeconds % 60));
            this.currFTimerSlice += elapsedTime * 1_000_000_000;
        }


        if (this.FcurrSeconds <= 0) {
            this.FcurrSeconds = 0;
            this.freezeTimer.setText("00:00");
            this.freezeTimer.setVisible(false);
        }
    }

    public void resetFreezeTimer() {
        this.FcurrSeconds = 10;
        this.freezeTimer.setText("00:10");
        this.freezeTimer.setVisible(false);
        this.currFTimerSlice = System.nanoTime();
    }

    public void updateImmunityTimer(long now) {
        this.immunityTimer.setVisible(true);
        if (this.currITimerSlice == 0L) {
            return;
        }
        long elapsedTime = (now - this.currITimerSlice) / 1_000_000_000;
        if (elapsedTime >= 1) {
            --this.IcurrSeconds;
           // System.out.println("Immunity :" + this.IcurrSeconds);
            this.immunityTimer.setText(String.format("%02d:%02d", this.IcurrSeconds / 60, this.IcurrSeconds % 60));
            this.currITimerSlice += elapsedTime * 1_000_000_000;
            //System.out.println(this.currITimerSlice);
        }


        if (this.IcurrSeconds <= 0) {
            this.IcurrSeconds = 0;
            this.immunityTimer.setText("00:00");
            this.immunityTimer.setVisible(false);
        }
    }

    public void resetImmunityTimer() {
        this.IcurrSeconds = 10;
        this.immunityTimer.setText("00:10");
        this.immunityTimer.setVisible(false);
        this.currITimerSlice = System.nanoTime();
    }
    public void incScore(int inc) {
        this.currScore += inc;
        this.score.setText(String.format("Poeni: %d", this.currScore));
    }

    public static class ProgressBar
            extends Group {
        private final double max = 100.0;
        private final double maxWidth;
        private final Rectangle progress;

        public ProgressBar(double width, double height, double x, double y) {
            this.maxWidth = width - 2.0;
            Rectangle frame = new Rectangle(width, height, Color.TRANSPARENT);
            frame.setStroke(Color.BLACK);
            frame.setStrokeWidth(2.0);
            frame.setStrokeType(StrokeType.OUTSIDE);
            frame.setArcHeight(height / 4.0);
            frame.setArcWidth(height / 4.0);
            Stop[] stops = new Stop[]{new Stop(0.0, Color.GOLDENROD), new Stop(1.0, Color.YELLOW)};
            LinearGradient paint = new LinearGradient(0.0, 0.5, 1.0, 0.5, true, CycleMethod.NO_CYCLE, stops);
            this.progress = new Rectangle(width - 2.0, height - 2.0, paint);
            this.progress.setArcHeight(height / 4.0);
            this.progress.setArcWidth(height / 4.0);
            this.progress.getTransforms().add(new Translate(1.0, 1.0));
            super.getChildren().addAll((Node[])new Node[]{frame, this.progress});
            super.getTransforms().add(new Translate(x, y));
        }

        public ProgressBar(double width, double height, double x, double y, Color color) {
            this.maxWidth = width - 2.0;
            Rectangle frame = new Rectangle(width, height, Color.TRANSPARENT);
            frame.setStroke(Color.BLACK);
            frame.setStrokeWidth(2.0);
            frame.setStrokeType(StrokeType.OUTSIDE);
            frame.setArcHeight(height / 4.0);
            frame.setArcWidth(height / 4.0);
            Stop[] stops = new Stop[]{new Stop(0.0, color), new Stop(1.0, Color.RED)};
            LinearGradient paint = new LinearGradient(0.0, 0.5, 1.0, 0.5, true, CycleMethod.NO_CYCLE, stops);
            this.progress = new Rectangle(width - 2.0, height - 2.0, paint);
            this.progress.setArcHeight(height / 4.0);
            this.progress.setArcWidth(height / 4.0);
            this.progress.getTransforms().add(new Translate(1.0, 1.0));
            super.getChildren().addAll((Node[])new Node[]{frame, this.progress});
            super.getTransforms().add(new Translate(x, y));
        }

        public double getProgressEnergy() {
            return this.progress.getWidth() * 100.0 / this.maxWidth;
        }

        public void setProgressEnergy(double value) {
            this.progress.setWidth(this.maxWidth * value / 100.0);
        }

        public double getProgressHP() {
            return this.progress.getWidth() * 100.0 / this.maxWidth;
        }

        public void setProgressHP(double value) {
            this.progress.setWidth(this.maxWidth * value / 100.0);
        }

    }
}
