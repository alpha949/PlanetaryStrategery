package com.ue.ps;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Ship extends BaseActor{
	
	public String type;
	
	public int health;
	public int maxhp;
	
	public int people;
	public int peoplecap;
	
	
	public Texture texture;
	
	public Ship(Faction faction) {
		super("");
		//Grab base stats from the faction it is from, which will contain these
	}
}
