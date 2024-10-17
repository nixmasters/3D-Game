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

public class Energy extends Group {
    public Energy(double x, double z) {
        Box part1 = new Box(1.0, 4.0, 1.0);
        part1.setRotate(30.0);
        part1.setTranslateY(-1.5);
        part1.setTranslateX(-0.6);
        Box part2 = new Box(4.0, 1.0, 1.0);
        Box part3 = new Box(1.0, 4.0, 1.0);
        part3.setRotate(30.0);
        part3.setTranslateY(1.5);
        part3.setTranslateX(0.6);
        part1.setMaterial(new PhongMaterial(Color.LIGHTYELLOW));
        part2.setMaterial(new PhongMaterial(Color.LIGHTYELLOW));
        part3.setMaterial(new PhongMaterial(Color.LIGHTYELLOW));
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
