package com.ue.ps;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.ue.ps.ships.ShipType;
import com.ue.ps.ui.Images;

public class Faction {

	public String name;
	public Color color;
	public String abv;

	private Texture droneImg;
	private Texture scoutImg;
	private Texture transportImg;
	private Texture cruiserImg;
	private Texture dreadImg;
	private Texture flagshipImg;
	private Texture symbolImg;

	private int[][] shipModifiers = new int[ShipType.values().length][3];

	public Faction(String name, String abv, Color color) {
		this.name = name;
		this.color = color;
		this.abv = abv;
		this.droneImg = Images.getImg("ships/" + abv + "Drone");
		this.scoutImg = Images.getImg("ships/" + abv + "Scout");
		this.transportImg = Images.getImg("ships/" + abv + "Transport");
		this.cruiserImg = Images.getImg("ships/" + abv + "Cruiser");
		this.dreadImg = Images.getImg("ships/" + abv + "Dread");
		this.flagshipImg = Images.getImg("ships/" + abv + "Flagship");
		this.symbolImg = Images.getImg(abv);
	}

	public Texture getShipTypeTexture(ShipType s) {
		switch (s) {
			case drone:
				return this.droneImg;
			case scout:
				return this.scoutImg;
			case transport:
				return this.transportImg;
			case cruiser:
				return this.cruiserImg;
			case dread:
				return this.dreadImg;
			case flagship:
				return this.flagshipImg;
			default:
				return this.droneImg;
		}

	}

	public Texture getSymbolTexture() {
		return this.symbolImg;
	}

	/**
	 * Sets the ship modifiers of the faction
	 * 
	 * @param mods a list of integers, first is health mod, then damage mod,
	 *            then cost mod
	 */
	private void setShipModifiers(ShipType ship, int... mods) {
		for (int i = 0; i < mods.length; i++) {
			shipModifiers[ship.ordinal()][i] = mods[i];
		}
	}

	public int getMod(ShipType ship, int modNum) {
		return this.shipModifiers[ship.ordinal()][modNum];
	}

	public static Faction Braecious = new Faction("Braecious", "brc", Color.RED);
	public static Faction Reach = new Faction("The Reach", "trc", Color.BLUE);
	public static Faction Lelouk = new Faction("Lelouk", "lok", Color.YELLOW);
	public static Faction Atch = new Faction("Atch", "agh", Color.WHITE);
	public static Faction Vilioth = new Faction("Vilioth", "vlt", Color.PURPLE);
	public static Faction Efferent = new Faction("Efferent", "efr", Color.ORANGE);
	public static Faction Xin = new Faction("Xin", "xin", Color.GREEN);
	public static Faction[] allFactions = { Braecious, Reach, Lelouk, Atch, Vilioth, Efferent, Xin };
	static {
		Braecious.setShipModifiers(ShipType.cruiser, -5, 4, 0);
		Reach.setShipModifiers(ShipType.scout, 0, 1, -5);
		Lelouk.setShipModifiers(ShipType.flagship, 0, 1, 0);
	}

	public static Faction getFactionFromAbv(String abv) {
		for (Faction f : allFactions) {
			if (f.abv.equals(abv)) {
				return f;
			}
		}
		return null;
	}

}
