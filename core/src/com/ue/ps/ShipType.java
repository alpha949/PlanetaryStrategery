package com.ue.ps;

public enum ShipType {
	drone, scout, transport, cruiser, dread, flagship;
	
	private char id;
	
	public char getId() {
		return this.id;
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
		scout.id = 's';
		transport.id = 't';
		cruiser.id = 'c';
		dread.id = 'D';
		flagship.id = 'f';
	}
	
}
