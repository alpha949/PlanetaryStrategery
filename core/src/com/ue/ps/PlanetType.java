package com.ue.ps;

public enum PlanetType {

	asteroid, gas, rock, early, goldilocks;

	public int landCap;
	public int spaceCap;
	public int coloniesCap;
	public String name;

	static {
		asteroid.landCap = 1;
		asteroid.spaceCap = 2;
		asteroid.coloniesCap = 1;
		asteroid.name = "Asteroid Field";
		gas.landCap = 0;
		gas.spaceCap = 4;
		gas.coloniesCap = 0;
		gas.name = "Gas Planet";
		rock.landCap = 5;
		rock.spaceCap = 2;
		rock.coloniesCap = 2;
		rock.name = "Rocky Planet";
		early.landCap = 2;
		early.spaceCap = 2;
		early.coloniesCap = 1;
		early.name = "Early Planet";
		goldilocks.landCap = 6;
		goldilocks.spaceCap = 2;
		goldilocks.coloniesCap = 3;
		goldilocks.name = "Goldilocks Planet";
	}
}
