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
	
	public BuildingContainer() {
		super(texture);
		buildingImg.setPosition(1, 1);
		this.addActor(buildingImg);
	}
	
	public void setBuilding(Building b) {
		building = b;
		if (b != null) {

			buildingImg.setTexture(b.getTexture());
		} else {
			buildingImg.setTexture(Utils.emptyTexture);
		}
		
		
	}
	
	public Building getBuilding() {
		return building;
	}
	
	public void update(Vector2 mousePos) {
		if (this.getBoundingRectangle().contains(mousePos) && Gdx.input.justTouched()) {
			this.isSelected = true;
			this.setColor(Color.LIGHT_GRAY);
		} else if (Gdx.input.justTouched()) {
			this.isSelected = false;
			this.setColor(Color.WHITE);
		}
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
}
