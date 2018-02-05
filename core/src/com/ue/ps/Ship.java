package com.ue.ps;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Ship extends BaseActor {

	public int health;
	public int maxhp;

	public int people;
	public int peoplecap;


	public ShipType type;

	public int buildProgress;

	public Planet destination;
	public float angle;
	
	private Planet location;

	public Ship(Faction faction, ShipType type) {
		super("");
		this.setTexture(faction.getShipTypeTexture(type));

	}

	public void update() {
		if (destination != null) {
			
		}
	}

	public void setDestination(Planet p, Player pla, Stage s) {
		if (this.distanceTo(p.center.x, p.center.y) < pla.maxTravelDist) {
			this.location.orbitingShips.remove(this);
			//TODO make this work
			Vector2 globalPos = this.location.localToStageCoordinates(this.center);
			this.setCenter(globalPos.x, globalPos.y);
			s.addActor(this);
			this.pointAt(p.center.x, p.center.y, 0, true);
			
		}
	}

	public static void spawnShip(Faction f, Planet p, ShipType s, int angle) {
		Ship newShip = new Ship(f, s);
		p.orbitingShips.add(newShip);

		p.addActor(newShip);
		newShip.angle = angle;
		int orbitDist = 25 + 16;
		newShip.setRotation(newShip.angle);
		newShip.setCenter(p.getWidth() / 2, p.getHeight() / 2);
		Vector2 pos = Utils.polarToRect((int) (p.getWidth() / 2 + newShip.getWidth() / 2) + orbitDist, newShip.angle,
				new Vector2(p.getWidth() / 2 - 16, p.getHeight() / 2 - 16));
		newShip.setCenter(pos.x, pos.y);
		newShip.setRotation(newShip.angle - 90);
		newShip.location = p;

	}
}
