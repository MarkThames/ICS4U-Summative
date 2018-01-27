package LifeSimulator;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

class Species {
	//name of species
	String name;
	//colour
	Color c;
	//how old it gets
	int adultAge, deathAge;
	//diet
	boolean eatPlants;
	boolean eatAnimals;
	//id is used to differentiate species
	//t1 controls size
	//t2 controls strength
	int id, t1, t2;
	int[] size;
	//strength helps with speed, energy, attacking/defending
	double strng;
	//constructor
	public Species(String str, Color col, int ad, int de, boolean pl, boolean an, int i, int t1, int t2){
		name=str;
		c=col;
		adultAge=ad;
		deathAge=de;
		eatPlants=pl;
		eatAnimals=an;
		id=i;
		this.t1=t1;
		this.t2=t2;
		int[][] sizeValues={{2, 3}, {3, 4}, {4, 5}};
        double[] strengthValues={2.0, 5.0, 12.0};
        double minSize=sizeValues[t1][0];
        double maxSize=sizeValues[t1][1];
		size=new int[2*de+205];
		for(int j=0;j<2*de+205;j++){
			size[j]=(int)(minSize+j*(1.0*(maxSize-minSize)/(1.0*de)));
		}
		strng=strengthValues[t2];
	}
	//update values
	public void update(String str, int ad, int de, boolean pl, boolean an, int t1, int t2){
		name=str;
		adultAge=ad;
		deathAge=de;
		eatPlants=pl;
		eatAnimals=an;
		this.t1=t1;
		this.t2=t2;
		int[][] sizeValues={{2, 3}, {3, 4}, {7, 9}};
        double[] strengthValues={2.0, 5.0, 12.0};
        double minSize=sizeValues[t1][0];
        double maxSize=sizeValues[t1][1];
		size=new int[2*de+205];
		for(int j=0;j<2*de+205;j++){
			size[j]=(int)(minSize+j*(1.0*(maxSize-minSize)/(1.0*de)));
		}
		strng=strengthValues[t2];
	}
}
