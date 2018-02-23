package com.ue.ps;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

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

	public Faction(String name, String abv, Color color) {
		this.name = name;
		this.color = color;

		this.droneImg = Utils.getImg("ships/" + abv + "Drone");
		this.scoutImg = Utils.getImg("ships/" + abv + "Scout");
		this.transportImg = Utils.getImg("ships/" + abv + "Transport");
		this.cruiserImg = Utils.getImg("ships/" + abv + "Cruiser");
		this.dreadImg = Utils.getImg("ships/" + abv + "Dread");
		this.flagshipImg = Utils.getImg("ships/" + abv + "Flagship");
		this.symbolImg = Utils.getImg(abv);
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

	public static Faction Braecious = new Faction("Braecious", "brc", Color.RED);
	public static Faction Reach = new Faction("The Reach", "trc", Color.BLUE);
	public static Faction Lelouk = new Faction("Lelouk", "lok", Color.YELLOW);
	public static Faction Atch = new Faction("Atch", "agh", Color.WHITE);
	public static Faction Vilioth = new Faction("Vilioth", "vlt", Color.PURPLE);
	public static Faction Efferent = new Faction("Efferent", "efr", Color.ORANGE);
	public static Faction Xin = new Faction("Xin", "xin", Color.GREEN);

}
