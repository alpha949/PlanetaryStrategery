package com.ue.ps.buildings;

import com.ue.ps.Planet;
import com.ue.ps.ui.Images;

public class SpaceFactory extends Building{

	public SpaceFactory() {
		super(Images.spaceFactory);
		this.id = 's';
		this.health = 50;
		this.resourceCost = 5; //should cost more but build ships cheaper/faster
		
		this.isSpace = true;
		this.type = Building.Types.spaceFactory;
	}

	@Override
	public void update(Planet p) {
		// TODO Auto-generated method stub
		
	}

}
