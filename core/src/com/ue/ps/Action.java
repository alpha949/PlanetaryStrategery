package com.ue.ps;

import com.badlogic.gdx.scenes.scene2d.Stage;

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
	/**
	 * Build ship action	
	 * @param id the planet id
	 * @param type the shipType
	 */
	public Action(int id, ShipType type){
		this.type = ActionType.buildShip;
		this.planetId = id;
		this.shipType = type;
	}
	
	public static void execute(Action a, Stage m, Player pla){
		switch(a.type){
		case sendShips:
			Ship[] ships = new Ship[a.shipIds.length];
			for (int i = 0; i< World.getWorld().size(); i++){
				for (int l = 0; l < World.getWorld().get(i).orbitingShips.size(); l++){
					for (int j = 0; j < a.shipIds.length; j++){
						if (World.getWorld().get(i).orbitingShips.get(l).id.equals(a.shipIds[j])){
							ships[j] = World.getWorld().get(i).orbitingShips.get(l);
						}
					}
				}
			}
			Ship.sendShipsTo(ships, a.destination, m, pla);
			break;
			
		case buildBuilding:
			
			World.getPlanetById(a.planetId).addBuilding(a.building, a.buildingSlot);
			
			break;
			
		case buildShip:
	
			//TODO angle gen
			Ship.spawnShip(pla, World.getPlanetById(a.planetId), a.shipType, 90);
			
			break;
		
		}
		
		
			
		
		
	}
	
	
	
	
}
