package com.ue.ps;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Ship extends BaseActor{
	

	
	public int health;
	public int maxhp;
	
	public int people;
	public int peoplecap;
	
	
	public Texture texture;
	
	public ShipType type;
	
	public int buildProgress;
	
	public Planet destination;
	
	public Ship(Faction faction, ShipType type) {
		super("");
		
		
		
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
		
	}
}
