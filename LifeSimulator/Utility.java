package LifeSimulator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Utility {
    private static HashMap<String,Integer>counters=new HashMap<>();
    private static HashMap<String,Object>objects=new HashMap<>();
    private Utility(){}
    public static BufferedImage getColouredSquare(Color C,int size){
        BufferedImage bi = new BufferedImage(size,size,BufferedImage.TYPE_INT_RGB);
        Graphics G = bi.getGraphics();
        G.setColor(C);
        G.fillRect(0,0,size,size);
        return bi;
    }
    public static void newGlobalCounter(String name){
        newGlobalCounter(name,0);
    }
    public static void newGlobalCounter(String name,int initial){
        counters.put(name,initial);
    }
    public static Integer getGlobalCounter(String name){
        if(!counters.containsKey(name)) return 0;
        else return counters.get(name);
    }
    public static void decrementGlobalCounter(String name){
        incrementGlobalCounter(name,-1);
    }
    public static void decrementGlobalCounter(String name,int value){
        incrementGlobalCounter(name,-value);
    }
    public static void incrementGlobalCounter(String name){
        incrementGlobalCounter(name,1);
    }
    public static void incrementGlobalCounter(String name,int value){
        counters.put(name,counters.get(name)+value);
    }
    public static void addGlobalObject(String name,Object O){
        objects.put(name,O);
    }
    public static Object getGlobalObject(String name){
        return objects.get(name);
    }
    public static void modifyGlobalObject(String name, Object O){
        objects.put(name,O);
    }
}
