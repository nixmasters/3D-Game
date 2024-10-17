package com.etf.lab3.kanmi.objects;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class LBlock extends Group {
    private static double size = 10;

    public LBlock(double x, double z) {
        Box left = new Box(2,size,size/2);

        Box bot = new Box(size,2,size/2);

        left.setTranslateX(-4.0);
        left.setTranslateY(4.0);
        bot.setTranslateY(size-1);


        PhongMaterial material = new PhongMaterial(Color.SANDYBROWN);
        bot.setMaterial(material);
        left.setMaterial(material);

        this.getChildren().addAll(bot, left);
        this.setTranslateX(x);
        this.setTranslateZ(z);
    }



}