package com.ue.ps.buildings;

import com.ue.ps.Planet;
import com.ue.ps.ships.ShipType;
import com.ue.ps.ui.Images;

public class Factory extends Building {

	

	public ShipType buildingShip;

	public int buildProgress;

	public Factory() {
		super(Images.factory);
		this.id = 'f';
		this.health = 60;
		this.resourceCost = 5;
		this.type = Building.Types.factory;
		
	}

	@Override
	public void update(Planet p) {

	}

}
