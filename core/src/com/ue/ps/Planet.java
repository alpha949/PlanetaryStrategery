package com.ue.ps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Planet extends BaseActor{
	public boolean hasBranched;
	private int size = MathUtils.random(1, 3);
	public int capacity = size * 4;
	private Building[] buildings = new Building[capacity];
	private int builtBuildings = 0;
	public Planet() {
		super("");
		this.genTexture("assets/planet" + size + ".png");
		
		
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void act(float dt){
		super.act(dt);
		
		
		
	}
	
	
	public void addBuilding(Building b){
		if (builtBuildings < capacity){
			this.addActor(b);
			buildings[builtBuildings] = b;
			
			int angle = 360/capacity * builtBuildings;
			b.setRotation(angle);
			b.setCenter(this.getWidth()/2, this.getHeight()/2);
			Vector2 pos = Utils.polarToRect((int) (this.getWidth()/2 + b.getWidth()/2),  360/capacity * builtBuildings, new Vector2(this.getWidth()/2-16, this.getHeight()/2-16));
			b.setCenter(pos.x, pos.y);
			b.setRotation(angle - 90);
			
			
			builtBuildings += 1;
			
		}
		
		
	}
	
	

}
