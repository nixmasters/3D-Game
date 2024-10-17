package com.etf.lab3.kanmi.objects;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class CannonBall extends Group {

    private Point3D cannonBallSpeed ;

    public CannonBall(double x,double z , Point3D speed){
        Sphere cannonBall = new Sphere(2.0);
        Sphere cannonBallHitbox = new Sphere(2.0);

        cannonBallHitbox.setVisible(false);
        PhongMaterial material = new PhongMaterial(Color.color(0.4039,0.4314,0.4157));
        cannonBall.setMaterial(material);
        this.getChildren().addAll((Node[])new Node[]{cannonBall, cannonBallHitbox});

        this.cannonBallSpeed = speed;
        this.setTranslateX(x);
        this.setTranslateZ(z);
    }

    public void translate(){
        this.setTranslateX(this.getTranslateX()+this.cannonBallSpeed.getX());
        this.setTranslateZ(this.getTranslateZ()+this.cannonBallSpeed.getZ());
    }

}
