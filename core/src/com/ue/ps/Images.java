package com.ue.ps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Images {
	
	//Base textures
	public static final Texture na = getImg("missingTex");
	public static final Texture emptyTexture = getImg("empty");
	
	//Button Textures
	public static final Texture Increment = getImg("increment");
	public static final Texture Deincrement = getImg("deincrement");
	
	public static Texture BuildBox = getImg("buildBox");
	public static Texture ShipBuildBox = getImg("shipBuildBox");
	public static Texture shipcontainer = getImg("shipContainer");
	public static Texture shipbuildbox = getImg("shipBuildbox");
	public static Texture TechTreeHolder = getImg("assets/techTreeItemHolder");
	
	//Ingame textures
	public static final Texture planetAsteroids = getImg("planets/asteroids");
	public static final Texture planetGas = getImg("planets/Gas");
	public static final Texture planetBarren = getImg("planets/Barren");
	public static final Texture planetEarly = getImg("planets/Early");
	public static final Texture planet = getImg("planets/planet");
	
	public static Texture panelAdd = getImg("panelAdd");
	public static Texture panelStats = getImg("buildingStats");
	
	public static Texture factionBoxTexure = getImg("factionBox");
	
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
	
	//idk
	public static Texture copy(Texture t) {
		return new Texture(t.getTextureData());
	}
}
