package LifeSimulator;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.ArrayList;

import javax.swing.*;

public class Menu extends JFrame implements ActionListener{
	boolean isMap=false;
	Island world;
	EditMenu menu;
	public Menu(){
		Utility.addGlobalObject("Species", new ArrayList<Species>());
		Utility.addGlobalObject("To Insert", null);
        Utility.addGlobalObject("Highlighted SW", null);
		Utility.newGlobalCounter("Species", 0);
		menu = new EditMenu();
		JPanel content=new JPanel(new GridLayout());
		JButton map=new JButton("Map");
		JButton graph=new JButton("Data");
		JButton edit=new JButton("Edit");
		JButton info=new JButton("Info");
		map.addActionListener(this);
		graph.addActionListener(this);
		edit.addActionListener(this);
		content.add(map);
		content.add(graph);
		content.add(edit);
		content.add(info);
		setContentPane (content);
        pack();
        setTitle ("Menu");
        setSize(400, 80);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        world=new Island();
		world.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e) {
		        // handle closing the window
		        world.setVisible(false);
		        world.dispose();
		        world.pause();
		    }
		});
	}
	public static void main(String[] args) {
		Menu demo=new Menu();
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Map")){
			if(!world.isVisible()){
		    	world.setVisible(true);
		    }
		}
		if(e.getActionCommand().equals("Data")){
			GraphMenu demo=new GraphMenu(world.island.getData());
			
		}
        if(e.getActionCommand().equals("Edit")){
            menu.setVisible(true);
        }
        if(world!=null) {
            menu.updateSpecies();
            menu.repaint();
        }
	}
}
