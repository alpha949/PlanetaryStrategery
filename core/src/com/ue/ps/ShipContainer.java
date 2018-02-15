package com.ue.ps;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class ShipContainer extends BaseActor{
	private static Texture texture = Utils.getImg("shipContainer");
	private Ship ship;
	private BaseActor shipImg = new BaseActor();
	private boolean isSelected;
	private String distinationName;
	private boolean isDestinationSet;
	
	public boolean isDestinationUnset;
	public ShipContainer() {
		super(texture);
		shipImg.setPosition(1, 1);
		this.addActor(shipImg);
	}
	
	public void setShip(Ship s) {
		ship = s;
		if (s != null) {

			shipImg.setTexture(s.getTexture());
		} else {
			shipImg.setTexture(Utils.emptyTexture);
		}
		
		
	}
	
	public Ship getShip() {
		return ship;
	}
	
	public void update(Vector2 mousePos, ArrayList<ShipContainer> otherShipContainers) {
		int numNotHovering = 0;
		for (ShipContainer sc : otherShipContainers) {
			if (!sc.getBoundingRectangle().contains(mousePos)) {
				numNotHovering += 1;
			}
		}
		if (numNotHovering == otherShipContainers.size() && Gdx.input.justTouched() && this.getColor() != Color.GREEN ) {
			this.isSelected = false;
			this.setColor(Color.WHITE);
		}
		
		if (this.getBoundingRectangle().contains(mousePos)) {
			
			if (this.isDestinationSet) {
				//TODO show destination of ships here
			}
			
			if (Gdx.input.justTouched()) {
				this.isSelected = true;
				if (this.isDestinationSet) {
					unsetDestination();
				}
				this.setColor(Color.LIGHT_GRAY);
				
			}
			
		}
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	
	public void setDestinationInfo(Planet destination) {
		this.setColor(Color.GREEN);
		distinationName = destination.name;
		this.isDestinationSet = true;
		System.out.println("Destination set");
	}
	
	public void unsetDestination() {
		this.isDestinationUnset = true;
		distinationName = "";
		this.isDestinationSet = false;
		
	}
	
	
}
