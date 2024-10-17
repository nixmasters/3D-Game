package com.etf.lab3.kanmi.objects;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

public class Ghost extends Group {
    public Ghost() {
        Cylinder body = new Cylinder(4.0, 6.0);
        Sphere head = new Sphere(4.0);
        head.setTranslateY(-3.0);
        PhongMaterial material = new PhongMaterial(Color.DARKRED);
        material.setSpecularColor(Color.RED);
        body.setMaterial(material);
        head.setMaterial(material);
        Cylinder eye1 = new Cylinder(1.0, 1.0);
        eye1.setMaterial(new PhongMaterial(Color.BLACK));
        eye1.getTransforms().addAll((Transform[])new Transform[]{new Rotate(90.0, new Point3D(0.0, 1.0, 0.0)), new Rotate(90.0, new Point3D(0.0, 0.0, 1.0)), new Translate(-2.0, -3.5, -1.5)});
        Cylinder eye2 = new Cylinder(1.0, 1.0);
        eye2.setMaterial(new PhongMaterial(Color.BLACK));
        eye2.getTransforms().addAll((Transform[])new Transform[]{new Rotate(90.0, new Point3D(0.0, 1.0, 0.0)), new Rotate(90.0, new Point3D(0.0, 0.0, 1.0)), new Translate(-2.0, -3.5, 1.5)});
        this.setRotationAxis(new Point3D(0.0, 1.0, 0.0));
        this.setTranslateY(7.0);
        this.getChildren().addAll((Node[])new Node[]{body, head, eye1, eye2});
    }
}
