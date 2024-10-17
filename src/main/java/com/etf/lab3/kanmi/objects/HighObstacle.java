package com.etf.lab3.kanmi.objects;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class HighObstacle extends Box {
    public static double size = 20;
    public HighObstacle (double x , double z){
        super(size,size*2,size);
        setMaterial(new PhongMaterial(Color.SADDLEBROWN));

        setTranslateX(x);
        setTranslateZ(z);
    }
}
