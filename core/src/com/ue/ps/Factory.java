package com.ue.ps;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class Factory extends Building {

	private int resourceInput = 3;
	private int shipProgressOutput = 10;
	

	public ShipType buildingShip;
	
	public int buildProgress;
	

	public Factory() {
		super("assets/factory.png");
		this.health = 50;
		this.resourceCost = 5;
		this.id = 'f';
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(Planet p) {
		
		
	}

}
