package com.ue.ps.buildings;

import com.ue.ps.Planet;
import com.ue.ps.ships.ShipType;

public class Factory extends Building {

	

	public ShipType buildingShip;

	public int buildProgress;

	public Factory() {
		super(BuildingType.factory);
		this.health = 50;
		this.resourceCost = 5;
		this.id = 'f';
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(Planet p) {

	}

}
