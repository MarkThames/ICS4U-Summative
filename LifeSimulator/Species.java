package LifeSimulator;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

class Species {
	String name;
	Color c;
	int adultAge, deathAge;
	boolean eatPlants;
	boolean eatAnimals;
	int id;
	int[] size;
	double strng;
	public Species(String str, Color col, int ad, int de, boolean pl, boolean an, int i, int minSize, int maxSize, double strength){
		name=str;
		c=col;
		adultAge=ad;
		deathAge=de;
		eatPlants=pl;
		eatAnimals=an;
		id=i;
		size=new int[2*de+205];
		for(int j=0;j<2*de+205;j++){
			size[j]=(int)(minSize+j*(1.0*(maxSize-minSize)/(1.0*de)));
		}
		strng=strength;
	}

}