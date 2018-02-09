package com.ue.ps;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class ShipWarpTrail extends BaseActor{

	private static Texture warpTrailTexture = Utils.getImg("shipWarpTrail");
	private int deathTime = 60;
	
	public ShipWarpTrail() {
		super(warpTrailTexture);
	}
	
	
	public void act(float dt) {
		super.act(dt);
		deathTime -= 1;
		if (deathTime < 0) {
			this.remove();
			
		}
	}
	

}
