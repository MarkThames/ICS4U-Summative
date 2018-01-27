package LifeSimulator;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

	class Graph extends JPanel
    {
		int x, y;
		//data
		ArrayList<Integer>[] data;
		//color of species
		Color[] c;
		//two species or not
		boolean two=false;
		//constructor
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
        //constructor
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
        	//draw graph
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
        	
        	//connect data points
        	for(int i=1;i<data[0].size();i++){
        		int x1=x+(500*(i-1))/(len);
        		int y1=y+300-(300*(data[0].get(i-1)-min))/(max-min);
        		int x2=x+(500*i)/(len);
        		int y2=y+300-(300*(data[0].get(i)-min))/(max-min);
        		g.drawLine(x1, y1, x2, y2);
        	}
        	g.setColor(Color.black);
        	g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        	//y-axis markings
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
        	//x-axis markings
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
        	//write y label
        	String[] word={"P", "o", "p", "u", "l", "a", "t", "i", "o", "n"};
        	g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        	for(int i=0;i<10;i++){
        		g.drawString(word[i], x-50, y+80+15*i);
        	}
        	//write x label
        	g.setColor(Color.black);
        	g.drawString("Time (Ticks)", x+200, y+335);
        	//draw second graph
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
            	//connect points
            	for(int i=1;i<data[0].size();i++){
            		int x1=x+(500*(i-1))/(len);
            		int y1=y+300-(300*(data[1].get(i-1)-min))/(max-min);
            		int x2=x+(500*i)/(len);
            		int y2=y+300-(300*(data[1].get(i)-min))/(max-min);
            		g.drawLine(x1, y1, x2, y2);
            	}
            	g.setColor(Color.black);
            	g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
            	//y-axis markings
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
            	//write y-label
            	for(int i=0;i<10;i++){
            		g.drawString(word[i], x+540, y+80+15*i);
            	}
        	}
        }
    }
