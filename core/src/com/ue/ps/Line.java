package com.ue.ps;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Line {
	
	public Planet planet1;
	public Planet planet2;
	private BaseActor[] lineSegments;
	private static Texture lineTexture = Utils.getImg("line");
	
	
	public Line(Planet p1, Planet p2, Faction f) {
		planet1 = p1;
		planet2 = p2;
		int numLineSegments = (int) Utils.distanceTo(p1.center.x, p1.center.y, p2.center.x, p2.center.y)/16 + 1;
		lineSegments = new BaseActor[numLineSegments];
		for (int i = 0; i < lineSegments.length; i++) {
			BaseActor b = new BaseActor(lineTexture);
			b.setColor(f.color);
			lineSegments[i] = b;
		}
		
		
	}
	
	
	public void update(Stage m) {
		for (BaseActor b : lineSegments) {
			float angle = (float) b.pointAt(planet2.center.x, planet2.center.y, 0, true);
			Vector2 vel = Utils.convertVel(angle);
			b.moveBy(vel.x * 16, vel.y * 16);
			
			if (planet2.overlaps(b, false)) {
				b.setPosition(planet1.center.x, planet1.center.y);
			}
			
		}
		
		
		
	}
	
	
}
