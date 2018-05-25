package com.ue.ps.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Images {

	// Base textures
	public static final Texture na = getImg("missingTex");
	public static final Texture emptyTexture = getImg("empty");

	// Button Textures
	public static final Texture Increment = getImg("increment");
	public static final Texture Deincrement = getImg("deincrement");

	public static Texture BuildBox = getImg("buildBox");
	public static Texture DarkBuildBox = getImg("buildBoxb");
	public static Texture ShipBuildBox = getImg("shipBuildBox");
	public static Texture shipcontainer = getImg("shipContainer");
	public static Texture shipbuildbox = getImg("shipBuildbox");
	public static Texture TechTreeHolder = getImg("techTreeItemHolder");

	// Ingame textures
	public static final Texture planetAsteroids = getImg("planets/asteroids");
	public static final Texture planetGas = getImg("planets/Gas");
	public static final Texture planetBarren = getImg("planets/Barren");
	public static final Texture planetEarly = getImg("planets/Early");
	public static final Texture planet = getImg("planets/planet");
	public static final Texture colony = getImg("colony");
	public static final Texture factory = getImg("factory");
	public static final Texture mine = getImg("mine");
	public static final Texture spaceFactory = getImg("spaceFactory");
	public static final Texture STAcannon = getImg("STAcannon");

	public static Texture panelAdd = getImg("panelAdd");
	public static Texture panelStats = getImg("buildingStats");
	public static Texture shipStats = getImg("shipStats");

	public static Texture factionBoxTexure = getImg("factionBox");
	public static Texture pew = getImg("pew");
	public static Texture planetGlow = getImg("planetGlow");
	public static Texture shipHealthBar = getImg("shipHealth");

	public static Texture getImg(String path) {
		try {
			Texture t = new Texture(Gdx.files.internal("assets/" + path + ".png"));
			return t;

		} catch (Exception GdxRuntimeException) {
			System.out.println("Error: Could not find: " + path + " substituting...");
			Texture t = new Texture(na.getTextureData());
			return t;
		}
	}

	// idk
	public static Texture copy(Texture t) {
		return new Texture(t.getTextureData());
	}
}
