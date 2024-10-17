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

public class Health
        extends Group {
    public Health(double x, double z) {
        Box horizontal = new Box(8.0, 2.0, 2.0);
        Box innerHorizontal = new Box(6.0,0.75,2.5);
        Box vertical = new Box(2.0, 8.0, 2.0);
        Box innerVertical = new Box(0.75,6.0,2.5);
        PhongMaterial material = new PhongMaterial(Color.RED);
        PhongMaterial material1 = new PhongMaterial(Color.WHITE);
        horizontal.setMaterial(material);
        vertical.setMaterial(material);
        innerHorizontal.setMaterial(material1);
        innerVertical.setMaterial(material1);

        innerHorizontal.setTranslateY(0.25);
        innerVertical.setTranslateX(0.25);

        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(3.0), this);
        rotateTransition.setAxis(new Point3D(0.0, 1.0, 0.0));
        rotateTransition.setByAngle(360.0);
        rotateTransition.setCycleCount(Integer.MAX_VALUE);
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.play();
        this.getChildren().addAll((Node[])new Node[]{horizontal, vertical,innerVertical,innerHorizontal});
        this.setTranslateX(x);
        this.setTranslateZ(z);
    }
}
