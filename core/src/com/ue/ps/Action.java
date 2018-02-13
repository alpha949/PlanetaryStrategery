package com.ue.ps;

public class Action {
	
	private enum ActionType {
		sendShips, buildBuilding, buildShip;
	}
	
	
	public ActionType type;
	
	private String[] shipIds;
	private Planet locaction;
	private Planet destination;
	
	private int planetId;
	private int buildingSlot;
	private Building building;
	
	private ShipType shipType;
	
	
	public Action(String[] ids, Planet loc, Planet dest) {
		this.type = ActionType.sendShips;
		this.shipIds = ids;
		this.locaction = loc;
		this.destination = dest;
	}
	
	public Action(int id, int slot, Building b){
		this.type = ActionType.buildBuilding;
		this.planetId = id;
		this.buildingSlot = slot;
		this.building = b;
	}
	
	public Action(int id, ShipType type){
		this.type = ActionType.buildShip;
		this.planetId = id;
		this.shipType = type;
	}
	
	
	
	
}
