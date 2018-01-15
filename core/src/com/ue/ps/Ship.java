package com.ue.ps;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Ship extends BaseActor{
	

	
	public int health;
	public int maxhp;
	
	public int people;
	public int peoplecap;
	
	
	public Texture texture;
	
	public ShipType type;
	
	public int buildProgress;
	
	public Planet destination;
	public float angle;
	
	public Ship(Faction faction, ShipType type) {
		super("");
		this.setTexture(faction.getShipTypeTexture(type));
		
		
		
	}
	
	public void update(){
		if (destination != null){
			
		}
	}
	
	public void setDestination(Planet p, Player pla){
		if (this.distanceTo(p.centerX, p.centerY) < pla.maxTravelDist){
			
		}
	}
	
	public static void spawnShip(Faction f, Planet p, ShipType s){
		Ship newShip = new Ship(f,s);
		p.orbitingShips.add(newShip);
		
		p.addActor(newShip);
		newShip.angle = 90;
		int orbitDist = 25 + 16;
		newShip.setRotation(newShip.angle);
		newShip.setCenter(p.getWidth()/2, p.getHeight()/2);
		Vector2 pos = Utils.polarToRect((int) (p.getWidth()/2 + newShip.getWidth()/2) + orbitDist,  newShip.angle, new Vector2(p.getWidth()/2-16, p.getHeight()/2-16));
		newShip.setCenter(pos.x, pos.y);
		newShip.setRotation(newShip.angle - 90);
		
	}
}
