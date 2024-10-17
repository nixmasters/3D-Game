package com.etf.lab3.kanmi.objects;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.util.Duration;

public class Immunity extends Group {

    public Immunity(double x , double z) {
        Cylinder outerCylinder = new Cylinder(2.5, 1.0);
        outerCylinder.setMaterial(new PhongMaterial(Color.SILVER));
        outerCylinder.setRotate(90.0);

        Cylinder innerCylinder = new Cylinder(1.0, 5.0);
        innerCylinder.setMaterial(new PhongMaterial(Color.SILVER));


        this.getChildren().addAll((Node[])new Node[]{outerCylinder,innerCylinder});

        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(3.0), this);
        rotateTransition.setAxis(new Point3D(0.0, 1.0, 0.0));
        rotateTransition.setByAngle(360.0);
        rotateTransition.setCycleCount(Integer.MAX_VALUE);
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.play();
        this.setTranslateX(x);
        this.setTranslateZ(z);
    }
}
