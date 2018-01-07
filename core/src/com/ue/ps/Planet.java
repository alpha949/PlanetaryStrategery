package com.ue.ps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

public class Planet extends BaseActor{
	public boolean hasBranched;
	private int size = MathUtils.random(1, 3);
	public Planet() {
		super("");
		this.genTexture("assets/planet" + size + ".png");
		
		
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void act(float dt){
		super.act(dt);
		
		
		
	}
	
	

}
