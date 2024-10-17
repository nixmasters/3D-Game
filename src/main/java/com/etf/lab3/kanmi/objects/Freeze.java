package com.etf.lab3.kanmi.objects;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.util.Duration;

public class Freeze extends Group {
    public Freeze(double x, double z) {
        Box part1 = new Box(0.5, 7.0, 0.5);
        part1.setRotate(30.0);
        Box part2 = new Box(7.0, 0.5, 0.5);
        Box part3 = new Box(0.5, 7.0, 0.5);
        part3.setRotate(-30.0);
        part1.setMaterial(new PhongMaterial(Color.AQUA));
        part2.setMaterial(new PhongMaterial(Color.AQUA));
        part3.setMaterial(new PhongMaterial(Color.AQUA));
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(3.0), this);
        rotateTransition.setAxis(new Point3D(0.0, 1.0, 0.0));
        rotateTransition.setByAngle(360.0);
        rotateTransition.setCycleCount(Integer.MAX_VALUE);
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.play();
        this.setTranslateX(x);
        this.setTranslateZ(z);
        this.getChildren().addAll((Node[])new Node[]{part1, part2, part3});
    }
}
