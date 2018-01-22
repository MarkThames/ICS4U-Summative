package LifeSimulator;
import java.awt.*;
import javax.swing.*;
public class Sample extends JFrame{
    
    public static void main(String[] args){
    }
}
class DrawArea extends JPanel{
    Painter P;
    public DrawArea(int w,int h){
        P=new Painter();
        setPreferredSize(new Dimension(w,h));
    }
    public void paintComponent(Graphics G){
        P.drawAll(G);
    }
    public void add(Drawable D){
        P.add(D);
    }
}