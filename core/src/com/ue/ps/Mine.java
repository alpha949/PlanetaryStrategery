package com.ue.ps;

public class Mine extends Building{

	public int production;
	
	public Mine() {
		super("assets/mine.png");
		// TODO Auto-generated constructor stub
	}
	
	
	public void update(Player p){
		p.resource += production;
	}
}
