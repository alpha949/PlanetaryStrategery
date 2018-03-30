package com.ue.ps.systems;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.ue.ps.Planet;
import com.ue.ps.Player;
import com.ue.ps.World;
import com.ue.ps.buildings.Building;
import com.ue.ps.ships.Ship;
import com.ue.ps.ships.ShipType;

public class Action {

	private enum ActionType {
		sendShips, buildBuilding, buildShip, attackShip, attackBuilding;
	}

	public ActionType type;

	private String[] shipIds;
	private String id;
	private int damage;
	private int locactionId;
	private int destinationId;

	private int planetId;
	private int buildingSlot;
	private char buildingId;

	private char shipTypeId;

	private Action(String[] ids, int loc, int dest) {
		this.type = ActionType.sendShips;
		this.shipIds = ids;
		this.locactionId = loc;
		this.destinationId = dest;
	}

	private Action(int id, int slot, char buildingId) {
		this.type = ActionType.buildBuilding;
		this.planetId = id;
		this.buildingSlot = slot;
		this.buildingId = buildingId;
	}

	private Action(int id, char type) {
		this.type = ActionType.buildShip;
		this.planetId = id;
		this.shipTypeId = type;
	}

	private Action(String id, int damage) {
		this.type = ActionType.attackShip;
		this.id = id;
		this.damage = damage;
	}

	private Action(int id, int slot, int damage) {
		this.type = ActionType.attackBuilding;
		this.planetId = id;
		this.buildingSlot = slot;
		this.damage = damage;
	}

	/**
	 * Tells server to move ships
	 * 
	 * @param ids the ids of the ships to move
	 * @param loc the planet id of the location of the ships
	 * @param dest the plant id of the destination of the ships
	 */
	public static Action sendShips(String[] ids, int loc, int dest) {
		return new Action(ids, loc, dest);
	}

	/**
	 * Tells server to build a building
	 * 
	 * @param id the id of the planet of the building
	 * @param slot the slot of the building on that planet
	 * @param buildingId the type of building to build
	 */
	public static Action buildBuilding(int id, int slot, char buildingId) {
		return new Action(id, slot, buildingId);
	}

	/**
	 * Tells server to build a ship
	 * 
	 * @param id the planet id to spawn this ship
	 * @param type the type of ship to spawn
	 */
	public static Action buildShip(int id, char type) {
		return new Action(id, type);
	}

	/**
	 * Tells server to attack a ship
	 * 
	 * @param id the id of the ship to be attacked
	 * @param damage the damage to deal to the ship
	 */
	public static Action attackShip(String id, int damage) {
		return new Action(id, damage);
	}

	/**
	 * Tells server to attack a building
	 * 
	 * @param id the id of the planet of the building
	 * @param slot the slot of the building on that planet
	 * @param damage the damage to deal to the building
	 */
	public static Action attackBuilding(int id, int slot, int damage) {
		return new Action(id, slot, damage);
	}

	public Action() {

	}

	public static void execute(Action a, Stage m, Player pla) {
		switch (a.type) {
			case sendShips:
				Ship[] ships = new Ship[a.shipIds.length];
				for (int i = 0; i < World.getWorld().size(); i++) {
					for (int l = 0; l < World.getWorld().get(i).orbitingShips.size(); l++) {
						for (int j = 0; j < a.shipIds.length; j++) {
							if (World.getWorld().get(i).orbitingShips.get(l).id.equals(a.shipIds[j])) {
								ships[j] = World.getWorld().get(i).orbitingShips.get(l);
							}
						}
					}
				}
				Ship.sendShipsTo(ships, World.getPlanetById(a.destinationId), m, pla);
				break;

			case buildBuilding:

				World.getPlanetById(a.planetId).addBuilding(Building.getBuildingFromId(a.buildingId), a.buildingSlot);

				break;

			case buildShip:

				// TODO angle gen
				Ship.spawnShip(pla, World.getPlanetById(a.planetId), ShipType.getShipType(a.shipTypeId), 90);

				break;
			case attackShip:

				for (Planet p : World.getWorld()) {
					for (Ship s : p.orbitingShips) {
						if (s.id.equals(a.id)) {
							s.health -= a.damage;
						}
					}
				}

				break;
			case attackBuilding:

				break;

		}

	}

}
