package com.ue.ps;

public class Mine extends Building {

	public int production = 5;

	public Mine() {
		super("assets/mine.png");
		this.health = 50;
		this.resourceCost = 5;
		this.id = 'm';
		// TODO Auto-generated constructor stub
	}



	@Override
	public void update(Planet p) {
		p.resource += this.production;
		System.out.println("Planet resource: " + p.resource);
		
	}
}
