package com.ue.ps;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Faction {

	public String name;
	public Color color;
	public String abv;
	
	public Texture droneImg;
	public Texture scoutImg;
	public Texture transportImg;
	public Texture cruiserImg;
	public Texture dreadImg;
	public Texture flagshipImg;
	
	public static Faction Braecious = new Faction("Braecious", "brc", Color.RED);
	public static Faction Reach = new Faction("The Reach", "trc", Color.BLUE);
	public static Faction Lelouk = new Faction("Lelouk", "lok", Color.YELLOW);
	public static Faction Atch = new Faction("Atch", "agh", Color.WHITE);
	public static Faction Vilioth = new Faction("Vilioth", "vlt", Color.PURPLE);
	public static Faction Efferent = new Faction("Efferent", "efr", Color.ORANGE);
	public static Faction Xin = new Faction("Xin", "xin", Color.GREEN);
	
	public Faction(String name, String abv, Color color) {
		this.name = name;
		this.color = color;
		
		this.droneImg = Utils.getImg(abv+"drone");
		this.scoutImg = Utils.getImg(abv+"scout");
		this.transportImg = Utils.getImg(abv+"transport");
		this.cruiserImg = Utils.getImg(abv+"cruiser");
		this.dreadImg = Utils.getImg(abv+"dread");
		this.flagshipImg = Utils.getImg(abv+"flagship");
	}
}
