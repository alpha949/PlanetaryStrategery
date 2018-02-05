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
			
			lineSegments[i] = b;
		}

	}

	

	public static void genLine(Line l, Stage m) {
		//lines are still slightly off, zakiah, it's your job to fix this
		for (int i = 0; i < l.lineSegments.length; i++) {
			m.addActor(l.lineSegments[i]);
			
			l.lineSegments[i].setCenter(l.planet1.center.x, l.planet1.center.y);
			Vector2 globalPos = l.planet1.localToStageCoordinates(l.lineSegments[i].center);
		
			double angle = Utils.pointAt(globalPos.x, globalPos.y, l.planet2.center.x, l.planet2.center.y);
			l.lineSegments[i].setRotation((float) angle);
	
			Vector2 pos = Utils.polarToRect((i * 32), angle, l.planet1.center);
			l.lineSegments[i].setCenter(pos.x, pos.y);
			l.lineSegments[i].setZIndex(0);
			
			
		}
	}
	
	public static void deleteLine(Line l, Stage m) {
		for (int i = 0; i < l.lineSegments.length; i++) {
			m.getRoot().removeActor(l.lineSegments[i]);
			
		}
	}

}
