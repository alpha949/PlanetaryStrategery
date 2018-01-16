package com.ue.ps;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Planet extends BaseActor{
	public boolean hasBranched;
	private int size;
	public int capacity;
	private Building[] buildings;
	private WorldType type;
	public boolean isHome = false;
	
	//Display
	public static final float focusZoomAmount = 0.4f;
	public ArrayList<Ship> orbitingShips = new ArrayList<Ship>();
	
	//Storage info
	public int resource;
	public int people;
	

	public static enum WorldType {
		asteroid, gas, rock, early, gold;
		public int cap;
		public int colonies;
		
		static {
			asteroid.cap = 1;
			asteroid.colonies = 1;
			gas.cap = 4;
			gas.colonies = 0;
			rock.cap = 2;
			rock.colonies = 1;
			early.cap = 5;
			early.colonies = 2;
			gold.cap = 6;
			gold.cap = 3;
		}
	}
		
	private int builtBuildings = 0;
	
	//planet where you can say what it is
	public Planet(WorldType T, int size) {
		super("");
		this.type = T;
		this.size = size;
		this.Finish();
	}
	
	//planet without specific given info
	public Planet(){
		super("");
		this.type = WorldType.values()[MathUtils.random(0, 4)];
		this.size = MathUtils.random(1, 3);
		this.Finish();
	}
	
	//Does calculations that will be done on every planet, no matter what.
	public void Finish(){
		this.capacity = size * 4;
		this.buildings = new Building[capacity];
		this.genTexture("assets/planet" + size + ".png"); //TODO add type to path
		//this.setRotation(MathUtils.random(0, 360)); //just need to take the rotation displacement into account and move the image core
	}
	
	public void update(){
		
	}
	
	@Override
	public void act(float dt){
		super.act(dt);
		for (Ship s : orbitingShips) {
			s.angle -= 0.1f;
			int orbitDist = 25 + 16;
			s.setRotation(s.angle);
			s.setCenter(this.getWidth()/2, this.getHeight()/2);
			Vector2 pos = Utils.polarToRect((int) (this.getWidth()/2 + s.getWidth()/2) + orbitDist,  s.angle, new Vector2(this.getWidth()/2-16, this.getHeight()/2-16));
			s.setCenter(pos.x, pos.y);
			s.setRotation(s.angle - 90);
		}
		
		
	}
	
	
	public void addBuilding(Building b){
		if (builtBuildings < capacity){
			this.addActor(b);
			buildings[builtBuildings] = b;
			
			int angle = 360/capacity * builtBuildings;
			b.setRotation(angle);
			b.setCenter(this.getWidth()/2, this.getHeight()/2);
			Vector2 pos = Utils.polarToRect((int) (this.getWidth()/2 + b.getWidth()/2),  360/capacity * builtBuildings, new Vector2(this.getWidth()/2-16, this.getHeight()/2-16));
			b.setCenter(pos.x, pos.y);
			b.setRotation(angle - 90);
			
			builtBuildings += 1;
		}
	}
	
	

}
