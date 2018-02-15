package com.ue.ps;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class ShipPointer {
	
	public ArrayList<BaseActor> pointerBody = new ArrayList<BaseActor>();
	
	private static Texture pointerHeadTexture = Utils.getImg("pointerHead");
	private static Texture pointerBodyTexture = Utils.getImg("pointerBody");
	
	public Planet location;
	public Planet destination;
	private boolean isStatic;
	private boolean hasStaticRendered;
	public ArrayList<Ship> ships = new ArrayList<Ship>();
	
	public ShipPointer(Planet location){
		this.location = location;
	}
	
	
	public void delete(){
		for (BaseActor ba : pointerBody){
			ba.remove();
		}
		pointerBody.clear();
		
	}
	
	public void renderShipPointer(Vector2 mousePos, Stage s) {
		
		
		
		this.delete();
		BaseActor head = new BaseActor(pointerHeadTexture);
		int numLineSegments = (int) Utils.distanceTo(location.center.x,location.center.y,mousePos.x, mousePos.y)/ 64;
	
		for (int i = 0; i < numLineSegments; i++){
			
			BaseActor body = new BaseActor(pointerBodyTexture);
			s.addActor(body);
			double angle = Utils.pointAt(location.center.x,location.center.y, mousePos.x, mousePos.y);
			
			body.setRotation((float) angle);
	
			Vector2 poss = Utils.polarToRect((i * 64), angle, location.center);
			body.setCenter(poss.x, poss.y);
			body.setZIndex(0);
			pointerBody.add(body);
			
			
			if (i == numLineSegments-1){
				body.setTexture(pointerHeadTexture);
				body.setCenter(poss.x, poss.y);
			}
		
			
			
			
		}
	}
	
	public void staticRender(Stage s){
		if (!hasStaticRendered){
			
		
			int numLineSegments = (int) Utils.distanceTo(location.center.x,location.center.y,destination.center.x,destination.center.y)/ 64;
			
			for (int i = 0; i < numLineSegments; i++){
				
				BaseActor body = new BaseActor(pointerBodyTexture);
				s.addActor(body);
				double angle = Utils.pointAt(location.center.x,location.center.y, destination.center.x,destination.center.y);
				
				body.setRotation((float) angle);
		
				Vector2 poss = Utils.polarToRect((i * 64), angle, location.center);
				body.setCenter(poss.x, poss.y);
				body.setZIndex(0);
				pointerBody.add(body);
				
				
				if (i == numLineSegments-1){
					body.setTexture(pointerHeadTexture);
					body.setCenter(poss.x, poss.y);
				}
			
				
				hasStaticRendered = true;
				
			}
		}
	}
	
	public void setDestination(Planet d){
		this.destination = d;
		isStatic = true;
	}
	
	public ShipPointer clone() {
		ShipPointer newSP = new ShipPointer(this.location);
		newSP.destination = this.destination;
		newSP.ships = this.ships;
		newSP.isStatic = this.isStatic;
		newSP.pointerBody = this.pointerBody;
		newSP.hasStaticRendered = this.hasStaticRendered;
		return newSP;
	}
	
	
	
}
