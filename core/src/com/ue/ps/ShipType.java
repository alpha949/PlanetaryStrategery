package com.ue.ps;

public enum ShipType {
	drone, scout, transport, cruiser, dread, flagship;
	
	private char id;
	private int buildLimit;
	
	public char getId() {
		return this.id;
	}
	
	public int getBuildLimit() {
		return this.buildLimit;
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
		drone.buildLimit = 40;
		
		scout.id = 's';
		scout.buildLimit = 100;
		
		transport.id = 't';
		transport.buildLimit = 120;
		
		cruiser.id = 'c';
		cruiser.buildLimit = 200;
		
		dread.id = 'D';
		cruiser.buildLimit = 350;
		
		flagship.id = 'f';
		flagship.buildLimit = 500;
	}
	
}
