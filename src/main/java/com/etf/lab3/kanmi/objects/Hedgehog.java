package com.etf.lab3.kanmi.objects;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

public class Hedgehog extends Group {
    public static final float SIZE = 8.0f;

    public Hedgehog(double x, double z) {
        Sphere sphere = new Sphere(7.0);
        PhongMaterial material = new PhongMaterial(Color.color(0.15, 0.15, 0.15));
        material.setSpecularColor(Color.WHITESMOKE);
        sphere.setMaterial(material);
        TriangleMesh mesh = new TriangleMesh();
        float[] points = new float[]{-1.0f, -2.0f, -1.0f, 1.0f, -2.0f, -1.0f, 1.0f, -2.0f, 1.0f, -1.0f, -2.0f, 1.0f, 0.0f, -4.0f, 0.0f};
        float[] texPoints = new float[]{0.5f, 0.5f};
        int[] triangles = new int[]{0, 0, 1, 0, 4, 0, 1, 0, 2, 0, 4, 0, 2, 0, 3, 0, 4, 0, 3, 0, 0, 0, 4, 0, 0, 0, 3, 0, 1, 0, 1, 0, 2, 0, 3, 0};
        mesh.getPoints().addAll(points);
        mesh.getTexCoords().addAll(texPoints);
        mesh.getFaces().addAll(triangles);
        MeshView sting1 = new MeshView(mesh);
        MeshView sting2 = new MeshView(mesh);
        sting2.getTransforms().addAll((Transform[])new Transform[]{new Translate(1.0, 0.0, 0.0), new Rotate(45.0)});
        MeshView sting3 = new MeshView(mesh);
        sting3.getTransforms().addAll((Transform[])new Transform[]{new Translate(-1.0, 0.0, 0.0), new Rotate(-45.0)});
        MeshView sting4 = new MeshView(mesh);
        sting4.getTransforms().addAll((Transform[])new Transform[]{new Translate(0.0, 0.0, 1.0), new Rotate(-45.0, new Point3D(1.0, 0.0, 0.0))});
        MeshView sting5 = new MeshView(mesh);
        sting5.getTransforms().addAll((Transform[])new Transform[]{new Translate(0.0, 0.0, -1.0), new Rotate(45.0, new Point3D(1.0, 0.0, 0.0))});
        sting1.setMaterial(material);
        sting2.setMaterial(material);
        sting3.setMaterial(material);
        sting4.setMaterial(material);
        sting5.setMaterial(material);
        Group stings = new Group(sting1, sting2, sting3, sting4, sting5);
        stings.setTranslateY(-4.5);
        this.setTranslateX(x);
        this.setTranslateZ(z);
        this.setTranslateY(8.0);
        this.getChildren().addAll((Node[])new Node[]{sphere, stings});
    }
}
