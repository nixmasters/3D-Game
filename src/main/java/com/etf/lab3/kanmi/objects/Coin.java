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

public class Coin extends Group
{
    private final Kind kind;

    public Coin(double x, double z, Kind kind) {
        this.kind = kind;
        Cylinder outerCylinder = new Cylinder(4.0, 1.0);
        if (kind.equals((Object)Kind.GOLD)) {

            outerCylinder.setMaterial(new PhongMaterial(Color.GOLD));
            outerCylinder.setRotate(90.0);
            Cylinder innerCylinder = new Cylinder(3.0, 1.0);
            innerCylinder.setMaterial(new PhongMaterial(Color.GOLDENROD));
            innerCylinder.setRotate(90.0);
            this.getChildren().addAll((Node[])new Node[]{outerCylinder, innerCylinder});
        } else if (kind.equals((Object)Kind.GREEN)) {

            outerCylinder.setMaterial(new PhongMaterial(Color.LIME));
            outerCylinder.setRotate(90.0);
            Cylinder innerCylinder = new Cylinder(3.4, 1.0);
            innerCylinder.setMaterial(new PhongMaterial(Color.GREEN));
            innerCylinder.setRotate(90.0);
            this.getChildren().addAll((Node[])new Node[]{outerCylinder, innerCylinder});
        } else if (kind.equals((Object)Kind.BLUE)) {

            outerCylinder.setMaterial(new PhongMaterial(Color.ROYALBLUE));
            outerCylinder.setRotate(90.0);
            Cylinder innerCylinder = new Cylinder(3.8, 1.0);
            innerCylinder.setMaterial(new PhongMaterial(Color.BLUE));
            innerCylinder.setRotate(90.0);
            this.getChildren().addAll((Node[])new Node[]{outerCylinder, innerCylinder});
        }
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(3.0), this);
        rotateTransition.setAxis(new Point3D(0.0, 1.0, 0.0));
        rotateTransition.setByAngle(360.0);
        rotateTransition.setCycleCount(Integer.MAX_VALUE);
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.play();
        this.setTranslateX(x);
        this.setTranslateZ(z);
    }

    public Kind getKind() {
        return this.kind;
    }

    public static enum Kind {
        GOLD,
        GREEN,
        BLUE;

    }
}
