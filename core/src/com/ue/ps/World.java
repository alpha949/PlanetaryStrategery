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
	
	public static void setWorld(ArrayList<PlanetData> planets){
		for (PlanetData pd : planets) {
			allPlanets.add(PlanetData.fromPlanetData(pd));
		}
	}
	
	public static ArrayList<Planet> getWorld(){
		return allPlanets;
	}
}
