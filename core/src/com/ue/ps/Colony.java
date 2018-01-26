package com.ue.ps;

public class Colony extends Building {

	private int production = 1;

	public Colony() {
		super("assets/colony.png");
		this.health = 100;

	


	}

	public void update(Player p, Planet P) {
		P.people += production;
		p.techPoints += 1;
	}

	@Override
	public void update(Planet p) {
		// TODO Auto-generated method stub
		
	}

}
