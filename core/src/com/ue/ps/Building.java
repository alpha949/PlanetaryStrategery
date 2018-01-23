package com.ue.ps;

public abstract class Building extends BaseActor {
	public int health;

	public Building(String path) {
		super(path);

	}

	public void update(Planet p) {
	}

	public void update(Player P, Planet p) {
	}
	
	

}
