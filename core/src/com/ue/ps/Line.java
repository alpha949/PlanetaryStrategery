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
		int numLineSegments = (int) Utils.distanceTo(p1.center.x, p1.center.y, p2.center.x, p2.center.y) / 32;
		lineSegments = new BaseActor[numLineSegments];
		for (int i = 0; i < lineSegments.length; i++) {
			BaseActor b = new BaseActor(lineTexture);
			b.setColor(f.color);
			b.setCenter(p1.center.x, p1.center.y);
			lineSegments[i] = b;
		}

	}

	

	public static void genLine(Line l, Stage m) {
		
		for (int i = 0; i < l.lineSegments.length; i++) {
			m.addActor(l.lineSegments[i]);
			//confirmed it is a problem with the point at formula,
			//may be something to do with local/stage/screen coordinate conversion
			
			float angle = (float) l.lineSegments[i].pointAt(l.planet2.center.x, l.planet2.center.y, 0, true);
			
			Vector2 pos = Utils.polarToRect((i * 32), angle, l.planet1.center);
			l.lineSegments[i].setCenter(pos.x, pos.y);
			
		}
	}

}
