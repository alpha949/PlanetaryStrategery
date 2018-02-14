package com.ue.ps;

import java.util.ArrayList;

public class World {
	
	
	private static ArrayList<Planet> allPlanets = new ArrayList<Planet>();
	
	public static Planet getPlanetById(int id){
		for (Planet p : allPlanets){
			if (p.id == id){
				return p;
			}
		}
		return null;
	}
	
	public static void setWorld(ArrayList<Planet> planets){
		allPlanets = planets;
	}
	
	public static ArrayList<Planet> getWorld(){
		return allPlanets;
	}
}
