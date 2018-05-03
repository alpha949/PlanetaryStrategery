package com.ue.ps;

import java.util.ArrayList;

import com.ue.ps.ui.TechTreeItem;

public class Player {

	public int techPoints;
	public int resource;
	public int people;
	public int maxTravelDist = 100 * 8 * 3;
	public Faction faction;

	private String userName;

	public Planet homePlanet;
	
	private ArrayList<TechTreeItem> techs = new ArrayList<TechTreeItem>();

	public Player(String userName, Faction f) {
		if (userName == null) {
			System.out.println("error: Null Player name");
		} else {
			System.out.println("Player user name: " + userName);
		}
		this.userName = userName;
		this.resource = 0;
		this.techPoints = 1000000;
		this.people = 0;
		this.faction = f;

	}

	public String getUser() {
		return this.userName;

	}

	public PlayerData toPlayerData() {
		return new PlayerData(this.userName, this.faction.abv);
	}

	public static Player fromPlayerData(PlayerData pd) {
		return new Player(pd.username, Faction.getFactionFromAbv(pd.factionAbv));
	}
	
	public boolean hasTech(TechTreeItem tti) {
		if (this.techs.contains(tti)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void addTech(TechTreeItem tti) {
		this.techs.add(tti);
	}

}
