package com.ue.ps.buildings;

import com.ue.ps.Planet;
import com.ue.ps.ships.Ship;
import com.ue.ps.ships.ShipType;
import com.ue.ps.ui.Images;

public class SpaceFactory extends Building{

	public ShipType buildingShip;
	
	public int buildProgress, consumption, efficiency;
	
	public SpaceFactory() {
		super(Images.spaceFactory);
		this.id = 's';
		this.health = 50;
		this.resourceCost = 5; //should cost more but build ships cheaper/faster
		this.buildProgress = 0;
		this.isSpace = true;
		this.type = Building.Types.spaceFactory;
		this.consumption = 4;
		this.efficiency = 4;
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
