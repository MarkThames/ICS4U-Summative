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
	private JComboBox s1, s2;
	private JButton create;
	private String[] species;
	private Color[] col;
	private ArrayList<Integer>[] data;
	public GraphMenu(ArrayList<Integer>[] arr){
		data=arr;
		setPreferredSize(new Dimension(700, 500));
		content=new JPanel();
		content.setLayout(new FlowLayout());
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
	public void displayGraph(ArrayList<Integer> data1, Color c1){
		content.remove(current);
		current=new Graph(data1, c1);
		content.add(current);
		setContentPane(content);
		repaint();
	}
	public void displayGraph(ArrayList<Integer> data1, ArrayList<Integer> data2, Color c1, Color c2){
		content.remove(current);
		current=new Graph(data1, data2, c1, c2);
		content.add(current);
		setContentPane(content);
		repaint();
	}
	public static void main(String[] args) {
	}
	class Graph extends JPanel
    {
		int x, y;
		ArrayList<Integer>[] data;
		Color[] c;
		boolean two=false;
        public Graph (ArrayList<Integer> data1, Color c1)
        {
            this.setPreferredSize (new Dimension (700, 500));
            x=120;
            y=50;
            data=new ArrayList[1];
            this.data[0]=new ArrayList<Integer>(data1);
            c=new Color[1];
            c[0]=c1;
            two=false;
        }
        public Graph (ArrayList<Integer> data1, ArrayList<Integer> data2, Color c1, Color c2){
        	this.setPreferredSize (new Dimension (700, 500));
            x=120;
            y=50;
            data=new ArrayList[2];
            this.data[0]=new ArrayList<Integer>(data1);
            this.data[1]=new ArrayList<Integer>(data2);
            c=new Color[2];
            c[0]=c1;
            c[1]=c2;
            two=true;
        }
        public void paintComponent (Graphics g)
        {
        	g.setColor(Color.white);
        	g.fillRect(x, y, 500, 300);
        	g.setColor(Color.black);
        	g.drawLine(x, y+300, x, y);
        	g.drawLine(x, y+300, x+500, y+300);
        	int max=(int)(Integer.MIN_VALUE);
        	int min=(int)(Integer.MAX_VALUE);
        	for(int i=0;i<data[0].size();i++){
        		max=Math.max(max, data[0].get(i));
        		min=Math.min(min, data[0].get(i));
        	}
        	if(max-min>=15)max=min+10*(int)(Math.ceil((1.0*(max-min)/10.0)));
        	if(max==min){
        		max++;
        		min--;
        	}
        	int len=data[0].size()-1;
        	if(len==0)return;
        	g.setColor(c[0]);
        	//if(len>=14)len=10*(int)(Math.ceil((1.0)*len/10.0));
        	for(int i=1;i<data[0].size();i++){
        		int x1=x+(500*(i-1))/(len);
        		int y1=y+300-(300*(data[0].get(i-1)-min))/(max-min);
        		int x2=x+(500*i)/(len);
        		int y2=y+300-(300*(data[0].get(i)-min))/(max-min);
        		g.drawLine(x1, y1, x2, y2);
        	}
        	g.setColor(Color.black);
        	g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        	if(max-min<15){
        		for(int i=min;i<=max;i++){
        			g.drawString((""+i), x-25, y+300-(300*(i-min))/(max-min)+4);
        		}
        	}
        	else{
        		for(int i=0;i<=10;i++){
        			g.drawString((""+(min+i*((max-min)/10))), x-25, y+300-30*i+4);
        		}
        	}
        	if(data[0].size()<15){
        		for(int i=0;i<data[0].size();i++){
        			g.drawString((""+i), x-2+(i*500)/(len), y+320);
        		}
        	}
        	else{
        		for(int i=0;i<11;i++){
        			g.drawString((""+(i*len)/10), x-2+50*i, y+320);
        		}
        	}
        	g.setColor(c[0]);
        	String[] word={"P", "o", "p", "u", "l", "a", "t", "i", "o", "n"};
        	g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        	for(int i=0;i<10;i++){
        		g.drawString(word[i], x-50, y+80+15*i);
        	}
        	g.setColor(Color.black);
        	g.drawString("Time (Days)", x+200, y+335);
        	if(two){
        		g.drawLine(x+500, y+300, x+500, y);
        		max=(int)(Integer.MIN_VALUE);
            	min=(int)(Integer.MAX_VALUE);
            	for(int i=0;i<data[0].size();i++){
            		max=Math.max(max, data[1].get(i));
            		min=Math.min(min, data[1].get(i));
            	}
            	if(max-min>=15)max=min+10*(int)(Math.ceil((1.0*(max-min)/10.0)));
            	if(max==min){
            		max++;
            		min--;
            	}
            	g.setColor(c[1]);
            	for(int i=1;i<data[0].size();i++){
            		int x1=x+(500*(i-1))/(len);
            		int y1=y+300-(300*(data[1].get(i-1)-min))/(max-min);
            		int x2=x+(500*i)/(len);
            		int y2=y+300-(300*(data[1].get(i)-min))/(max-min);
            		g.drawLine(x1, y1, x2, y2);
            	}
            	g.setColor(Color.black);
            	g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
            	if(max-min<15){
            		for(int i=min;i<=max;i++){
            			g.drawString((""+i), x+505, y+300-(300*(i-min))/(max-min)+4);
            		}
            	}
            	else{
            		for(int i=0;i<=10;i++){
            			g.drawString((""+(min+i*((max-min)/10))), x+505, y+300-30*i+4);
            		}
            	}
            	g.setColor(c[1]);
            	g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
            	for(int i=0;i<10;i++){
            		g.drawString(word[i], x+540, y+80+15*i);
            	}
        	}
        }
    }
}
