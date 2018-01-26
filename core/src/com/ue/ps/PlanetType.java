package com.ue.ps;

public enum PlanetType {

	asteroid, gas, rock, early, goldilocks;
	public int cap;
	public int colonies;
	public String name;
	
	static {
		asteroid.cap = 1;
		asteroid.colonies = 1;
		asteroid.name = "Asteroid Field";
		gas.cap = 4;
		gas.colonies = 0;
		gas.name = "Gas Planet";
		rock.cap = 4;
		rock.colonies = 1;
		rock.name = "Rocky Planet";
		early.cap = 2;
		early.colonies = 2;
		early.name = "Early Planet";
		goldilocks.cap = 6;
		goldilocks.cap = 3;
		goldilocks.name = "Goldilocks Planet";
	}
}

