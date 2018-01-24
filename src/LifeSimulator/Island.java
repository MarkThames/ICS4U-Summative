package LifeSimulator;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import LifeSimulator.GraphMenu.Graph;
import javafx.scene.chart.PieChart.Data;

public class Island extends JFrame implements ActionListener, ChangeListener{
	IslandLife island;
	JLabel content;
	Timer t;
	myMouseAdapter mouse;
	public Island(){
		makeSpecies();
		island=new IslandLife();
		content=new JLabel();
		content.setLayout(new BorderLayout());
		content.add(island, "West");
		//content.add(idk);
		setContentPane (content);
        pack ();
        setTitle ("World");
        setSize (515, 535);
        setMaximumSize(new Dimension(515,535));
        setLocationRelativeTo (null);
        t=new Timer(50, this);
        t.start();
        mouse=new myMouseAdapter();
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        addMouseWheelListener(mouse);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if(keyEvent.getKeyCode()==KeyEvent.VK_ESCAPE){
                    Utility.modifyGlobalObject("To Insert",null);
                    if(Utility.getGlobalObject("Highlighted SW")!=null)
                        ((EditMenu.SpeciesWrapper)Utility.getGlobalObject("Highlighted SW")).setBorder(BorderFactory.createEmptyBorder());
                    Utility.modifyGlobalObject("Highlighted SW",null);
                }
            }
        });
	}
	private void makeSpecies(){
		Utility.incrementGlobalCounter("Species", 2);
		((ArrayList<Species>)Utility.getGlobalObject("Species")).add(new Species("Mouse", Color.red, 50, 200, true, false, 0, 3, 4, 2.0));
		((ArrayList<Species>)Utility.getGlobalObject("Species")).add(new Species("Cat", Color.magenta, 100, 400, false, true, 1, 5, 7, 10.0));
	}
	public static void main(String[] args) {

	}
	public void pause(){
		t.stop();
	}
	public void stateChanged(ChangeEvent arg0) {
		
	}
	public void actionPerformed(ActionEvent arg0) {
		island.tick();
	}
	class myMouseAdapter extends MouseAdapter{
		double sX, sY;
		public myMouseAdapter(){
			
		}
		
		//scroll to zoom towards/from a point
		public void mouseWheelMoved(MouseWheelEvent e){
			island.changeZoom(0.1*e.getPreciseWheelRotation(), e.getX()-8, e.getY()-28);
		}
		
		public void mouseClicked(MouseEvent e) {
			Species S = (Species)Utility.getGlobalObject("To Insert");
            if(S!=null){
            	//add animal at point clicked
                island.addAnimal(S, e.getX()-8, e.getY()-28);
            }
		}

		public void mouseEntered(MouseEvent e) {
			
		}

		public void mouseExited(MouseEvent e) {
			
		}

		public void mousePressed(MouseEvent e) {
			//determine where drag started
			if(SwingUtilities.isLeftMouseButton(e)){
				sX=e.getX();
				sY=e.getY();
			}
		}
		
		public void mouseDragged(MouseEvent e){
			//shift map when dragged
			if(SwingUtilities.isLeftMouseButton(e)){
				island.shift(sX-e.getX(), sY-e.getY());
				sX=e.getX();
				sY=e.getY();
			}
		}

		public void mouseReleased(MouseEvent e) {
			
		}
		
	}
}
class IslandLife extends JPanel{
	//all drawn creatures
	private ArrayList<Animal> creatures;
	//counters
	private int animalId=1, ticks;
	//map of world, includes details about terrain
	private Map m;
	//zoom and shift of map
	private double zoom=1.0, shiftX=0, shiftY=0;
	//keeps track of populations
	private ArrayList<Integer>[] pop;
	
	//constructor
	public IslandLife(){
		
		pop=new ArrayList[20];
		for(int i=0;i<20;i++)pop[i]=new ArrayList<Integer>();
		
		ticks=0;
		
		//size of map is 500 by 500
		setPreferredSize(new Dimension(500, 500));
		m=new Map(500, 500);
		
		creatures=new ArrayList<Animal>();
		
		//add in starting animals
		for(int i=0;i<100;i++){
			int x=(int)(500*Math.random());
			int y=(int)(500*Math.random());
			while(m.type[x][y]==3){
				x=(int)(500*Math.random());
				y=(int)(500*Math.random());
			}
			Animal test=new Animal(x, y, ((ArrayList<Species>) Utility.getGlobalObject("Species")).get(0), animalId++);
			creatures.add(test);
			test.randomAge();
		}
		for(int i=0;i<20;i++){
			int x=(int)(500*Math.random());
			int y=(int)(500*Math.random());
			while(m.type[x][y]==3){
				x=(int)(500*Math.random());
				y=(int)(500*Math.random());
			}
			Animal test=new Animal(x, y, ((ArrayList<Species>) Utility.getGlobalObject("Species")).get(1), animalId++);
			creatures.add(test);
			test.randomAge();
		}
	}
	
	public ArrayList<Integer>[] getData(){
		ArrayList<Integer>[]temp=new ArrayList[20];
		for(int i=0;i<20;i++){
			temp[i]=new ArrayList<Integer>(pop[i]);
		}
		return temp;
	}
	
	//adds new animal to world at specific point
	public void addAnimal(Species S, double x, double y){
		Animal a=new Animal(x/zoom+shiftX, y/zoom+shiftY, S, animalId++);
	    creatures.add(a);
	}
	
	//change zoom of map while ensuring mouse is pointing to the same location
	public void changeZoom(double val, double x, double y){
		double nxt=Math.max(1.0, Math.min(5.0, zoom+val));
		double temp=zoom;
		zoom=nxt;
		changeShift(x/temp-x/nxt, y/temp-y/nxt);
	}
	
	//shift map accordingly
	public void shift(double x, double y){
		changeShift(x/zoom, y/zoom);
	}
	
	//changes shift variables
	private void changeShift(double x, double y){
		shiftX+=x;
		shiftY+=y;
		if(shiftX<0)shiftX=0;
		if(shiftX+500/zoom>500)shiftX=500-500/zoom;
		if(shiftY<0)shiftY=0;
		if(shiftY+500/zoom>500)shiftY=500-500/zoom;
	}
	
	
	
	//update population data
	private void updatePop(){
		int[] count=new int[20];
		for(Animal A: creatures){
			if(A.alive){
				count[A.sp.id]++;
			}
		}
		for(int i=0;i<20;i++)pop[i].add(count[i]);
	}
	
	//get rid of dead creatures
	private void discardGone(){
		ArrayList<Animal> temp=new ArrayList<Animal>();
		for(Animal A:creatures){
			if(!A.gone)temp.add(A);
		}
		creatures=temp;
	}
	
	//draw world
    public void paintComponent(Graphics G){
    	
    	G.setColor(Color.white);
    	//looks fun if you take this line out
    	G.fillRect(0, 0, 800, 800);
    	
    	//determine color for each terrain type
    	Color[] mapColors=new Color[6];
    	mapColors[0]=new Color(100, 100, 100);
    	mapColors[1]=new Color(255, 255, 0);
    	mapColors[2]=new Color(160, 40, 40);
    	mapColors[3]=new Color(0, 100, 255);
    	mapColors[4]=new Color(0, 255, 0);
    	mapColors[5]=new Color(0, 150, 50);
    	
    	//draw map
    	for(int i=0;i<500;i+=4){
    		for(int j=0;j<500;j+=4){
    			G.setColor(mapColors[m.type[(int)(Math.max(Math.min(i/zoom+shiftX, 499.4), 0))][(int)(Math.max(Math.min(j/zoom+shiftY, 499.4), 0))]]);
    			G.drawRect(i, j, (int)(2), (int)(2));
    		}
    	}
    	
    	//draw animals
        for(Animal A:creatures)A.draw(G);
        
        repaint();
    }
    
    //time moves on
	public void tick(){
		
		//update data
		updatePop();
		
		//each animal does something
		for(Animal A: creatures){
			A.act(creatures);
		}
		
		//update list of animals
		discardGone();
		
		//spawn more animals
		breed();
		
		//update map
		m.rebuild();
	}
	
	//produce new generation
	public void breed(){
		ArrayList<Animal> temp=new ArrayList<Animal>(creatures);
		for(Animal A: temp){
			for(Animal B: temp){
				
				//bunch of conditions
				if(A.sp.id==B.sp.id&&A.alive&&B.alive&&A.id<B.id){
					if(A.hunger>50&&B.hunger>50&&A.delay>500&&B.delay>500&&A.dis(B)<10&&(A.action!=1&&A.action!=2&&A.action!=5)&&(B.action!=1&&B.action!=2&&B.action!=5)&&A.age>A.sp.adultAge&&B.age>B.sp.adultAge){
						
						//if lucky enough
						if(Math.random()<(A.health*B.health)*0.000001*0.1){
							
							//slow things down a little
							A.delay-=500;
							B.delay-=500;
							
							
							int num=1;
							for(int i=0;i<num;i++){
								//create child
								Animal test=new Animal(0.5*A.posX+0.5*B.posX, 0.5*A.posY+0.5*B.posY, A.sp, animalId++);
								creatures.add(test);
							}
						}
					}
				}
			}
		}
	}
	
	//controls animal's actions
	class Animal{
		
		//determines colour of animal
		private double colR, colG, colB;
		
		//not really necessary
		private static final double EPS=1e-4;
		
		//current action animal is doing
		private int action=0;
		
		//info about animal's current position and movement
		private double posX, posY, movX, movY, dirX=0, dirY=0;
		
		//various physical attributes
		private int health=1000, delay=1000;
		private double age=0, total=0, hunger=70, speed=0;
		
		//identifier
		private int id;
		
		//sensory ranges
		private double eye=1.8, eye2=30, ear=10;
		
		//what species this animal is
		private Species sp;
		
		//energy
		private double energy=100, maxEnergy=100;
		
		//gone is when the animal has died and decomposed
		private boolean alive=true, gone=false;
		
		//constructor
		public Animal(double x, double y, Species s, int i){
			//born hungry
			hunger=20+Math.random()*20;
			
		    movX=0;
		    movY=0;
		    posX=x;
		    posY=y;
		    
		    sp=s;
		    colR=sp.c.getRed();
		    colG=sp.c.getGreen();
		    colB=sp.c.getBlue();
		    
		    id=i;
		}
		
		//assign the animal a random age
		public void randomAge(){
			age=Math.random()*sp.deathAge*0.7;
			total=age;
		}
		
		//determine angle formed by line (0, 0) to (x, y)
		private double angle(double x, double y){
			if(Math.abs(x)<EPS&&y<=0)return (3.0*Math.PI*0.5);
			if(Math.abs(x)<EPS&&y>0)return (Math.PI*0.5);
			if(Math.abs(y)<EPS&&x<=0)return (Math.PI);
			if(Math.abs(y)<EPS&&x>0)return (0);
			double ratio=y/x;
			double ret=Math.atan(ratio);
			if(x<=0)ret+=Math.PI;
			ret=modp(ret);
			return ret;
		}
		
		//determine angle from this animal to animal n
		private double angle(Animal n){
			return angle(n.posX-posX, n.posY-posY);
		}
		
		//distance
		private double dis(double x, double y){
			return Math.sqrt(x*x+y*y);
		}
		
		//distance to an animal
		private double dis(Animal n){
			return dis((n.posX-posX), (n.posY-posY));
		}
		
		//ensure angles are from 0 to 2 pi
		private double modp(double x){
			while(x>=2*Math.PI)x-=(2*Math.PI);
		 	while(x<0)x+=(2*Math.PI);
		 	return x;
		}
		
		//ensure points are within the boundaries, mainly for using the map m
		private int modsz(double x){
			return (int)(Math.max(Math.min(x, 499.4), 0.0));
		}
		
		//move animal
		private void move(){
		    posX+=movX;
		    posY+=movY;
		    //doesn't use modsz because that returns int
		    //it could use a similar method but oh well
		    if(posX<0)posX=0;
		    if(posX>499.9)posX=499.9;
		    if(posY<0)posY=0;
		    if(posY>499.9)posY=499.9;
		}
		
		//check if this animal can sense another animal
		private boolean detect(Animal n){
			double ang=angle(n);
			double len=dis(n);
			double arg=angle(movX, movY);
			
			//if in sight range
			if(modp(arg-ang)<eye||modp(ang-arg)<eye)if(len<eye2)return true;
			
			//if in ear range
			if(len<ear)return true;
			
			//glance around
			if(Math.random()<0.01)if(len<eye2)return true;
			
			return false;
		}
		
		//same thing except with a specific point
		private boolean detect(double x, double y){
			x-=posX;
			y-=posY;
			double ang=angle(x, y);
			double len=dis(x, y);
			double arg=angle(movX, movY);
			
			//if in sight range
			if(modp(arg-ang)<eye||modp(ang-arg)<eye)if(len<eye2)return true;
			
			//if in ear range
			if(len<ear)return true;
			
			//glance around
			if(Math.random()<0.01)if(len<eye2)return true;
			
			return false;
		}
		
		//check if it can eat an animal n
		private boolean delicious(Animal n){
			if(!(sp).eatAnimals)return false;
			if(!(n.sp).eatPlants)return false;
			if(getSize()>n.getSize())return true;
			return false;
		}
		
		//check if animal n can eat it
		private boolean dangerous(Animal n){
			return n.delicious(this);
		}
		
		//returns weighted average of list of numbers
		private double avg(ArrayList<Double> val, ArrayList<Double> wgt){
			double ret=0;
			double tot=0;
			
			for(int i=0;i<val.size();i++){
				ret+=val.get(i)*wgt.get(i);
				tot+=wgt.get(i);
			}
			
			ret/=tot;
			return ret;
		}
		
		//oh boy
		public int selectAction(ArrayList<Animal> animals){
			//0 = nothing
			//1 = dying
			//2 = running
			//3 = wandering
			//4 = following
			//5 = chasing
			//6 = eating
			//7 = drinking
			//8 = resting
			
			if(health<EPS){
				alive=false;
				return 1;
			}
			
			//getting attacked
			for(Animal A: animals){
				if(dangerous(A)&&dis(A)<4){
					if(Math.random()>sp.strng/A.sp.strng)health-=50;
				}
			}
			
			//if animal sees a dangerous animal, run
			for(Animal A: animals){
				if(dangerous(A)&&detect(A)){
					if(action==2)return 2;
					if(A.action==5||dis(A)<20)return 2;
				}
			}
			
			//exhausted
			if(energy<40+EPS)return 8;
			
			//doesn't want to move anymore
			if(energy<80&&action==8)return 8;
			
			//eating
			if(hunger<80&&action==6)return 6;
			
			//looking for plants
			if(hunger<30&&sp.eatPlants){
				double closest=500, locX=0, locY=0;
				
				//find close-by plants
				for(int i=-10;i<=10;i++){
					for(int j=-10;j<=10;j++){
						int x=modsz(posX+i);
						int y=modsz(posY+j);
						if(m.type[x][y]>=4&&detect(x, y)){
							if(m.fruit[x][y]>0){
								if(dis(i, j)<closest){
									closest=dis(i, j);
									locX=x;
									locY=y;
								}
							}
						}
					}
				}
				
				if(closest<4){
					
					//map loses some resources
					for(int i=-2;i<=2;i++){
						for(int j=-2;j<=2;j++){
							m.fruit[modsz(locX+i)][modsz(locY+j)]--;
						}
					}
					
					//eat
					return 6;
				}
				
				if(closest<400){
					
					//move towards food
					dirX=locX-posX;
					dirY=locY-posY;
					double ang=angle(dirX, dirY);
					dirX=Math.max((80.0-3.0*closest), 20.0)*Math.cos(ang);
					dirY=Math.max((80.0-3.0*closest), 20.0)*Math.sin(ang);
					return 3;
					
				}
			}
			
			//dead animal nearby
			if(hunger<80&&sp.eatAnimals){
				
				double closest=500, locX=0, locY=0;
				
				//find closest corpse
				for(Animal A: creatures){
					
					//no cannibalism
					if(A.sp.id!=sp.id&&!A.alive&&detect(A)){
						if(dis(A)<closest){
							closest=dis(A);
							locX=A.posX;
							locY=A.posY;
						}
					}
					
				}
				
				
				//if close enough, eat
				if(closest<6)return 6;
				
				//head towards corpse
				if(closest<20){
					dirX=locX-posX;
					dirY=locY-posY;
					double ang=angle(dirX, dirY);
					dirX=5.0*Math.cos(ang);
					dirY=5.0*Math.sin(ang);
					return 3;
				}
			}
			
			//hunting prey
			if(hunger<30&&energy>40&&sp.eatAnimals){
				
				double closest=500, locX=0, locY=0;
				
				//find closest prey
				for(Animal A:creatures){
					if(delicious(A)&&detect(A)){
						if(dis(A)<closest){
							closest=dis(A);
							locX=A.posX;
							locY=A.posY;
						}
					}
				}
				
				//chase
				if(closest<400){
					dirX=locX-posX;
					dirY=locY-posY;
					double ang=angle(dirX, dirY);
					dirX=Math.max((80.0-3.0*closest), 20.0)*Math.cos(ang);
					dirY=Math.max((80.0-3.0*closest), 20.0)*Math.sin(ang);
					return 5;
				}
			}
			
			//safety in numbers
			
			//finds closest one from same species
			double closest=5000, locX=0, locY=0;
			for(Animal A: creatures){
				if(A.sp.id==sp.id&&detect(A)&&A.id!=id){
					if(dis(A)<closest){
						closest=dis(A);
						locX=A.posX;
						locY=A.posY;
					}
				}
			}
			
			//follow them
			if((Math.random()<0.02||(action==4&&Math.random()<0.98))&&closest<400){
				dirX=locX-posX;
				dirY=locY-posY;
				double ang=angle(dirX, dirY);
				dirX=2.0*Math.cos(ang);
				dirY=2.0*Math.sin(ang);
				return 4;
			}
			
			//move in random direction
			if(Math.abs(dirX)<EPS&&Math.abs(dirY)<EPS){
				dirX=4*Math.random()-2;
				dirY=4*Math.random()-2;
			}
			
			if(Math.abs(dirX*movY-dirY*movX)<EPS){
				dirX=4*Math.random()-2;
				dirY=4*Math.random()-2;
			}
			return 3;
		}
		
		//carry out action
		private void determine(ArrayList<Animal> animals){
			
			//dying
			if(action==1){
				
				//stop moving
				movX=0;
				movY=0;
				
				//colour fades to black
				colR=0*0.001+colR*0.99;
				colG=0*0.001+colG*0.99;
				colB=0*0.001+colB*0.99;
				
				//disappear when dead for long enough
				if((int)(colR)<40&&(int)(colG)<40&&(int)(colB)<40)gone=true;
			}
			
			//running away
			if(action==2){
				
				ArrayList<Double> val1=new ArrayList<Double>();
				ArrayList<Double> val2=new ArrayList<Double>();
				ArrayList<Double> wgt=new ArrayList<Double>();
				val1.add(movX);
				val2.add(movY);
				wgt.add(50.0);
				
				//run away from dangerous animals
				boolean danger=false;
				double dx=0, dy=0, closest=500;
				for(Animal A: animals){
		    		if(dangerous(A)&&dis(A)<100){
		    			if(A.action==5||dis(A)<100){
		    				danger=true;
		    				if(closest>dis(A)){
		    					closest=dis(A);
		    					dx=posX-A.posX;
		    					dy=posY-A.posY;
		    					double ang=modp(angle(dx, dy)-angle(movX, movY));
		    					if(Math.abs(ang-Math.PI)<0.2){
		    						if(ang-Math.PI>0){
		    							double temp=dy;
		    							dy=dx;
		    							dx=-temp;
		    						}
		    						else{
		    							double temp=dy;
		    							dy=-dx;
		    							dx=temp;
		    						}
		    					}
		    				}
		    			}
		    		}
		    	}
				if(danger){
					val1.add(dx);
					val2.add(dy);
					double amt=Math.max(0.5, 1.5-closest/20.0);
					if(closest<9)amt=5*(10-closest);
					wgt.add(amt);
				}
				
				//use up energy when running
				double curAbs=dis(movX, movY);
				changeVelocity(avg(val1, wgt), avg(val2, wgt));
				changeVelocity2(angle(movX, movY), curAbs*0.95+speed*(0.2+0.8*energy*0.01)*0.05);
				energy=Math.max(energy-1.0, 0);
				
			}
			
			//wandering or following
			if(action==3||action==4){
				ArrayList<Double> val1=new ArrayList<Double>();
				ArrayList<Double> val2=new ArrayList<Double>();
				ArrayList<Double> wgt=new ArrayList<Double>();
				
				//gradually start moving towards a certain direction
				val1.add(dirX);
				val1.add(movX);
				val2.add(dirY);
				val2.add(movY);
				wgt.add(1.0);
				wgt.add(50.0);
				
				//stay away from dangerous animals
				boolean danger=false;
				double dx=0, dy=0, closest=500;
				
				for(Animal A: animals){
		    		if(dangerous(A)&&detect(A)){
		    			
		    			danger=true;
		    			if(closest>dis(A)){
		    				closest=dis(A);
		    				dx=posX-A.posX;
		    				dy=posY-A.posY;
		    				double ang=modp(angle(dx, dy)-angle(movX, movY));
		    				
		    				if(Math.abs(ang-Math.PI)<0.2){
		    					if(ang-Math.PI>0){
		    						double temp=dy;
		    						dy=dx;
		    						dx=-temp;
		    					}
		    					else{
		    						double temp=dy;
		    						dy=-dx;
		    						dx=temp;
		    					}
		    				}
		    				
		    				//if the animal is moving towards a predator
		    				if((ang<0.4||ang>2*Math.PI-0.4)&&dis(A)<10){
		    					if(ang>2*Math.PI-0.4){
		    						double temp=dy;
		    						dy=dx;
		    						dx=-temp;
		    					}
		    					else{
		    						double temp=dy;
		    						dy=-dx;
		    						dx=temp;
		    					}
		    				}
		    				
		    			}
		    		}
		    	}
				
				//change movement
				changeVelocity(avg(val1, wgt), avg(val2, wgt));
				changeVelocity2(angle(movX, movY), speed/2.0);
				
				if(danger){
					val1.clear();
					val2.clear();
					wgt.clear();
					val1.add(movX);
					val2.add(movY);
					wgt.add(5000.0);
					val1.add(dx);
					val2.add(dy);
					wgt.add(5.0*(100.0-closest)/100.0);
					changeVelocity(avg(val1, wgt), avg(val2, wgt));
					changeVelocity2(angle(movX, movY), speed/2.0);
				}
			}
			
			//chasing
			if(action==5){
				ArrayList<Double> val1=new ArrayList<Double>();
				ArrayList<Double> val2=new ArrayList<Double>();
				ArrayList<Double> wgt=new ArrayList<Double>();
				
				double len=dis(movX, movY);
				val1.add(dirX);
				val1.add(movX);
				val2.add(dirY);
				val2.add(movY);
				wgt.add(1.0);
				wgt.add(50.0);
				
				//change movement
				changeVelocity(avg(val1, wgt), avg(val2, wgt));
				changeVelocity2(angle(movX, movY), speed*0.01+len*0.99);
				
				//lose some energy
				energy=Math.max(energy-1.0, 0);
			}
			
			//eating
			if(action==6){
				//slow down
				dirX=0;
				dirY=0;
				ArrayList<Double> val1=new ArrayList<Double>();
				ArrayList<Double> val2=new ArrayList<Double>();
				ArrayList<Double> wgt=new ArrayList<Double>();
				
				val1.add(dirX);
				val1.add(movX);
				val2.add(dirY);
				val2.add(movY);
				wgt.add(2.0);
				wgt.add(20.0);
				changeVelocity(avg(val1, wgt), avg(val2, wgt));
				
				//eat
				hunger+=2;
			}
			
			//resting
			if(action==8){
				
				//slow down
				dirX=0;
				dirY=0;
				ArrayList<Double> val1=new ArrayList<Double>();
				ArrayList<Double> val2=new ArrayList<Double>();
				ArrayList<Double> wgt=new ArrayList<Double>();
				
				val1.add(dirX);
				val1.add(movX);
				val2.add(dirY);
				val2.add(movY);
				wgt.add(2.0);
				wgt.add(20.0);
				
				changeVelocity(avg(val1, wgt), avg(val2, wgt));
				
				//recover energy
				energy=Math.min(energy+0.5, maxEnergy);
			}
			
			//water is dangerous
			if(action==3||action==4||action==5){
				ArrayList<Double> val1=new ArrayList<Double>();
				ArrayList<Double> val2=new ArrayList<Double>();
				ArrayList<Double> wgt=new ArrayList<Double>();
				
				//stay away from water
				boolean danger=false;
				double dx=0, dy=0, closest=500;
				
				for(int i=-10;i<=10;i++){
					for(int j=-10;j<=10;j++){
						int x=modsz(posX+i);
						int y=modsz(posY+j);
						if(m.type[x][y]==3&&detect(x, y)){
			    			danger=true;
			    			if(closest>dis(i, j)){
			    				closest=dis(i, j);
			    				dx=posX-x;
			    				dy=posY-y;
			    			}
						}
					}
				}
				
				//change movement
				if(danger){
					val1.add(movX);
					val2.add(movY);
					wgt.add(5.0);
					val1.add(dx);
					val2.add(dy);
					if(closest>2)wgt.add(0.05);
					else wgt.add(5.0);
					changeVelocity(avg(val1, wgt), avg(val2, wgt));
					changeVelocity2(angle(movX, movY), speed/2.0);
				}
			}
		}
		
		//each tick, controls animal's actions
		public void act(ArrayList<Animal> animals){
			
			//approximate speed of animal
			speed=0.5*Math.sqrt(sp.strng);
			
			//select and carry out action
			action=selectAction(animals);
			determine(animals);
			
			if(!alive)return;
			
			//move
			move();
			
			//determine current physical/mental values
			energy=Math.min(energy+0.5, maxEnergy);
			age+=0.1;
			delay++;
			if(delay>1000)delay=1000;
			if(hunger<20)health-=2;
			if(hunger>60)health+=2;
			if(health>1000)health=1000;
			if(sp.eatAnimals)hunger-=0.02;
			else hunger-=0.5;
			
			//check if drowning
			boolean drowning=true;
			for(int i=-1;i<=1;i++){
				for(int j=-1;j<=1;j++){
					if(Math.abs(i)+Math.abs(j)<1.5){
						if(m.type[modsz(posX+i)][modsz(posY+j)]!=3)drowning=false;
					}
				}
			}
			
			//die from drowning
			if(drowning)health-=40;
			
			//die from old age
			if(age>=sp.deathAge)health-=(2*(age-sp.deathAge));
			
			//grow
			total+=(health/10000.0);
			if(total>2*sp.deathAge)total=2*sp.deathAge;
			if(total<EPS)total=0;
		}
		
		//get current size of animal
		public int getSize(){
			return sp.size[(int)total];
		}
		
		//change movement
		public void changeVelocity(double x, double y){
			movX=x;
			movY=y;
		}
		
		//change movement using direction and magnitude
		public void changeVelocity2(double arg, double abs){
			movX=abs*Math.cos(arg);
			movY=abs*Math.sin(arg);
		}
		
		//draw the animal
		public void draw(Graphics G){
		    Color tmp=G.getColor();
		    G.setColor(new Color((int)colR, (int)colG, (int)colB));
		    
		    int radius=(int)(getSize()*zoom);
		    G.fillOval((int)((posX-shiftX)*zoom), (int)((posY-shiftY)*zoom), radius, radius);
		    
		    if(radius>6){
		    	double ang=angle(movX, movY);
		    	double x=(int)((posX-shiftX)*zoom)+radius/2.0;
		    	double y=(int)((posY-shiftY)*zoom)+radius/2.0;
		    	G.setColor(Color.white);
		    	G.fillOval((int)x, (int)y, 3, 3);
		    }
		    
		    G.setColor(tmp);
		}
	}
}

//map class determines terrain
class Map{
	//type is type of terrain of a square
	int[][] type;
	//fruit is amount of food in a square
	int[][] fruit;
	
	public Map(int a, int b){
		type=new int[a][b];
		fruit=new int[a][b];
		int[] spread=new int[6];
		
		//approximately how frequently a terrain type occurs
		spread[0]=4;
		spread[1]=4;
		spread[2]=4;
		spread[3]=8;
		spread[4]=2;
		spread[5]=2;
		
		//generate random map
		Queue<Integer> q1=new LinkedList<Integer>();
		Queue<Integer> q2=new LinkedList<Integer>();
		Queue<Integer> q3=new LinkedList<Integer>();
		Queue<Integer> q4=new LinkedList<Integer>();
		
		int[][] dis=new int[a][b];
		for(int i=0;i<a;i++)for(int j=0;j<b;j++)dis[i][j]=100*a*b+1;
		for(int i=0;i<Math.sqrt(Math.sqrt(a*b));i++){
			int x=(int)(Math.random()*2*a/3+a/6.0);
			int y=(int)(Math.random()*2*b/3+b/6.0);
			int c=(int)(Math.random()*5);
			if(c>=2)c++;
			//0 = rock
			//1 = bad soil
			//2 = good soil
			//3 = water
			//4 = grass
			//5 = trees
			q1.add(x);
			q2.add(y);
			q3.add(c);
			q4.add(spread[c]);
			type[x][y]=c;
			dis[x][y]=0;
		}
		
		//generate sea
		for(int i=0;i<a;i++){
			int x;
			int y;
			double r=Math.random();
			int spr=spread[3];
			if(r<0.25){
				x=(int)(Math.random()*20);
				y=(int)(Math.random()*b);
				if(x<10){
					if(y<50||y>b-50)spr=(int)(Math.random()*2)+1;
					else if(y<100||y>b-100)spr=(int)(Math.random()*2)+2;
				}
			}
			else if(r<0.50){
				x=a-1-(int)(Math.random()*20);
				y=(int)(Math.random()*b);
				if(x>a-10){
					if(y<50||y>b-50)spr=(int)(Math.random()*2)+1;
					else if(y<100||y>b-100)spr=(int)(Math.random()*2)+2;
				}
			}
			else if(r<0.75){
				x=(int)(Math.random()*a);
				y=(int)(Math.random()*20);
				if(y<10){
					if(x<50||x>b-50)spr=(int)(Math.random()*2)+1;
					else if(x<100||x>b-100)spr=(int)(Math.random()*2)+2;
				}
			}
			else{
				x=(int)(Math.random()*a);
				y=b-1-(int)(Math.random()*20);
				if(y>b-10){
					if(x<50||x>b-50)spr=(int)(Math.random()*2)+1;
					else if(x<100||x>b-100)spr=(int)(Math.random()*2)+2;
				}
			}
			int c=3;
			//0 = rock
			//1 = bad soil
			//2 = good soil
			//3 = water
			//4 = grass
			//5 = trees
			q1.add(x);
			q2.add(y);
			q3.add(c);
			q4.add(spr);
			type[x][y]=c;
			dis[x][y]=0;
		}
		
		
		int[][] dir={{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
		while(!q1.isEmpty()){
			int curx=q1.poll();
			int cury=q2.poll();
			int curc=q3.poll();
			int curs=q4.poll();
			//if(Math.random()<0.05*spread[curc])continue;
			if(curc!=type[curx][cury])continue;
			for(int k=0;k<4;k++){
				int i=dir[k][0];
				int j=dir[k][1];
				if(curx+i>=0&&curx+i<a&&cury+j>=0&&cury+j<b){
					if(dis[curx+i][cury+j]>dis[curx][cury]+curs){
						dis[curx+i][cury+j]=dis[curx][cury]+curs;
						q1.add(curx+i);
						q2.add(cury+j);
						q3.add(curc);
						q4.add(curs);
						type[curx+i][cury+j]=curc;
					}
				}
			}
		}
		
		//generate fruit values
		//only for type 4/5 terrains
		for(int i=0;i<a;i++){
			for(int j=0;j<b;j++){
				if(type[i][j]==4){
					if(Math.random()<0.8)fruit[i][j]=0;
					else fruit[i][j]=1;
				}
				if(type[i][j]==5){
					fruit[i][j]=(int)(Math.random()*5);
				}
			}
		}
	}
	
	//update map
	public void rebuild(){
		for(int i=0;i<500;i++){
			for(int j=0;j<500;j++){
				if(type[i][j]>=4){
					if(Math.random()<0.001)fruit[i][j]++;
					if(fruit[i][j]<0)fruit[i][j]=0;
				}
			}
		}
		
	}
}
