package com.ue.ps;

public class SidePanel extends BaseActor{
	
	private Planet planet = new Planet();

	public SidePanel() {
		super("assets/sidePanel.png");
	
	}
	
	
	
	
	public void setPlanet(Planet p) {
		this.planet.remove();
		this.addActor(p);
		p.setSize(p.getXbyY().x/5, p.getXbyY().y/5);
		p.setPosition(1, PS.viewHeight - 64 * 2 - 32 + 5);
		this.planet = p;
		
	}
	
	

	
}
