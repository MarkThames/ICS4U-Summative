package LifeSimulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Menu extends JFrame implements ActionListener{
	boolean isMap=false;
	Island world;
	EditMenu menu;
	public Menu(){
		Utility.addGlobalObject("Species", new ArrayList<Species>());
		Utility.addGlobalObject("To Insert", null);
        Utility.addGlobalObject("Highlighted SW", null);
		Utility.newGlobalCounter("Species",2);
		menu = new EditMenu();
		JPanel content=new JPanel(new GridLayout());
		JButton world=new JButton("Map");
		JButton graph=new JButton("Data");
		JButton edit=new JButton("Edit");
		JButton info=new JButton("Info");
		world.addActionListener(this);
		graph.addActionListener(this);
		edit.addActionListener(this);
		content.add(world);
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
	}
	public static void main(String[] args) {
		Menu demo=new Menu();
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Map")&&!isMap){
			world=new Island();
			isMap=true;
		}
		if(e.getActionCommand().equals("Data")){
			GraphMenu demo=new GraphMenu();
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
