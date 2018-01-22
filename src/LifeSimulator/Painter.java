package LifeSimulator;

import java.awt.*;
import java.util.ArrayList;

// helper to draw everything
public class Painter{
    ArrayList<Animal>toDraw;
    public Painter(){
        toDraw=new ArrayList<>();
    }
    public void drawAll(Graphics G){
        for(Animal D:toDraw)
            D.draw(G);
    }
    public void change(ArrayList<Animal> arr){
        toDraw=arr;
    }
}

interface Drawable{
    public void draw(Graphics G);
}