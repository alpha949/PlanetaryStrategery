package com.ue.ps;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class SidePanel extends BaseActor {

	private Planet dispPlanet = new Planet();
	private Planet planet = new Planet();
	private Label planetName = new Label("", PS.font);
	private Label planetType = new Label("", PS.font);
	
	private Label planetCap = new Label("5", PS.font);
	private Label planetPrioirity = new Label("9", PS.font);
	
	private BaseActor incrementCapButton = new BaseActor("assets/increment.png");
	private BaseActor incrementPriorityButton = new BaseActor("assets/increment.png");
	private BaseActor deincrementCapButton = new BaseActor("assets/deincrement.png");
	private BaseActor deincrementPriorityButton = new BaseActor("assets/deincrement.png");
	
	private BaseActor capContainer = new BaseActor("assets/numContainer.png");
	private BaseActor priorityContainer = new BaseActor("assets/numContainer.png");
	
	private BaseActor prioritySymbol = new BaseActor("assets/priority.png");
	private BaseActor capacitySymbol = new BaseActor("assets/capacity.png");
	
	private BaseActor uiMouseBlot = new BaseActor("assets/stageMouseBlot.png");
	private Vector2 localMousePos = new Vector2();
	
	private Vector2 copiedMousePos = new Vector2();
	
	private Texture buildingBox = Utils.getImg("buildingContainer");
	private Texture buildingUiHealthbar = Utils.getImg("buildingUiHealthbar");
	
	private ArrayList<BaseActor> buildingBoxes = new ArrayList<BaseActor>();
	private ArrayList<BaseActor> dispedBuildings = new ArrayList<BaseActor>();
	private ArrayList<BaseActor> buildingHealthbars = new ArrayList<BaseActor>();
	
	
	private BaseActor[] buildBoxes;
	private int selectedBuildingSlot = -1;
	private boolean buildBoxesShowing;
	
	private BaseActor destroyBuildingBox = new BaseActor("assets/destoryBuilding.png");
	
	
	public SidePanel() {
		super("assets/sidePanel.png");
		planetName.setPosition(98, PS.viewHeight - 47);
		planetName.setFontScale(2);
		this.addActor(planetName);
		
		planetType.setPosition(70, PS.viewHeight - 75);
		this.addActor(planetType);
		
		capacitySymbol.setPosition(70, PS.viewHeight - 75 - 16 - 10);
		this.addActor(capacitySymbol);
		
		prioritySymbol.setPosition(70, PS.viewHeight - 75 - 16 - 10 - 16 - 10);
		this.addActor(prioritySymbol);
		
		capContainer.setPosition(70 + 21, PS.viewHeight - 75 - 16 - 10);
		this.addActor(capContainer);
		planetCap.setPosition(74 + 21,  PS.viewHeight - 75 - 16 - 11);
		this.addActor(planetCap);
		incrementCapButton.setPosition(70 + 21 + 21, PS.viewHeight - 75 - 16 - 10);
		this.addActor(incrementCapButton);
		deincrementCapButton.setPosition(70 + 21 + 21 + 21, PS.viewHeight - 75 - 16 - 10);
		this.addActor(deincrementCapButton);
		
		
		
		
		priorityContainer.setPosition(70 + 21, PS.viewHeight - 75 - 16 - 10 - 16 - 10);
		this.addActor(priorityContainer);
		planetPrioirity.setPosition(74 + 21,  PS.viewHeight - 75 - 16 - 10 - 16 - 11);
		this.addActor(planetPrioirity);
		incrementPriorityButton.setPosition(70 + 21 + 21, PS.viewHeight - 75 - 16 - 10 - 16 - 10);
		this.addActor(incrementPriorityButton);
		deincrementPriorityButton.setPosition(70 + 21 + 21 + 21, PS.viewHeight - 75 - 16 - 10 - 16 - 10);
		this.addActor(deincrementPriorityButton);
		
		this.addActor(this.uiMouseBlot);
		buildBoxes = new BaseActor[Building.allBuildings.size()];
		
		this.destroyBuildingBox.setPosition(-100, -100);
		this.addActor(destroyBuildingBox);
		for (int i = 0; i < buildBoxes.length; i++) {
			buildBoxes[i] = new BaseActor("assets/buildBox.png");
			buildBoxes[i].setPosition(-100, -100);
			BaseActor buildingImg = new BaseActor(Building.allBuildings.get(i).texture);
			buildingImg.setPosition(1, 1);
			buildBoxes[i].addActor(buildingImg);
			this.addActor(buildBoxes[i]);
		}
	}

	public void setPlanet(Planet p) {
		this.planet = p;
		this.removeActor(this.dispPlanet);
		this.dispPlanet = p.copy();
		this.addActor(dispPlanet);
		dispPlanet.setSize(dispPlanet.getXbyY().x / 5, dispPlanet.getXbyY().y / 5);
		dispPlanet.setCenter(32, PS.viewHeight - 98);

		planetName.setText(this.planet.name);
		
		planetType.setText(this.planet.getPlanetType().name);
		
		for (BaseActor bbox : this.buildingBoxes) {
			this.removeActor(bbox);
		}
		this.buildingBoxes.clear();
		this.buildingHealthbars.clear();
		this.dispedBuildings.clear();
		
		
		for (int i = 0; i < this.planet.buildings.length; i++) {
			BaseActor bbox = new BaseActor(this.buildingBox);
			bbox.setPosition(10, PS.viewHeight - 150 - i * 20);
		
			this.buildingBoxes.add(bbox);
			this.addActor(bbox);
			
			if (this.planet.buildings[i] != null) {
				BaseActor buildingImg = new BaseActor(this.planet.buildings[i].texture);
				buildingImg.setPosition(1, 1);
				dispedBuildings.add(buildingImg);
				bbox.addActor(buildingImg);
			} else {
				BaseActor buildingImg = null;
				dispedBuildings.add(buildingImg);
			
				
			}
			
			
			
		}

	}

	
	
	public void update(Stage uiStage) {
		
		copiedMousePos.x = GameplayScreen.mouseBlot.getX();
		copiedMousePos.y =  PS.viewHeight -GameplayScreen.mouseBlot.getY();
		
		localMousePos = this.stageToLocalCoordinates(uiStage.screenToStageCoordinates(copiedMousePos));
		//localMousePos.y = PS.viewHeight - localMousePos.y;
		//What the actual heck with these numbers?!?!?!??
		uiMouseBlot.setPosition(localMousePos.x *1.25f, localMousePos.y * 1.25f - 166);
		if (this.incrementCapButton.getBoundingRectangle().overlaps(uiMouseBlot.getBoundingRectangle()) && Gdx.input.justTouched()) {
			this.planet.resourceCapacity += 1;
		} else if (this.incrementPriorityButton.getBoundingRectangle().overlaps(uiMouseBlot.getBoundingRectangle()) && Gdx.input.justTouched()) {
			this.planet.priority += 1;
		} else if (this.deincrementCapButton.getBoundingRectangle().overlaps(uiMouseBlot.getBoundingRectangle()) && Gdx.input.justTouched()) {
			this.planet.resourceCapacity -= 1;
		}  else if (this.deincrementPriorityButton.getBoundingRectangle().overlaps(uiMouseBlot.getBoundingRectangle()) && Gdx.input.justTouched()) {
			this.planet.priority -= 1;
		}
		
		
		for (int i = 0; i < this.buildingBoxes.size(); i++) {
			if (buildingBoxes.get(i).getBoundingRectangle().contains(uiMouseBlot.center) && Gdx.input.justTouched()) {
			
				if (this.dispedBuildings.get(i) == null) {
					showBuildBoxes();
					selectedBuildingSlot = i;
				} else {
					showDestroy();
					selectedBuildingSlot = i;
				}
				for (int l = 0; l < this.buildingBoxes.size(); l++) {
					buildingBoxes.get(l).setColor(Color.WHITE);
				}
				buildingBoxes.get(i).setColor(Color.LIGHT_GRAY);
				break;
				
				
			}
		}
		
		if (buildBoxesShowing) {
			for (int i = 0; i < this.buildBoxes.length; i++) {
				if (buildBoxes[i].getBoundingRectangle().contains(uiMouseBlot.center) && Gdx.input.justTouched()) {
					if (selectedBuildingSlot != -1) {
						try {
							this.planet.addBuilding(Building.allBuildings.get(i).getClass().newInstance(), selectedBuildingSlot);
							
							BaseActor buildingImg = new BaseActor(Building.allBuildings.get(i).texture);
							buildingImg.setPosition(1, 1);
							dispedBuildings.set(selectedBuildingSlot, buildingImg);
							buildingBoxes.get(selectedBuildingSlot).addActor(buildingImg);
							buildingBoxes.get(selectedBuildingSlot).setColor(Color.WHITE);
							hideBuildBoxes();
							
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
					
				}
			}
		}
		
		if (this.destroyBuildingBox.getBoundingRectangle().contains(uiMouseBlot.center) && Gdx.input.justTouched()) {
			this.planet.destroyBuilding(selectedBuildingSlot);
			dispedBuildings.set(selectedBuildingSlot, null);
			buildingBoxes.get(selectedBuildingSlot).clearChildren();
			buildingBoxes.get(selectedBuildingSlot).setColor(Color.WHITE);
			hideDestroy();
		}
		
		
		
		this.planetCap.setText(Integer.toString(this.planet.resourceCapacity));
		this.planetPrioirity.setText(Integer.toString(this.planet.priority));
	}
	
	private void showBuildBoxes() {
		for (int i = 0; i < buildBoxes.length; i++) {
			buildBoxes[i].setPosition(50, PS.viewHeight - 150 - i * 20);
		
		}
		buildBoxesShowing = true;
	}
	
	private void hideBuildBoxes() {
		for (int i = 0; i < buildBoxes.length; i++) {
			buildBoxes[i].setPosition(-100, -100);
			this.destroyBuildingBox.setPosition(-100, -100);
		}
		buildBoxesShowing = false;
	}
	
	private void showDestroy() {
		this.destroyBuildingBox.setPosition(50, PS.viewHeight - 150);
	}
	
	private void hideDestroy() {
		this.destroyBuildingBox.setPosition(-100, -100);
	}

}
