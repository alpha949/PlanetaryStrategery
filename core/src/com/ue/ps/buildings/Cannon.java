package com.ue.ps.buildings;


import com.ue.ps.Planet;
import com.ue.ps.ui.Images;

public class Cannon extends Building{

	public Cannon() {
		super(Images.STAcannon);
		this.id = 'd';
		this.health = 120;
		this.resourceCost = 8;
		this.type = Building.Types.cannon;
		
	}

	@Override
	public void update(Planet p) {
		
		
	}

}
