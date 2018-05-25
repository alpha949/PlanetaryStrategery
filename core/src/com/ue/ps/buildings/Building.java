package com.ue.ps.buildings;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.ue.ps.BaseActor;
import com.ue.ps.Planet;
import com.ue.ps.Player;

public abstract class Building extends BaseActor {
	public int health = 10;

	public int resourceCost;
	public static ArrayList<Building> allLandBuildings = new ArrayList<Building>();
	public static ArrayList<Building> allSpaceBuildings = new ArrayList<Building>();
	public static ArrayList<Building> allBuildings = new ArrayList<Building>();

	public char id;
	public Types type;
	public Player owner;
	protected boolean isSpace;
	public float angle;
	
	public enum Types {
		colony, factory, mine, spaceFactory, cannon;
	}

	public Building(Texture t) {
		super(t);
	
		
	}

	public abstract void update(Planet p);


	public static Building getBuildingFromId(char id) {
		for (Building b : allBuildings) {
			if (b.id == id) {
				return b;
			}
		}
		return null;
	}
	public static Building getBuildingFromType(Types id) {
		for (Building b : allBuildings) {
			if (b.type == id) {
				return b;
			}
		}
		return null;
	}

	public void act(float dt) {
		super.act(dt);
		if (this.health < 0) {
			this.remove();
			// TODO remove building from planet
		}
	}
	
	public boolean isSpaceBuilding() {
		return isSpace;
	}
	
	// all buildings must be registered here, if some one can find a way to do
	// this automagically, that would be awsome
	private static void register(Building b) {
		allBuildings.add(b);
		if (b.isSpace) {
			allSpaceBuildings.add(b);
		} else {
			allLandBuildings.add(b);
		}
	}
	//register buildings here
	static {
		register(new Factory());
		register(new Cannon());
		register(new Mine());
		register(new Colony());
		register(new SpaceFactory());
	}

}
