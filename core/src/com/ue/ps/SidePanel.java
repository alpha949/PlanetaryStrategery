package com.ue.ps;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class SidePanel extends BaseActor{
	
	private Planet planet = new Planet();
	private Label planetName = new Label("55", PS.font);

	public SidePanel() {
		super("assets/sidePanel.png");
		planetName.setPosition(98, PS.viewHeight-56);
		planetName.setFontScale(2);
		this.addActor(planetName);
	
	}
	
	
	
	
	public void setPlanet(Planet p) {
		this.removeActor(this.planet);
		this.planet = p.copy();
		this.addActor(planet);
		planet.setSize(planet.getXbyY().x/5, planet.getXbyY().y/5);
		planet.setCenter(32, PS.viewHeight-98);
		
		
		planetName.setText(this.planet.name);
		
		
		
	}
	
	

	
}
