package com.ue.ps;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class Colony extends Building {

	private int production = 1;

	public Colony() {
		super("assets/colony.png");
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
