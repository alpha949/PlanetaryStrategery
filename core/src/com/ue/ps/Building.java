package com.ue.ps;

import java.util.ArrayList;

public abstract class Building extends BaseActor {
	public int health;

	public int resourceCost;
	public static ArrayList<Building> allBuildings = new ArrayList<Building>();
	public char id;

	public Building(String path) {
		super(path);
		

	}

	public abstract void update(Planet p);

	public void update(Player P, Planet p) {
	}
	
	public static Building getBuildingFromId(char id) {
		for (Building b : allBuildings) {
			if (b.id == id) {
				return b;
			}
		}
		return null;
	}
	
	//all buildings must be registered here, if some one can find a way to do this automagically, that would be awsome
	static {
		allBuildings.add(new Factory());
		allBuildings.add(new Colony());
		allBuildings.add(new Mine());
	
	}

}
