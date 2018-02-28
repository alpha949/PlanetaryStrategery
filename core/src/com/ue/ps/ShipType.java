package com.ue.ps;

public enum ShipType {
	drone, scout, colonizer, transport, cruiser, dread, flagship;
	
	private char id;
	
	private int[] baseStats = new int[3];
	
	public char getId() {
		return this.id;
	}
	

	
	
	public int getStat(int stat) {
		return this.baseStats[stat];
	}
	
	private void setStats( int...stats) {
		for (int i = 0; i < stats.length; i++) {
			this.baseStats[i] = stats[i];
		}
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
		drone.setStats(5, 1, 25);
	
		scout.id = 's';
		scout.setStats(20, 3, 50);
		
		colonizer.id = 'c';
		colonizer.setStats(30, 0, 60);
		
		transport.id = 't';
		transport.setStats(25, 0, 40);
		
		cruiser.id = 'c';
		cruiser.setStats(40, 5, 75);
		
		dread.id = 'D';
		dread.setStats(80, 10, 125);
		
		flagship.id = 'f';
		flagship.setStats(160, 20, 200);
		
	}
	
}
