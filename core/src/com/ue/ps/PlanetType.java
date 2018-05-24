package com.ue.ps;

import com.badlogic.gdx.graphics.Texture;
import com.ue.ps.buildings.BuildingType;
import com.ue.ps.ui.Images;

public enum PlanetType {

	asteroid, gas, rock, early, goldilocks;

	public int landCap;
	public BuildingType[] landBuildings;
	public int spaceCap;
	public BuildingType[] spaceBuildings;
	public int coloniesCap;
	public String name;
	public Texture tex;


	static {
		asteroid.landCap = 1;
		asteroid.landBuildings = new BuildingType[]{BuildingType.mine, BuildingType.colony};
		asteroid.spaceCap = 0;
		asteroid.spaceBuildings = new BuildingType[0];
		asteroid.coloniesCap = 1;
		asteroid.name = "Asteroid Field";
		asteroid.tex = Images.planetAsteroids;
	
		gas.landCap = 0;
		gas.landBuildings = new BuildingType[0];
		gas.spaceCap = 4;
		gas.spaceBuildings = new BuildingType[]{BuildingType.spacefactory};
		gas.coloniesCap = 0;
		gas.name = "Gas Planet";
		gas.tex = Images.planetGas;
	
		rock.landCap = 3;
		rock.landBuildings = new BuildingType[]{BuildingType.colony, BuildingType.factory, BuildingType.mine};
		rock.spaceCap = 2;
		rock.spaceBuildings = new BuildingType[]{BuildingType.spacefactory};
		rock.coloniesCap = 1;
		rock.name = "Rocky Planet";
		rock.tex = Images.planetBarren;
	
		early.landCap = 2;
		early.landBuildings = new BuildingType[]{BuildingType.colony, BuildingType.factory, BuildingType.mine};
		early.spaceCap = 2;
		early.spaceBuildings = new BuildingType[]{BuildingType.spacefactory};
		early.coloniesCap = 1;
		early.name = "Early Planet";
		early.tex = Images.planetEarly;
	
		goldilocks.landCap = 4;
		goldilocks.landBuildings = new BuildingType[]{BuildingType.colony, BuildingType.factory, BuildingType.mine};
		goldilocks.spaceCap = 2;
		goldilocks.spaceBuildings = new BuildingType[]{BuildingType.spacefactory};
		goldilocks.coloniesCap = 3;
		goldilocks.name = "Goldilocks Planet";
		goldilocks.tex = Images.planet;
		
	}
}
