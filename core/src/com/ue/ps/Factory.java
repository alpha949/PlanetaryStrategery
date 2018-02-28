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
		//check to see if it can work on building the ship
		if (buildingShip != null && p.resource > resourceInput) {
			p.resource -= resourceInput;

			buildProgress += shipProgressOutput;
			System.out.println("Ship Prog: " + buildProgress + "/" + buildingShip.getStat(2) );
		}
		//check to see if ship is built
		if (buildingShip != null && buildProgress >= buildingShip.getStat(2)) {
		
			Ship.spawnShip(owner, p, buildingShip, 90);
			GameServerClient.packet.addAction(Action.buildShip(p.id, buildingShip.getId()));
			buildProgress = 0;
			this.buildingShip = null;
		}
		
		
	}

}
