package LifeSimulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

class GraphMenu extends JFrame implements ActionListener{
	private Graph current;
	private JPanel content;
	//choose what species to display
	private JComboBox s1, s2;
	private JButton create;
	//names of species
	private String[] species;
	private Color[] col;
	//all data
	private ArrayList<Integer>[] data;
	public GraphMenu(ArrayList<Integer>[] arr){
		//gain data
		data=arr;
		setPreferredSize(new Dimension(700, 500));
		content=new JPanel();
		content.setLayout(new FlowLayout());
		//empty
		current=new Graph(new ArrayList<Integer>(), Color.black);
		ArrayList<Species>list =((ArrayList<Species>) Utility.getGlobalObject("Species"));
		species=new String[list.size()+1];
		col=new Color[list.size()+1];
		species[0]="---";
		col[0]=Color.black;
		for(int i=1;i<=list.size();i++){
			species[i]=list.get(i-1).name;
			col[i]=list.get(i-1).c;
		}
		s1=new JComboBox(species);
		s2=new JComboBox(species);
		
		create=new JButton("Go!");
		create.addActionListener(this);
		
		//add components
		content.add(s1, "North");
		content.add(s2, "North");
		content.add(create, "North");
		content.add(current, "South");
		setContentPane (content);
        pack ();
        setTitle ("Population Graphs");
        setSize (700, 500);
        setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo (null);
        setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		//display species selected
		if(e.getActionCommand().equals("Go!")){
			if(s1.getSelectedIndex()==0){
				if(s2.getSelectedIndex()!=0){
					displayGraph(data[s2.getSelectedIndex()-1], col[s2.getSelectedIndex()]);
				}
			}
			else if(s2.getSelectedIndex()==0)displayGraph(data[s1.getSelectedIndex()-1], col[s1.getSelectedIndex()]);
			else displayGraph(data[s1.getSelectedIndex()-1], data[s2.getSelectedIndex()-1], col[s1.getSelectedIndex()], col[s2.getSelectedIndex()]);
		}
	}
	//display graph
	public void displayGraph(ArrayList<Integer> data1, Color c1){
		content.remove(current);
		current=new Graph(data1, c1);
		content.add(current);
		setContentPane(content);
		repaint();
	}
	//display graph
	public void displayGraph(ArrayList<Integer> data1, ArrayList<Integer> data2, Color c1, Color c2){
		content.remove(current);
		current=new Graph(data1, data2, c1, c2);
		content.add(current);
		setContentPane(content);
		repaint();
	}
	public static void main(String[] args) {
    }
}
