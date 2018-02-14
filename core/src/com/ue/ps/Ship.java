package com.ue.ps;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
public class Ship extends BaseActor {

	public int health;
	public int maxhp;

	public int people;
	public int peoplecap;


	public ShipType type;

	public int buildProgress;


	public float angle;
	
	public Planet location;
	
	public String id;
	private String ownerName;
	
	private static int shipCount;

	private static ArrayList<BaseActor> pointerBody = new ArrayList<BaseActor>();
	
	//private static ParticleSpawner shipWarpTrail = new ParticleSpawner("assets/particles/shipWarp");

	public Ship(Faction faction, ShipType type) {
		super();
		this.setTexture(faction.getShipTypeTexture(type));

	}

	
	public static void sendShipsTo(Ship[] ships, Planet p, Stage s, Player pla) {
		//if (this.location.distanceTo(p.center.x, p.center.y) < pla.maxTravelDist) {
			
			//someone could replace this with a fancier particle effect
			int numLineSegments = (int) Utils.distanceTo(ships[0].location.center.x,ships[0].location.center.y, p.center.x, p.center.y);
			for (int i = 0; i < numLineSegments; i++){
				ShipWarpTrail warp =new ShipWarpTrail();
				s.addActor(warp);
				warp.setColor(pla.faction.color);
				double angle = Utils.pointAt(ships[0].location.center.x,ships[0].location.center.y, p.center.x, p.center.y);
				
				warp.setRotation(90);
		
				Vector2 poss = Utils.polarToRect((i), angle, ships[0].location.center);
				warp.setCenter(poss.x, poss.y);
				warp.setZIndex(0);
				
			
				//warp.addAction(Actions.scaleBy(-16, -16, 16));
				
			    warp.addAction(Actions.fadeOut(0));
				warp.addAction(Actions.fadeIn((float) (0.0001*i)));
				warp.addAction(Actions.fadeOut((float) ((float) (0.0001*i) + 0.0001)));
				
				
				
			}
			for (Ship ship : ships){
				ship.location.removeActor(ship);
				ship.location.orbitingShips.remove(ship);
				p.orbitingShips.add(ship);
				p.addActor(ship);
		
				int orbitDist = 25 + 16;
		
				ship.setCenter(p.getWidth() / 2, p.getHeight() / 2);
				Vector2 pos = Utils.polarToRect((int) (p.getWidth() / 2 + ship.getWidth() / 2) + orbitDist, ship.angle,
						new Vector2(p.getWidth() / 2 - 16, p.getHeight() / 2 - 16));
				ship.setCenter(pos.x, pos.y);
				ship.setRotation(ship.angle - 90);
				ship.location = p;
			
				
			}
			
			
		
			
			
			
			
			
			
		//}
	
	

	
	
	
	}
	

	public static void spawnShip(Player pla, Planet p, ShipType s, int angle) {
		Ship newShip = new Ship(pla.faction, s);
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
		newShip.id = genId(pla);
		newShip.ownerName = pla.userName;
		shipCount += 1;

	}
	
	private static String genId(Player owner){
		String id = owner.userName + shipCount;
		
		return id;
		
	}
	
	public String getOwnerName(){
		return this.ownerName;
	}
}
