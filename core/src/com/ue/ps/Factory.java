package com.ue.ps;

public class Factory extends Building{
	
	private int resourceInput = 3;
	private int shipProgressOutput = 10;
	

	public Ship buildingShip;
	
	public Factory() {
		super("assets/factory.png");
		this.health = 50;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void update(Planet p){
	
		if (p.resource > resourceInput){
			p.resource -= resourceInput;

			buildingShip.buildProgress += shipProgressOutput;
		}
		
		
		
	}

}
