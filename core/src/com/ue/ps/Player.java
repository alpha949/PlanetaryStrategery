package com.ue.ps;

public class Player {

	public int techPoints;
	public int resource;
	public int people;
	public int maxTravelDist = 100 * 8 * 3;
	public Faction faction;

	
	private User user;

	public Planet homePlanet;

	public Player(User u, Faction f) {
		this.user = u;
		this.resource = 0;
		this.techPoints = 0;
		this.people = 0;
		this.faction = f;
		
	}
	
	public User getUser() {
		return this.user;
				
	}

}
