package com.etf.lab3.kanmi.objects;


import com.etf.lab3.kanmi.objects.CannonBall;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;

public class Cannon extends Group {
    private boolean left, right, up, down;

    public Cannon(double x, double z) {
        Cylinder outherCylinder = new Cylinder(3, 12);
        Cylinder innerCylinder = new Cylinder(2, 12.5);

        PhongMaterial innMaterial = new PhongMaterial(Color.BLACK);
        innerCylinder.setTranslateX(0); // Ensure it's correctly positioned
        innerCylinder.setTranslateZ(0); // Check if translation affects visibility
        innerCylinder.setTranslateZ(0); // Slightly in front of outer cylinder
        //outherCylinder.setTranslateZ(-2);

        innerCylinder.setMaterial(innMaterial);
        PhongMaterial outMaterial = new PhongMaterial(Color.GREY);
        outherCylinder.setMaterial(outMaterial);

        outherCylinder.setRotate(90);
        innerCylinder.setRotate(90);
        this.getChildren().addAll((Node[]) new Node[]{outherCylinder, innerCylinder});
        this.setRotationAxis(new Point3D(0.0, 1.0, 0.0));

        System.out.println("Outer Cylinder Visible: " + outherCylinder.isVisible());
        System.out.println("Inner Cylinder Visible: " + innerCylinder.isVisible());

        boolean isLeft = x < 0.0 && Math.abs(z) < Math.abs(x);
        boolean isRight = x > 0.0 && Math.abs(z) < Math.abs(x);
        boolean isDown = z < 0.0 && Math.abs(z) > Math.abs(x);
        boolean isUp = z > 0.0 && Math.abs(z) > Math.abs(x);

        // Update instance variables
        this.left = isLeft;
        this.right = isRight;
        this.down = isDown;
        this.up = isUp;

        // Set rotation based on direction
        double rotationAngle = 0.0;
        if (isLeft) {
            rotationAngle = 0.0;
        } else if (isRight) {
            rotationAngle = 180.0;
        } else if (isUp) {
            rotationAngle = 90.0;
        } else if (isDown) {
            rotationAngle = 270.0;
        }

        // Apply the rotation
        this.setRotate(rotationAngle);

        this.setTranslateX(x);
        this.setTranslateZ(z);


    }

    public CannonBall fire() {
        double xSpeed = 0.0;
        double ySpeed = 0.0;
        double zSpeed = 0.0;
        if (this.left) {
            xSpeed = 1.0;
        } else if (this.right) {
            xSpeed = -1.0;
        }
        if (this.down) {
            zSpeed = 1.0;
        } else if (this.up) {
            zSpeed = -1.0;
        }
        Point3D speed = new Point3D(xSpeed, ySpeed, zSpeed);
        double translateX = this.getTranslateX();
        double translateZ = this.getTranslateZ();
        int xDirection = 0;
        int zDirection = 0;
        if (this.left) {
            xDirection = 1;
        } else if (this.right) {
            xDirection = -1;
        }
        if (this.down) {
            zDirection = 1;
        } else if (this.up) {
            zDirection = -1;
        }
        double newX = translateX + (xDirection * 5);
        double newZ = translateZ + (zDirection * 5);
        return new CannonBall(newX, newZ, speed);
    }
}
