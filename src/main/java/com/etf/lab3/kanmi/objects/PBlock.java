package com.etf.lab3.kanmi.objects;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class PBlock extends Group {
    private static double size = 10;

    public PBlock(double x, double z) {
        // Create the L-block using Box instances
        Box left = new Box(2,size,size/2);
        Box right = new Box (2,size,size/2);
        Box top = new Box(size,2,size/2);

        left.setTranslateX(-4.0);
        left.setTranslateY(4.0);
        right.setTranslateX(4.0);
        right.setTranslateY(4.0);

        PhongMaterial material = new PhongMaterial(Color.ROSYBROWN);
        top.setMaterial(material);
        left.setMaterial(material);
        right.setMaterial(material);
        this.getChildren().addAll(top, left, right);
        this.setTranslateX(x);
        this.setTranslateZ(z);




    }


}