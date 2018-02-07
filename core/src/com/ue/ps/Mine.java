package com.ue.ps;

public class Mine extends Building {

	public int production;

	public Mine() {
		super("assets/mine.png");
		this.health = 50;
		this.resourceCost = 5;
		this.id = 'm';
		// TODO Auto-generated constructor stub
	}

	public void update(Player p) {
		p.resource += production;
	}

	@Override
	public void update(Planet p) {
		// TODO Auto-generated method stub
		
	}
}
