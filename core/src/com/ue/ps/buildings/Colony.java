package com.ue.ps.buildings;

import com.ue.ps.Planet;

public class Colony extends Building {

	private int production = 1;

	public Colony() {
		super(BuildingType.colony);
		this.health = 100;
		this.resourceCost = 6;
		this.id = 'c';

	}

	@Override
	public void update(Planet p) {
		p.people += production;
		owner.techPoints += 1;

	}

}
