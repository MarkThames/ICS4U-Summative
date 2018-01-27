package LifeSimulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Main extends JFrame {

	Island is;
    public Main() {
    	is=new Island();
        add(new background());
        setSize(516, 538);
        repaint();
        
        //center window
        GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen=ge.getDefaultScreenDevice();
        Rectangle rect=defaultScreen.getDefaultConfiguration().getBounds();
        int x = ((int)rect.getMaxX() - this.getWidth())/2;
        int y = 100;
        setLocation(x, y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //show island when pressed and hide
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                is.setVisible(true);
                setVisible(false);
            }
        });
    }

    private class background extends JPanel {
        private Map m; // used to draw a map

        //faded color
        private Color fade(Color n){
        	return new Color((n.getRed()+3*255)/4, (n.getGreen()+3*255)/4, (n.getBlue()+3*255)/4); 
        }
        public void paintComponent(Graphics G) {

            G.setColor(Color.white);
            G.fillRect(0, 0, 500, 500);

            //determine color for each terrain type
            Color[] mapColors = new Color[6];
        	mapColors[0]=fade(new Color(100, 100, 100));
        	mapColors[1]=fade(new Color(255, 255, 0));
        	mapColors[2]=fade(new Color(160, 40, 40));
        	mapColors[3]=fade(new Color(0, 100, 255));
        	mapColors[4]=fade(new Color(0, 255, 0));
        	mapColors[5]=fade(new Color(0, 150, 50));

            //draw map
            for (int i = 0; i < 500; i += 4) {
                for (int j = 0; j < 500; j += 4) {
                    G.setColor(mapColors[m.type[(int) (Math.max(Math.min(i, 499.4), 0))][(int) (Math.max(Math.min(j, 499.4), 0))]]);
                    G.drawRect(i, j, 2, 2);
                }
            }
            //add text
            G.setColor(Color.black);
            G.setFont(new Font("Britannic Bold", Font.PLAIN, 60));
            G.drawString("ECOSYSTEM", 100, 120);
            G.setFont(new Font("Calibri", Font.PLAIN, 18));
            G.drawString("Press Any Key to Start", 170, 250);
            G.setFont(new Font("Calibri", Font.PLAIN, 12));
            G.drawString("Created by: Frank Chen, Roger Fu, Victor Rong", 125, 470);
        }

        public background() {
            m = is.getMap();
            setSize(800, 800);
        }
    }

    public static void main(String[] args) {
        Utility.addGlobalObject("Species", new ArrayList<Species>());
        Utility.addGlobalObject("To Insert", null);
        Utility.addGlobalObject("Highlighted SW", null);
        Utility.newGlobalCounter("Species", 0);
        new Main().setVisible(true);
    }
}
