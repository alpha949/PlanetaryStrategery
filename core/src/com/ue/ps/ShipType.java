package com.ue.ps;

public enum ShipType {
	drone, scout, colonizer, transport, cruiser, dread, flagship;
	
	private char id;
	private int cost;
	
	public char getId() {
		return this.id;
	}
	
	public int getBuildLimit() {
		return this.cost;
	}
	
	public static ShipType getShipType(char id) {
		for (ShipType st : ShipType.values()) {
			if (st.id == id) {
				return st;
				
			}
		}
		return null;
		
	}
	
	static {
		drone.id = 'd';
		drone.cost = 40;
		
		scout.id = 's';
		scout.cost = 100;
		
		colonizer.id = 'c';
		colonizer.cost = 70;
		
		transport.id = 't';
		transport.cost = 120;
		
		cruiser.id = 'c';
		cruiser.cost = 200;
		
		dread.id = 'D';
		cruiser.cost = 350;
		
		flagship.id = 'f';
		flagship.cost = 500;
	}
	
}
