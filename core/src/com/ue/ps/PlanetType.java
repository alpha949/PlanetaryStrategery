package com.ue.ps;

import com.badlogic.gdx.graphics.Texture;
import com.ue.ps.buildings.Building;

import com.ue.ps.ui.Images;

public enum PlanetType {

	asteroid, gas, rock, early, goldilocks;

	public int landCap;
	public Building.Types[] landBuildings = new Building.Types[0];
	public int spaceCap;
	public Building.Types[] spaceBuildings = new Building.Types[0];
	public int coloniesCap;
	public String name;
	public Texture tex;

	
	private void setValidLandBuildings(Building.Types...types) {
		landBuildings = types;
	}
	private void setValidSpaceBuildings(Building.Types...types) {
		spaceBuildings = types;
	}
	static {
		asteroid.landCap = 1;
		asteroid.setValidLandBuildings(Building.Types.mine, Building.Types.colony);
		asteroid.spaceCap = 0;
		asteroid.coloniesCap = 1;
		asteroid.name = "Asteroid Field";
		asteroid.tex = Images.planetAsteroids;
	
		gas.landCap = 0;
		gas.spaceCap = 4;

		gas.setValidSpaceBuildings(Building.Types.spaceFactory);
		gas.coloniesCap = 0;
		gas.name = "Gas Planet";
		gas.tex = Images.planetGas;
	
		rock.landCap = 3;
		rock.setValidLandBuildings(Building.Types.colony, Building.Types.factory, Building.Types.mine);
		rock.spaceCap = 2;
		
		rock.setValidSpaceBuildings(Building.Types.spaceFactory);
		rock.coloniesCap = 1;
		rock.name = "Rocky Planet";
		rock.tex = Images.planetBarren;
	
		early.landCap = 2;
		early.setValidLandBuildings(Building.Types.colony, Building.Types.factory, Building.Types.mine);
		early.spaceCap = 2;
		early.setValidSpaceBuildings(Building.Types.spaceFactory);
		early.coloniesCap = 1;
		early.name = "Early Planet";
		early.tex = Images.planetEarly;
	
		goldilocks.landCap = 4;
		goldilocks.setValidLandBuildings(Building.Types.colony, Building.Types.factory, Building.Types.mine);
		goldilocks.spaceCap = 2;
		goldilocks.setValidSpaceBuildings(Building.Types.spaceFactory);
		goldilocks.coloniesCap = 3;
		goldilocks.name = "Goldilocks Planet";
		goldilocks.tex = Images.planet;
		
	}
}
