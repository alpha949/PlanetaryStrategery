package com.ue.ps.buildings;

import com.ue.ps.Planet;
import com.ue.ps.ui.Images;

public class Mine extends Building {

	public int production = 5;

	public Mine() {
		super(Images.mine);

		this.id = 'm';
		this.health = 50;
		this.resourceCost = 5;
		
		this.type = Building.Types.mine;
	}

	@Override
	public void update(Planet p) {
		p.resource += this.production;
		System.out.println("Planet resource: " + p.resource);

	}
}
