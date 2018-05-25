package com.ue.ps.buildings;

import com.ue.ps.Planet;
import com.ue.ps.ui.Images;

public class Colony extends Building {

	private int production = 1;

	public Colony() {
		super(Images.colony);
		this.id = 'c';
		this.health = 100;
		this.resourceCost = 6;
		this.type = Building.Types.colony;

	}

	@Override
	public void update(Planet p) {
		p.people += production;
		owner.techPoints += 1;

	}

}
