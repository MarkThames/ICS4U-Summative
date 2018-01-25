package LifeSimulator;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Menu extends JFrame implements ActionListener, ChangeListener{
	boolean isMap=false;
	Island world;
	EditMenu menu;
	JSlider spd;
	public Menu(){
		Utility.addGlobalObject("Species", new ArrayList<Species>());
		Utility.addGlobalObject("To Insert", null);
        Utility.addGlobalObject("Highlighted SW", null);
		Utility.newGlobalCounter("Species", 0);
		menu = new EditMenu();
		JPanel content=new JPanel(new GridLayout(2, 1));
		JPanel top=new JPanel(new GridLayout(1, 3));
		JPanel bot=new JPanel(new GridLayout(1, 2));
		JPanel botl=new JPanel(new GridLayout(1, 2));
		JPanel botr=new JPanel(new GridLayout(1, 1));
		JButton map=new JButton("Map");
		JButton graph=new JButton("Data");
		JButton edit=new JButton("Edit");
		JButton info=new JButton("Info");
		JButton start=new JButton("Start");
		JButton pause=new JButton("Pause");
		spd=new JSlider();
		spd.addChangeListener(this);
		map.addActionListener(this);
		graph.addActionListener(this);
		edit.addActionListener(this);
		info.addActionListener(this);
		start.addActionListener(this);
		pause.addActionListener(this);
		top.add(map);
		top.add(graph);
		top.add(edit);
		//top.add(info);
		botl.add(start);
		botl.add(pause);
		botr.add(spd);
		bot.add(botl);
		bot.add(botr);
		content.add(top);
		content.add(bot);
		setContentPane (content);
        pack();
        setTitle ("Menu");
        setSize(400, 80);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen=ge.getDefaultScreenDevice();
        Rectangle rect=defaultScreen.getDefaultConfiguration().getBounds();
        int x = ((int)rect.getMaxX() - this.getWidth())/2;
        int y = 50;
        setLocation(x, y);
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
		Menu open=new Menu();
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Map")){
			if(!world.isVisible()){
		    	world.setVisible(true);
		    }
		}
		if(e.getActionCommand().equals("Data")){
			GraphMenu gr=new GraphMenu(world.getData());
		}
        if(e.getActionCommand().equals("Edit")){
            menu.setVisible(true);
        }
        if(e.getActionCommand().equals("Start")){
        	if(world.isVisible()){
        		world.start();
        	}
        }
        if(e.getActionCommand().equals("Pause")){
        	world.pause();
        }
        if(world!=null) {
            menu.updateSpecies();
            menu.repaint();
        }
	}
	@Override
	public void stateChanged(ChangeEvent e){
		world.changeSpeed((int)(50.0*Math.pow(10, (spd.getValue()-50.0)/50.0)));
	}
}
