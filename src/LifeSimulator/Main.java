package LifeSimulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Main extends JDialog {

    public Main() {
        add(new background());
        setSize(500, 500);
        repaint();
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                Island is = new Island();
                is.setVisible(true);
                setVisible(false);
            }
        });
    }

    private class background extends JPanel {
        private Map m; // used to draw a map

        public void paintComponent(Graphics G) {

            G.setColor(Color.white);
            //looks fun if you take this line out
            G.fillRect(0, 0, 500, 500);

            //determine color for each terrain type
            Color[] mapColors = new Color[6];
            mapColors[0] = new Color(100, 100, 100);
            mapColors[1] = new Color(255, 255, 0);
            mapColors[2] = new Color(160, 40, 40);
            mapColors[3] = new Color(0, 100, 255);
            mapColors[4] = new Color(0, 255, 0);
            mapColors[5] = new Color(0, 150, 50);

            //draw map
            for (int i = 0; i < 500; i += 4) {
                for (int j = 0; j < 500; j += 4) {
                    G.setColor(mapColors[m.type[(int) (Math.max(Math.min(i, 499.4), 0))][(int) (Math.max(Math.min(j, 499.4), 0))]]);
                    G.drawRect(i, j, 2, 2);
                }
            }
            G.setColor(Color.black);
            G.drawString("Press Any Key to Start", 175, 250);
        }

        public background() {
            m = new Map(800, 800);
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
