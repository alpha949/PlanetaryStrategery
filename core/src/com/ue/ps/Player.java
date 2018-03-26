package com.ue.ps;

public class Player {

	public int techPoints;
	public int resource;
	public int people;
	public int maxTravelDist = 100 * 8 * 3;
	public Faction faction;

	
	private String userName;

	public Planet homePlanet;

	public Player(String userName, Faction f) {
		this.userName = userName;
		this.resource = 0;
		this.techPoints = 0;
		this.people = 0;
		this.faction = f;
		
	}
	
	
	
	public String getUser() {
		return this.userName;
				
	}
	
	
	public PlayerData toPlayerData(){
		return new PlayerData(this.userName, this.faction.abv);
	}
	public static Player fromPlayerData(PlayerData pd){
		return new Player(pd.username, Faction.getFactionFromAbv(pd.factionAbv));
	}

}
