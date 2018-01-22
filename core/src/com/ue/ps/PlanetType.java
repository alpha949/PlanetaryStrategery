package com.ue.ps;

public enum PlanetType {

	asteroid, gas, rock, early, goldilocks;
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
		goldilocks.cap = 6;
		goldilocks.cap = 3;
	}
}
