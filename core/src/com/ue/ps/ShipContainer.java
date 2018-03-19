package com.ue.ps;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class ShipContainer extends BaseActor{
	private static Texture texture = Images.getImg("shipContainer");
	private Ship ship;
	private BaseActor shipImg = new BaseActor();
	private boolean isSelected;
	private String distinationName;
	private boolean isDestinationSet;
	
	private HoverPanel hoverPanel;
	
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
			this.hoverPanel = new HoverPanel(HoverPanel.shipInfo, ship.type.name(), Integer.toString(ship.health), "N/A");
			this.hoverPanel.setPosition(this.getWidth(), this.getHeight()/2);
			this.addActor(this.hoverPanel);
		} else {
			shipImg.setTexture(Images.emptyTexture);
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
			
			this.hoverPanel.setVisible(true);
			
			if (this.isDestinationSet) {
				//TODO show destination of ships here
			}
			
			if (Gdx.input.justTouched() ) {
				
				if ( this.ship.getOwnerName().equals(GameServerClient.clientPlayer.getUser())) {
					this.isSelected = true;
					if (this.isDestinationSet) {
						unsetDestination();
					}
					this.setColor(Color.LIGHT_GRAY);
				}
				
				
				
			}
			
		} else {
			this.hoverPanel.setVisible(false);
		}
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	
	public void setDestinationInfo(Planet destination) {
		if (destination.name != distinationName) {
			this.setColor(Color.GREEN);
			distinationName = destination.name;
			this.isDestinationSet = true;
			System.out.println("Destination set");
			this.hoverPanel.getInfo().get(2).setText("Heading: " + distinationName);
			
		}
		
		
	}
	
	public void unsetDestination() {
		this.isDestinationUnset = true;
		distinationName = "";
		this.isDestinationSet = false;
		this.hoverPanel.getInfo().get(2).setText("Heading: N/A");
		
	}
	
	
}
