package com.ue.ps.buildings;

import com.ue.ps.Planet;
import com.ue.ps.ships.Ship;
import com.ue.ps.ships.ShipType;
import com.ue.ps.ui.Images;

public class Factory extends Building {

	public ShipType buildingShip;

	public int buildProgress, consumption, efficiency;

	public Factory() {
		super(Images.factory);
		this.id = 'f';
		this.health = 60;
		this.resourceCost = 5;
		this.type = Building.Types.factory;
		this.buildProgress = 0;
		this.consumption = 6;
		this.efficiency = 3;
	}

	@Override
	public void update(Planet p) {
		int intake = this.getConsumption(p) - (this.getConsumption(p) % efficiency);
		p.resource -= intake;
		this.buildProgress += intake;
		
		if((this.buildProgress == this.buildingShip.getStat(2) + p.owner.faction.getMod(buildingShip, 2)) && (buildingShip != null)){
			Ship.spawnShip(p.owner, p, buildingShip, 0);
			buildProgress = 0;
			buildingShip = null;
		}
	}
	
	public int getConsumption(Planet p){
		int a = this.buildingShip.getStat(2) + p.owner.faction.getMod(buildingShip, 2) - buildProgress;
		a *= efficiency;
		return consumption <= a ? consumption : a;
	}

}
