package com.ue.ps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class BuildingContainer extends BaseActor{
	private static Texture texture = Utils.getImg("buildingContainer");
	private Building building;
	private BaseActor buildingImg = new BaseActor();
	private boolean isSelected;
	private HoverPanel hoverPanel;
	
	public Planet planet;
	
	public BuildingContainer() {
		super(texture);
		buildingImg.setPosition(1, 1);
		this.addActor(buildingImg);
	}
	
	public void setBuilding(Building b) {
		building = b;
		if (b != null) {

			buildingImg.setTexture(b.getTexture());
			if (b instanceof Factory) {
				hoverPanel = new HoverPanel(HoverPanel.factoryInfo, "Factory", Integer.toString(b.health), "N/A", "N/A");
			} else {
				hoverPanel = new HoverPanel(HoverPanel.standardInfo, "building",  Integer.toString(b.health));
			}
			this.hoverPanel.setPosition(this.getWidth(), this.getHeight()/2);
			this.addActor(this.hoverPanel);
			hoverPanel.setZIndex(1000);
		} else {
			buildingImg.setTexture(Utils.emptyTexture);
		}
		
		
		
	}
	
	public void setFactoryShip(ShipType s) {
		if (this.building != null && this.building instanceof Factory) {
			((Factory) this.building).buildingShip = s;
			
		}
	}
	
	public Building getBuilding() {
		return building;
	}
	
	public void update(Vector2 mousePos) {
		if (this.getBoundingRectangle().contains(mousePos)) {
			//this should only run if there's a drone in orbit
			if (hoverPanel != null) {
				this.hoverPanel.setVisible(true);
			}
			
			
			if (Gdx.input.justTouched()) {
				if (planet.owner == GameServerClient.clientPlayer) {
					this.isSelected = true;
					this.setColor(Color.LIGHT_GRAY);
				}
			}
			
			
		} else if (Gdx.input.justTouched()) {
			this.isSelected = false;
			this.setColor(Color.WHITE);
		} else {
			if (hoverPanel != null) {
				this.hoverPanel.setVisible(false);
			}
		}
		if (this.building != null && this.building instanceof Factory && ((Factory)this.building).buildingShip != null) {
			this.hoverPanel.getInfo().get(2).setText("Ship: " + ((Factory)this.building).buildingShip.name());
			this.hoverPanel.getInfo().get(3).setText("Progress: " + ((Factory)this.building).buildProgress + "/" + ((Factory)this.building).buildingShip.getBuildLimit());
		}
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
}
