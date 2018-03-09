package com.ue.ps;

import com.badlogic.gdx.graphics.Texture;

public enum PlanetType {

	asteroid, gas, rock, early, goldilocks;

	public int landCap;
	public int spaceCap;
	public int coloniesCap;
	public String name;
	public Texture tex;
	


	static {
		asteroid.landCap = 1;
		asteroid.spaceCap = 2;
		asteroid.coloniesCap = 1;
		asteroid.name = "Asteroid Field";
		asteroid.tex = Images.planetAsteroids;
		gas.landCap = 0;
		gas.spaceCap = 4;
		gas.coloniesCap = 0;
		gas.name = "Gas Planet";
		gas.tex = Images.planetGas;
		rock.landCap = 5;
		rock.spaceCap = 2;
		rock.coloniesCap = 2;
		rock.name = "Rocky Planet";
		rock.tex = Images.planetBarren;
		early.landCap = 2;
		early.spaceCap = 2;
		early.coloniesCap = 1;
		early.name = "Early Planet";
		early.tex = Images.planetEarly;
		goldilocks.landCap = 6;
		goldilocks.spaceCap = 2;
		goldilocks.coloniesCap = 3;
		goldilocks.name = "Goldilocks Planet";
		goldilocks.tex = Images.planet;
	}
}
