package com.ue.ps.ships;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.ue.ps.BaseActor;
import com.ue.ps.Faction;
import com.ue.ps.Planet;
import com.ue.ps.Player;
import com.ue.ps.Utils;
import com.ue.ps.buildings.Building;
import com.ue.ps.systems.Action;
import com.ue.ps.systems.GameServerClient;

public class Ship extends BaseActor {

	public int health;
	public int maxhp;

	public int people;
	public int peoplecap;

	private int damage;
	private int cost;

	public ShipType type;

	public Planet prevLocation;

	public float angle;

	public Planet location;

	public String id;
	private String ownerName;

	private static int shipCount;

	// private static ParticleSpawner shipWarpTrail = new
	// ParticleSpawner("assets/particles/shipWarp");

	public Ship(Faction faction, ShipType type) {
		super();
		this.setTexture(faction.getShipTypeTexture(type));
		this.type = type;
		this.health = this.type.getStat(0) + faction.getMod(type, 0);
		this.maxhp = health;
		this.damage = this.type.getStat(1) + faction.getMod(type, 1);
		this.cost = this.type.getStat(2) + faction.getMod(type, 2);

	}

	public void attack(ArrayList<Ship> spaceTargets, ArrayList<Building> buildingTargets, Planet orbitingPlanet) {
		// whether this ship will attack a building or a ship
		int targetType;
		// target ship

		// check to see if there are buildings to target
		if (!buildingTargets.isEmpty()) {
			targetType = MathUtils.random(1, 3);
		} else {
			targetType = 3;
		}

		// attack ships
		int damage = (int) MathUtils.random(this.damage/2f, this.damage *1.5f);
		if (targetType > 1) {
			Ship target = spaceTargets.get(MathUtils.random(0, spaceTargets.size() - 1));
			GameServerClient.packet.addAction(Action.attackShip(target.id, damage));
		}
		// attack buildings
		else {
			Building target = buildingTargets.get(MathUtils.random(0, buildingTargets.size() - 1));
			GameServerClient.packet.addAction(Action.attackBuilding(orbitingPlanet.id, orbitingPlanet.getBuildingSlot(target), damage));
		}
	}

	public static void sendShipsTo(Ship[] ships, Planet p, Stage s, Player pla) {
		// if (this.location.distanceTo(p.center.x, p.center.y) <
		// pla.maxTravelDist) {

		// someone could replace this with a fancier particle effect
		if (ships[0] != null) {
			int numLineSegments = (int) Utils.distanceTo(ships[0].location.center.x, ships[0].location.center.y, p.center.x, p.center.y);
			for (int i = 0; i < numLineSegments; i++) {
				ShipWarpTrail warp = new ShipWarpTrail();
				s.addActor(warp);
				warp.setColor(pla.faction.color);
				double angle = Utils.pointAt(ships[0].location.center.x, ships[0].location.center.y, p.center.x, p.center.y);

				warp.setRotation(90);

				Vector2 poss = Utils.polarToRect((i), angle, ships[0].location.center);
				warp.setCenter(poss.x, poss.y);
				warp.setZIndex(0);

				// warp.addAction(Actions.scaleBy(-16, -16, 16));
 
				warp.addAction(Actions.fadeOut(0));
				warp.addAction(Actions.fadeIn((float) (0.0001 * i)));
				warp.addAction(Actions.fadeOut((float) ((float) (0.0001 * i) + 0.0001)));

			}
			for (Ship ship : ships) {

				// colonization
				if (ship.type == ShipType.colonizer && ship.location == p) {
					p.colonizeFrom(ship.prevLocation, pla, s);
					ship.location.embarkShip(ship);

					break;
				}
				ship.prevLocation = ship.location;
				
				ship.location.embarkShip(ship);
				p.dockShip(ship);
		

			}
		}

		// }

	}

	public void act(float dt) {
		super.act(dt);
		if (this.health < 0) {
			this.location.embarkShip(this);
		}
	}

	public static void spawnShip(Player pla, Planet p, ShipType s, int angle) {
		Ship newShip = new Ship(pla.faction, s);
	

		
		
		newShip.id = genId(pla);
		newShip.ownerName = pla.getUser();
		
	
		shipCount += 1;
		p.dockShip(newShip);

	}

	public static void spawnShip(Faction f, Planet p, ShipType s, int angle) {
		Ship newShip = new Ship(f, s);


	
		newShip.ownerName = f.name + "null";
		shipCount += 1;
		p.dockShip(newShip);
	}

	private static String genId(Player owner) {
		String id = owner.getUser() + shipCount;

		return id;

	}

	public String getOwnerName() {
		return this.ownerName;
	}
}
