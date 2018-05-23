package com.ue.ps.buildings;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.ue.ps.BaseActor;
import com.ue.ps.Planet;
import com.ue.ps.Player;

public abstract class Building extends BaseActor {
	public int health = 10;

	public int resourceCost;
	public static ArrayList<Building> allBuildings = new ArrayList<Building>();
	public char id;
	public Player owner;
	protected boolean isSpace;
	public float angle;

	public Building(String path) {
		super(path);

	}
	public Building(Texture t) {
		super(t);

	}

	public abstract void update(Planet p);

	public void update(Player P, Planet p) {
	}

	public static Building getBuildingFromId(char id) {
		for (Building b : allBuildings) {
			if (b.id == id) {
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
	static {
		allBuildings.add(new Factory());
		allBuildings.add(new Colony());
		allBuildings.add(new Mine());
		allBuildings.add(new SpaceFactory());

	}

}
