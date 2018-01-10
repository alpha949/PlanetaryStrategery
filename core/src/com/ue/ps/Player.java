package com.ue.ps;

public class Player {
	
	
	public int techPoints;
	public int resource;
	public int people;
	public int maxTravelDist = 100 * 8 * 3;
	public Faction faction;
	
	public ResourcePanel resourcePanel;
	
	public Player(Faction f){
		this.resource = 0;
		this.techPoints = 0;
		this.people = 0;
		this.faction = f;
		this.resourcePanel = new ResourcePanel(f, this);
	}
	
	
	
}
