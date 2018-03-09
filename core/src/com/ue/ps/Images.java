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
	public static Texture TechTreeHolder = getImg("assets/techTreeItemHolder");
	
	//Ingame textures
	public static final Texture planetAsteroids = getImg("planets/asteroids");
	public static final Texture planetGas = getImg("planets/Gas");
	public static final Texture planetBarren = getImg("planets/Barren");
	public static final Texture planetEarly = getImg("planets/Early");
	public static final Texture planet = getImg("planets/planet");
	
	
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
}
