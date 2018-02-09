package com.ue.ps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
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
	
	private static Texture warpTrailTexture = Utils.getImg("shipWarpTrail");

	public Ship(Faction faction, ShipType type) {
		super();
		this.setTexture(faction.getShipTypeTexture(type));

	}

	public void update() {
		if (destination != null) {
			
		}
	}

	public static void sendShipsTo(Ship[] ships, Planet p, Stage s, Player pla) {
		//if (this.location.distanceTo(p.center.x, p.center.y) < pla.maxTravelDist) {
			for (Ship ship : ships){
				ship.location.removeActor(ship);
				ship.location.orbitingShips.remove(s);
				p.orbitingShips.add(ship);
				p.addActor(ship);
				ship.angle = ship.angle;
				int orbitDist = 25 + 16;
		
				ship.setCenter(p.getWidth() / 2, p.getHeight() / 2);
				Vector2 pos = Utils.polarToRect((int) (p.getWidth() / 2 + s.getWidth() / 2) + orbitDist, ship.angle,
						new Vector2(p.getWidth() / 2 - 16, p.getHeight() / 2 - 16));
				ship.setCenter(pos.x, pos.y);
				ship.setRotation(ship.angle - 90);
				ship.location = p;
			
				
			}
			int numLineSegments = (int) Utils.distanceTo(ships[0].location.center.x,ships[0].location.center.y, p.center.x, p.center.y) / 16;
			for (int i = 0; i < numLineSegments; i++){
				BaseActor warp = new BaseActor(warpTrailTexture);
				s.addActor(warp);
				warp.setColor(pla.faction.color);
				double angle = Utils.pointAt(ships[0].location.center.x,ships[0].location.center.y, p.center.x, p.center.y);
				
				warp.setRotation(90);
		
				Vector2 poss = Utils.polarToRect((i * 16), angle, ships[0].location.center);
				warp.setCenter(poss.x, poss.y);
				//warp.setZIndex(0);
				
			
				warp.addAction(Actions.scaleBy(-16, -16, 16));
				warp.addAction(Actions.fadeOut(16));
				
			}
			
			
		//}
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
