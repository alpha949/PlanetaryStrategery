package com.ue.ps;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Planet extends BaseActor{
	public boolean hasBranched;
	private int size;
	public int capacity;
	public Building[] buildings;
	private PlanetType type;
	public boolean isHomePlanet;
	
	public int resourceCapacity;
	public int priority;
	
	public int id;
	
	//Display
	public static final float focusZoomAmount = 0.4f;
	public ArrayList<Ship> orbitingShips = new ArrayList<Ship>();
	
	//Storage info
	public int resource;
	public int people;
	
	public String name;
	
	public Player owner;
	
	private int rotateDirection = MathUtils.randomSign();
	
		
	private int builtBuildings = 0;
	
	//planet where you can say what it is
	public Planet(PlanetType type, int size) {
		super();
		this.type = type;
		this.size = size;
		this.finish();
	}
	
	//planet without specific given info
	public Planet(){
		super();
		this.type = PlanetType.values()[MathUtils.random(0, 4)];
		this.size = MathUtils.random(1, 3);
		this.finish();
	}
	
	//Does calculations that will be done on every planet, no matter what.
	private void finish(){
		this.capacity = size * 4;
		this.buildings = new Building[capacity];
		this.genTexture("assets/planet" + size + ".png"); //TODO add type to path
		this.setRotation(MathUtils.random(0, 360));
		this.name = Utils.genName();
	}
	
	public void setType(PlanetType p){
		this.type = p;
		
	}
	
	public PlanetType getPlanetType(){
		return this.type;
	}
	
	public void update(){
		
	}
	
	@Override
	public void act(float dt){
		super.act(dt);
		for (Ship s : orbitingShips) {
			s.angle -= 0.5f;
			int orbitDist = 25 + 16;
			s.setRotation(s.angle);
			s.setCenter(this.getWidth()/2, this.getHeight()/2);
			Vector2 pos = Utils.polarToRect((int) (this.getWidth()/2 + s.getWidth()/2) + orbitDist,  s.angle, new Vector2(this.getWidth()/2-16, this.getHeight()/2-16));
			s.setCenter(pos.x, pos.y);
			s.setRotation(s.angle - 90);
		}
		
		this.rotateBy(0.2f * rotateDirection);
		
		
	}
	
	
	public void addBuilding(Building b, int slot){
		if (builtBuildings < capacity && slot < capacity){
			this.addActor(b);
			buildings[slot] = b;
			
			int angle = 360/capacity * slot;
			b.setRotation(angle);
			b.setCenter(this.getWidth()/2, this.getHeight()/2);
			Vector2 pos = Utils.polarToRect((int) (this.getWidth()/2 + b.getWidth()/2),  360/capacity * slot, new Vector2(this.getWidth()/2, this.getHeight()/2));
			b.setCenter(pos.x, pos.y);
			b.setRotation(angle - 90);
			
			builtBuildings += 1;
		}
	}
	
	public void destroyBuilding(int slot) {
		this.removeActor(buildings[slot]);
		buildings[slot] = null;
		builtBuildings -= 1;
	}
	
	public Vector2 getXbyY() {
		
		if (size == 1) {
			return new Vector2(64,64);
		} else if (size == 2) {
			return new Vector2(128,128);
		} else {
			return new Vector2(256,256);
		}
		
		
		
	}
	
	public Planet copy() {
		Planet newPlanet = new Planet(this.type, this.size);
		newPlanet.buildings = this.buildings;
		newPlanet.builtBuildings = this.builtBuildings;
		newPlanet.capacity = this.capacity;
		newPlanet.isHomePlanet = this.isHomePlanet;
		newPlanet.name = this.name;
		newPlanet.setColor(this.getColor());
		return newPlanet;
		
		
	}
	
	
	
	
	
	
	
	
	

}
