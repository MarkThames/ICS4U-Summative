package LifeSimulator;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import LifeSimulator.GraphMenu.Graph;

public class Island extends JFrame implements ActionListener, ChangeListener{
	IslandLife island;
	JLabel content;
	Timer t;
	Species[] list;
	myMouseAdapter mouse;
	public Island(){
		makeSpecies();
		island=new IslandLife(list);
		content=new JLabel();
		content.setLayout(new BorderLayout());
		content.add(island, "West");
		//content.add(idk);
		setContentPane (content);
        pack ();
        setTitle ("World");
        setSize (515, 535);
        setMaximumSize(new Dimension(515,535));
        setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo (null);
        setVisible(true);
        t=new Timer(10, this);
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
		Utility.incrementGlobalCounter("Species",2);
		list=new Species[2];
		list[0]=new Species("Mouse", Color.red, 50, 200, true, false, 1, 3, 4, 2.0);
		list[1]=new Species("Cat", Color.magenta, 100, 400, false, true, 2, 5, 7, 10.0);
		((ArrayList<Species>)Utility.getGlobalObject("Species")).add(list[0]);
		((ArrayList<Species>)Utility.getGlobalObject("Species")).add(list[1]);
	}
	public static void main(String[] args) {
		Island world=new Island();

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
		
		public void mouseWheelMoved(MouseWheelEvent e){
			island.changeZoom(0.1*e.getPreciseWheelRotation(), e.getX(), e.getY());
			//System.out.println(e.getX()+" "+e.getY());
		}
		
		public void mouseClicked(MouseEvent e) {
			Species S = (Species)Utility.getGlobalObject("To Insert");
            if(S!=null){
                island.addAnimal(S, e.getX()-12, e.getY()-30);
            }
		}

		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void mousePressed(MouseEvent e) {
			if(SwingUtilities.isLeftMouseButton(e)){
				sX=e.getX();
				sY=e.getY();
			}
		}
		
		public void mouseDragged(MouseEvent e){
			if(SwingUtilities.isLeftMouseButton(e)){
				island.changeShift(sX-e.getX(), sY-e.getY());
				sX=e.getX();
				sY=e.getY();
			}
		}

		public void mouseReleased(MouseEvent e) {
			
		}
		
	}
}
class IslandLife extends JPanel{
	ArrayList<Animal> creatures;
	int animalId=1, ticks;
	Species[] list;
	int xLength, yLength;
	Map m;
	double zoom=1, shiftX=0, shiftY=0;
	ArrayList<Integer>[] pop;
	public IslandLife(Species[] arr){
		pop=new ArrayList[20];
		for(int i=0;i<20;i++)pop[i]=new ArrayList<Integer>();
		ticks=0;
		list=arr;
		setPreferredSize(new Dimension(500, 500));
		xLength=500;
		yLength=500;
		m=new Map(xLength, yLength);
		creatures=new ArrayList<Animal>();
		for(int i=0;i<100;i++){
			Animal test=new Animal(500*Math.random(), 500*Math.random(), list[0], animalId++);
			addIn(test);
			test.randomAge();
		}
		for(int i=0;i<10;i++){
			Animal test=new Animal(500*Math.random(), 500*Math.random(), list[1], animalId++);
			addIn(test);
			test.randomAge();
		}
	}
	public void addAnimal(Species S, double x, double y){
		Animal a=new Animal(x, y, S, animalId++);
	    addIn(a);
	    a.changeVelocity(0, 0);
	}
	public void changeZoom(double val, double x, double y){
		//x/zoom+shiftX = x/(zoom+val)+nX
		//y/zoom
		double nxt=Math.max(1.0, Math.min(5.0, zoom+val));
		shift(x/zoom-x/nxt, y/zoom-y/nxt);
		zoom=nxt;
	}
	public void changeShift(double x, double y){
		shift(x/zoom, y/zoom);
	}
	private void shift(double x, double y){
		shiftX+=x;
		shiftY+=y;
		if(shiftX<0)shiftX=0;
		if(shiftX+500/zoom>500)shiftX=500-500/zoom;
		if(shiftY<0)shiftY=0;
		if(shiftY+500/zoom>500)shiftY=500-500/zoom;
	}
	public void getData(){
		
	}
	private void updatePop(){
		int[] count=new int[20];
		for(Animal A: creatures){
			if(A.alive){
				count[A.sp.id]++;
			}
		}
		for(int i=0;i<20;i++)pop[i].add(count[i]);
	}
	private void addIn(Animal A){
		creatures.add(A);
	}
	private void discardGone(){
		ArrayList<Animal> temp=new ArrayList<Animal>();
		for(Animal A:creatures){
			if(!A.gone)temp.add(A);
		}
		creatures=temp;
	}
    public void paintComponent(Graphics G){
    	G.setColor(Color.white);
    	G.fillRect(0, 0, 800, 800);
    	Color[] mapColors=new Color[5];
    	mapColors[0]=new Color(100, 100, 100);
    	mapColors[1]=new Color(255, 255, 0);
    	mapColors[2]=new Color(160, 40, 40);
    	mapColors[3]=new Color(0, 255, 0);
    	mapColors[4]=new Color(0, 150, 50);
    	for(int i=0;i<xLength;i+=4){
    		for(int j=0;j<yLength;j+=4){
    			G.setColor(mapColors[m.type[(int)(Math.max(Math.min(i/zoom+shiftX, 499.4), 0))][(int)(Math.max(Math.min(j/zoom+shiftY, 499.4), 0))]]);
    			G.drawRect(i, j, (int)(2), (int)(2));
    			//System.out.println(m.type[i][j]);
    		}
    	}
        for(Animal A:creatures)A.draw(G);
        repaint();
    }
	public void tick(){
		if(ticks==0)updatePop();
		ticks++;
		for(Animal A: creatures){
			A.act(creatures);
		}
		discardGone();
		breed();
		rebuildMap();
		ticks%=10;
	}
	public void breed(){
		ArrayList<Animal> temp=new ArrayList<Animal>(creatures);
		for(Animal A: temp){
			for(Animal B: temp){
				if(A.sp.id==B.sp.id&&A.alive&&B.alive&&A.id!=B.id){
					if(A.hunger>40&&B.hunger>40&&A.delay>500&&B.delay>500&&A.dis(B)<10&&(A.action!=1&&A.action!=2&&A.action!=5)&&(B.action!=1&&B.action!=2&&B.action!=5)&&A.age>A.sp.adultAge&&B.age>B.sp.adultAge){
						if(Math.random()<(A.health*B.health)*0.0001*0.1){
							A.delay-=500;
							B.delay-=500;
							int num=1;
							for(int i=0;i<num;i++){
								Animal test=new Animal(0.5*A.posX+0.5*B.posX, 0.5*A.posY+0.5*B.posY, A.sp, animalId++);
								addIn(test);
							}
						}
					}
				}
			}
		}
	}
	public void rebuildMap(){
		for(int i=0;i<500;i++){
			for(int j=0;j<500;j++){
				if(m.type[i][j]>=3){
					if(Math.random()<0.001)m.fruit[i][j]++;
				}
			}
		}
		
	}
	class Animal{
		public double colR, colG, colB;
		private static final double EPS=1e-4;
		public int radius, action=0;
		public double movX, movY, posX, posY, until, dirX=0, dirY=0;
		public int thirst=70, health=100, delay=1000;
		public double age=0, total=0, hunger=70;
		public double speed=0;
		public int id;
		public int[] actionDetails;
		public double eye=1.8;
		public double eye2=100;
		public double ear=30;
		public Species sp;
		public double energy=100, maxEnergy=100;
		public boolean alive=true, gone=false;
			 // constructor
		public Animal(double x, double y, Species s, int i){
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
		public void randomAge(){
			age=Math.random()*sp.deathAge*0.7;
			total=age;
		}
		private double modp(double x){
			while(x>=2*Math.PI)x-=(2*Math.PI);
		 	while(x<0)x+=(2*Math.PI);
		 	return x;
		}
		private int modsz(double x){
			return (500+((int)x)%500)%500;
		}
		public void move(){
		    posX+=movX;
		    posY+=movY;
		    while(posX>=500)posX-=500;
		    while(posY>=500)posY-=500;
		    while(posX<0)posX+=500;
		    while(posY<0)posY+=500;
		}
		private boolean detect(Animal n){
			double ang=angle(n);
			double len=dis(n);
			double arg=angle(movX, movY);
			if(modp(arg-ang)<eye||modp(ang-arg)<eye)if(len<eye2)return true;
			if(len<ear)return true;
			if(Math.random()<0.01)if(len<eye2)return true;
			return false;
		}
		private boolean detect(double x, double y){
			double ang=angle(x, y);
			double len=dis(x, y);
			double arg=angle(movX, movY);
			if(modp(arg-ang)<eye||modp(ang-arg)<eye)if(len<eye2)return true;
			if(len<ear)return true;
			if(Math.random()<0.01)if(len<eye2)return true;
			return false;
		}
		private boolean dangerous(Animal n){
			if(!(n.sp).eatAnimals)return false;
			if(!sp.eatPlants)return false;
			if(n.getSize()>getSize())return true;
			return false;
		}
		private boolean delicious(Animal n){
			if(!(sp).eatAnimals)return false;
			if(!(n.sp).eatPlants)return false;
			if(getSize()>n.getSize())return true;
			return false;
		}
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
		private void feed(int val){
			hunger+=val;
		}
		private double angle(Animal n){
			return angle(n.posX-posX, n.posY-posY);
		}
		private double dis(double x, double y){
			return Math.sqrt(x*x+y*y);
		}
		private double dis(Animal n){
			return dis((n.posX-posX), (n.posY-posY));
		}
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
			//9 = fighting
			//10 = reacting
			
			if(health<EPS){
				alive=false;
				return 1;
			}
			for(Animal A: animals){
				if(dangerous(A)&&dis(A)<4){
					if(Math.random()>sp.strng/A.sp.strng)health-=20;
				}
			}
			for(Animal A: animals){
				if(dangerous(A)&&detect(A)){
					if(action==2)return 2;
					if(A.action==5||dis(A)<20)return 2;
				}
			}
			if(energy<40+EPS)return 8;
			if(energy<80&&action==8)return 8;
			if(hunger<80&&action==6)return 6;
			if(hunger<30&&sp.eatPlants){
				double closest=500, locX=0, locY=0;
				for(int i=-10;i<=10;i++){
					for(int j=-10;j<=10;j++){
						int x=modsz(posX+i);
						int y=modsz(posY+j);
						if(m.type[x][y]>=3){
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
					for(int i=-2;i<=2;i++){
						for(int j=-2;j<=2;j++){
							m.fruit[(((int)locX)+i+500)%500][(((int)locY)+j+500)%500]--;
						}
					}
					
					return 6;
				}
				if(closest<400){
					dirX=locX-posX;
					dirY=locY-posY;
					double ang=angle(dirX, dirY);
					dirX=Math.max((80.0-3.0*closest), 20.0)*Math.cos(ang);
					dirY=Math.max((80.0-3.0*closest), 20.0)*Math.sin(ang);
					return 3;
				}
			}
			if(hunger<80&&sp.eatAnimals){
				double closest=500, locX=0, locY=0;
				for(Animal A: creatures){
					if(delicious(A)&&!A.alive){
						if(dis(A)<closest){
							closest=dis(A);
							locX=A.posX;
							locY=A.posY;
						}
					}
				}
				if(closest<10){
					dirX=locX-posX;
					dirY=locY-posY;
					double ang=angle(dirX, dirY);
					dirX=5.0*Math.cos(ang);
					dirY=5.0*Math.sin(ang);
					return 3;
				}
			}
			if(hunger<30&&energy>40&&sp.eatAnimals){
				double closest=500, locX=0, locY=0;
				for(Animal A:creatures){
					if(delicious(A)&&detect(A)){
						if(dis(A)<closest){
							closest=dis(A);
							locX=A.posX;
							locY=A.posY;
						}
					}
				}
				if(closest<400){
					dirX=locX-posX;
					dirY=locY-posY;
					double ang=angle(dirX, dirY);
					dirX=Math.max((80.0-3.0*closest), 20.0)*Math.cos(ang);
					dirY=Math.max((80.0-3.0*closest), 20.0)*Math.sin(ang);
					return 5;
				}
			}
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
			if((Math.random()<0.02||(action==4&&Math.random()<0.98))&&closest<400){
				dirX=locX-posX;
				dirY=locY-posY;
				double ang=angle(dirX, dirY);
				dirX=2.0*Math.cos(ang);
				dirY=2.0*Math.sin(ang);
				return 4;
			}
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
		private void determine(ArrayList<Animal> animals){
			
			if(action==1){
				movX=0;
				movY=0;
				colR=0*0.001+colR*0.99;
				colG=0*0.001+colG*0.99;
				colB=0*0.001+colB*0.99;
				for(Animal A: animals){
					if(A.sp.eatAnimals&&dis(A)<4){
						A.feed(2);
						colR=0*0.001+colR*0.99*0.99;
						colG=0*0.001+colG*0.99*0.99;
						colB=0*0.001+colB*0.99*0.99;
					}
				}
				if((int)(colR)<40&&(int)(colG)<40&&(int)(colB)<40)gone=true;
			}
			if(action==2){
				ArrayList<Double> val1=new ArrayList<Double>();
				ArrayList<Double> val2=new ArrayList<Double>();
				ArrayList<Double> wgt=new ArrayList<Double>();
				val1.add(movX);
				val2.add(movY);
				wgt.add(50.0);
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
				double curAbs=dis(movX, movY);
				changeVelocity(avg(val1, wgt), avg(val2, wgt));
				changeVelocity2(angle(movX, movY), curAbs*0.95+speed*(0.2+0.8*energy*0.01)*0.05);
				energy=Math.max(energy-1.0, 0);
			}
			if(action==3){
				ArrayList<Double> val1=new ArrayList<Double>();
				ArrayList<Double> val2=new ArrayList<Double>();
				ArrayList<Double> wgt=new ArrayList<Double>();
				val1.add(dirX);
				val1.add(movX);
				val2.add(dirY);
				val2.add(movY);
				wgt.add(1.0);
				wgt.add(50.0);
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
			if(action==3){
				ArrayList<Double> val1=new ArrayList<Double>();
				ArrayList<Double> val2=new ArrayList<Double>();
				ArrayList<Double> wgt=new ArrayList<Double>();
				val1.add(dirX);
				val1.add(movX);
				val2.add(dirY);
				val2.add(movY);
				wgt.add(1.0);
				wgt.add(50.0);
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
				changeVelocity(avg(val1, wgt), avg(val2, wgt));
				changeVelocity2(angle(movX, movY), speed*0.01+len*0.99);
				energy=Math.max(energy-1.0, 0);
			}
			if(action==6){
				dirX=0;
				dirY=0;
				ArrayList<Double> val1=new ArrayList<Double>();
				ArrayList<Double> val2=new ArrayList<Double>();
				ArrayList<Double> wgt=new ArrayList<Double>();
				double len=dis(movX, movY);
				val1.add(dirX);
				val1.add(movX);
				val2.add(dirY);
				val2.add(movY);
				wgt.add(1.0);
				wgt.add(20.0);
				feed(2);
			}
			if(action==8){
				dirX=0;
				dirY=0;
				ArrayList<Double> val1=new ArrayList<Double>();
				ArrayList<Double> val2=new ArrayList<Double>();
				ArrayList<Double> wgt=new ArrayList<Double>();
				double len=dis(movX, movY);
				val1.add(dirX);
				val1.add(movX);
				val2.add(dirY);
				val2.add(movY);
				wgt.add(1.0);
				wgt.add(20.0);
				changeVelocity(avg(val1, wgt), avg(val2, wgt));
				energy=Math.min(energy+0.5, maxEnergy);
			}
		}
		
		public void act(ArrayList<Animal> animals){
			speed=0.5*Math.sqrt(sp.strng);
			action=selectAction(animals);
			determine(animals);
			if(!alive)return;
			move();
			energy=Math.min(energy+0.5, maxEnergy);
			//System.out.println(hunger);
			age+=0.1;
			delay++;
			if(delay>1000)delay=1000;
			if(hunger<20)health--;
			if(sp.eatAnimals)hunger-=0.02;
			else hunger-=0.5;
			if(age>=sp.deathAge)health-=(2*(age-sp.deathAge));
			total+=(health/1000.0);
			if(total<EPS)total=0;
		}
		
		public int getSize(){
			return sp.size[(int)total];
		}
		
		public void changeVelocity(double x, double y){
			movX=x;
			movY=y;
		}
		public void changeVelocity2(double arg, double abs){
			movX=abs*Math.cos(arg);
			movY=abs*Math.sin(arg);
		}
		public void draw(Graphics G){
		    Color tmp=G.getColor();
		    G.setColor(new Color((int)colR, (int)colG, (int)colB));
		    radius=(int)(getSize()*zoom);
		    G.fillOval((int)((posX-shiftX)*zoom), (int)((posY-shiftY)*zoom), radius, radius);
		    G.setColor(tmp);
		}
	}

}
class Map{
	int[][] type;
	int[][] fruit;
	public Map(int a, int b){
		type=new int[a][b];
		fruit=new int[a][b];
		int[] spread=new int[5];
		spread[0]=3;
		spread[1]=3;
		spread[2]=1;
		spread[3]=1;
		spread[4]=1;
		Queue<Integer> q1=new LinkedList<Integer>();
		Queue<Integer> q2=new LinkedList<Integer>();
		Queue<Integer> q3=new LinkedList<Integer>();
		int[][] dis=new int[a][b];
		for(int i=0;i<a;i++)for(int j=0;j<b;j++)dis[i][j]=100*a*b+1;
		for(int i=0;i<Math.sqrt(Math.sqrt(a*b));i++){
			int x=(int)(Math.random()*a);
			int y=(int)(Math.random()*b);
			int c=(int)(Math.random()*4);
			if(c>=2)c++;
			//0 = rock
			//1 = bad soil
			//2 = good soil
			//3 = grass
			//4 = trees
			q1.add(x);
			q2.add(y);
			q3.add(c);
			type[x][y]=c;
			dis[x][y]=0;
		}
		int[][] dir={{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
		while(!q1.isEmpty()){
			int curx=q1.poll();
			int cury=q2.poll();
			int curc=q3.poll();
			//if(Math.random()<0.05*spread[curc])continue;
			if(curc!=type[curx][cury])continue;
			for(int k=0;k<4;k++){
				int i=dir[k][0];
				int j=dir[k][1];
				if(curx+i>=0&&curx+i<a&&cury+j>=0&&cury+j<b){
					if(dis[curx+i][cury+j]>dis[curx][cury]+spread[curc]){
						dis[curx+i][cury+j]=dis[curx][cury]+spread[curc];
						q1.add(curx+i);
						q2.add(cury+j);
						q3.add(curc);
						type[curx+i][cury+j]=curc;
					}
				}
			}
		}
		for(int i=0;i<a;i++){
			for(int j=0;j<b;j++){
				if(type[i][j]==3){
					if(Math.random()<0.8)fruit[i][j]=0;
					else fruit[i][j]=1;
				}
				if(type[i][j]==4){
					fruit[i][j]=(int)(Math.random()*5);
				}
			}
		}
	}
}