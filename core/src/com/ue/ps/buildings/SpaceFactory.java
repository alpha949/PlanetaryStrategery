package com.ue.ps.buildings;

import com.ue.ps.Planet;
import com.ue.ps.ui.Images;

public class SpaceFactory extends Building{

	public SpaceFactory() {
		super(Images.Deincrement);
		this.health = 50;
		this.resourceCost = 5;
		this.isSpace = true;
		this.id = 's';
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(Planet p) {
		// TODO Auto-generated method stub
		
	}

}
