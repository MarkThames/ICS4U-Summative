package LifeSimulator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

// A general purpose utilities class
// Contains a hashmap which stores global varibles that are maintained throughout execution
// Idea is similar to Python's global keyword
public class Utility {
    // Hashmap to specifically store counters
    private static HashMap<String,Integer>counters=new HashMap<>();
    // Hashmap to store arbitrary objects
    private static HashMap<String,Object>objects=new HashMap<>();
    // Do not need constructor, since everything is static
    private Utility(){}
    // Method to generate a coloured square
    public static BufferedImage getColouredSquare(Color C,int size){
        // Construct the BufferedImage
        BufferedImage bi = new BufferedImage(size,size,BufferedImage.TYPE_INT_RGB);
        // Get the Graphics object
        Graphics G = bi.getGraphics();
        // Set the colour to what is desired
        G.setColor(C);
        // Draw the rectangle
        G.fillRect(0,0,size,size);
        // Return the BufferedImage, now complete with coloured square
        return bi;
    }
    // Adds a global counter
    public static void newGlobalCounter(String name){
        newGlobalCounter(name,0);
    }
    // Adds a global counter, with non-zero initial value
    public static void newGlobalCounter(String name,int initial){
        counters.put(name,initial);
    }
    // Retrieves value of counter
    public static Integer getGlobalCounter(String name){
        if(!counters.containsKey(name)) return 0;
        else return counters.get(name);
    }
    // Modifes the value of the counter
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
    // Allows for the creation of a new global Object
    public static void addGlobalObject(String name,Object O){
        objects.put(name,O);
    }
    // Retrieves the global object
    public static Object getGlobalObject(String name){
        return objects.get(name);
    }
    // Allows you to overwrite the old object stored with this name
    public static void modifyGlobalObject(String name, Object O){
        objects.put(name,O);
    }
}
