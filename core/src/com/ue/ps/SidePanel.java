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
	
	private Texture shipBox = Utils.getImg("shipContainer");
	
	private ArrayList<BaseActor> buildingBoxes = new ArrayList<BaseActor>();
	private ArrayList<BaseActor> dispedBuildings = new ArrayList<BaseActor>();
	private ArrayList<BaseActor> buildingHealthbars = new ArrayList<BaseActor>();
	
	private ArrayList<BaseActor> shipBoxes = new ArrayList<BaseActor>();
	private ArrayList<BaseActor> dispedShips = new ArrayList<BaseActor>();
	
	
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
	/**
	 * Sets the side panel to display a specific planet's stats.
	 * @param p the planet to be displayed
	 */

	public void setPlanet(Planet p) {
		//remove old planet
		this.planet = p;
		this.removeActor(this.dispPlanet);
		//get displayable planet copy
		this.dispPlanet = p.copy();
		this.addActor(dispPlanet);
		dispPlanet.setSize(dispPlanet.getXbyY().x / 5, dispPlanet.getXbyY().y / 5);
		dispPlanet.setCenter(32, PS.viewHeight - 98);
		
		//update text fields
		planetName.setText(this.planet.name);
		planetType.setText(this.planet.getPlanetType().name);
		//remove old building boxes
		for (BaseActor bbox : this.buildingBoxes) {
			this.removeActor(bbox);
		}
		
		for (BaseActor sbox : this.shipBoxes) {
			this.removeActor(sbox);
		}
		this.buildingBoxes.clear();
		this.buildingHealthbars.clear();
		this.dispedBuildings.clear();
		
		this.shipBoxes.clear();
		this.dispedShips.clear();
		
		//setup building boxes
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
		
		for (int i = 0; i < this.planet.orbitingShips.size(); i++) {
			BaseActor sbox = new BaseActor(this.shipBox);
			sbox.setPosition(100, PS.viewHeight - 150 - i * 20);
			
			this.shipBoxes.add(sbox);
			this.addActor(sbox);
			if (this.planet.orbitingShips.get(i) != null) {
				//for some strange reason, getting the ship's texture returns null
				//BaseActor shipImg = new BaseActor(this.planet.orbitingShips.get(i).texture);
				//shipImg.setPosition(1, 1);
				//dispedShips.add(shipImg);
				//sbox.addActor(shipImg);
			}
		}

	}

	
	/**
	 * updates the sidepanel
	 * @param uiStage the uiStage
	 */
	public void update(Stage uiStage) {
		//update local mouse position
		copiedMousePos.x = GameplayScreen.mouseBlot.getX();
		copiedMousePos.y =  PS.viewHeight -GameplayScreen.mouseBlot.getY();
		
		localMousePos = this.stageToLocalCoordinates(uiStage.screenToStageCoordinates(copiedMousePos));
		//localMousePos.y = PS.viewHeight - localMousePos.y;
		//What the actual heck with these numbers?!?!?!??
		uiMouseBlot.setPosition(localMousePos.x *1.25f, localMousePos.y * 1.25f - 166);
		//check for clicking on increment/deincrement priority/capacity and increment/deincrement them
		if (this.incrementCapButton.getBoundingRectangle().overlaps(uiMouseBlot.getBoundingRectangle()) && Gdx.input.justTouched()) {
			this.planet.resourceCapacity += 1;
		} else if (this.incrementPriorityButton.getBoundingRectangle().overlaps(uiMouseBlot.getBoundingRectangle()) && Gdx.input.justTouched()) {
			this.planet.priority += 1;
		} else if (this.deincrementCapButton.getBoundingRectangle().overlaps(uiMouseBlot.getBoundingRectangle()) && Gdx.input.justTouched()) {
			this.planet.resourceCapacity -= 1;
		}  else if (this.deincrementPriorityButton.getBoundingRectangle().overlaps(uiMouseBlot.getBoundingRectangle()) && Gdx.input.justTouched()) {
			this.planet.priority -= 1;
		}
		
		//check for clicking on a building box
		for (int i = 0; i < this.buildingBoxes.size(); i++) {
			if (buildingBoxes.get(i).getBoundingRectangle().contains(uiMouseBlot.center) && Gdx.input.justTouched()) {
				//show possible actions
				if (this.dispedBuildings.get(i) == null) {
					showBuildBoxes();
					selectedBuildingSlot = i;
				} else {
					showDestroy();
					selectedBuildingSlot = i;
				}
				//highlight selected building box
				for (int l = 0; l < this.buildingBoxes.size(); l++) {
					buildingBoxes.get(l).setColor(Color.WHITE);
				}
				buildingBoxes.get(i).setColor(Color.LIGHT_GRAY);
				break;
				
				
			}
		}
		//check for clicking on buildBox
		if (buildBoxesShowing) {
			for (int i = 0; i < this.buildBoxes.length; i++) {
				if (buildBoxes[i].getBoundingRectangle().contains(uiMouseBlot.center) && Gdx.input.justTouched()) {
					if (selectedBuildingSlot != -1) {
						try {
							//add building to planet
							this.planet.addBuilding(Building.allBuildings.get(i).getClass().newInstance(), selectedBuildingSlot);
							//update building boxes
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
		//check for destroying building
		if (this.destroyBuildingBox.getBoundingRectangle().contains(uiMouseBlot.center) && Gdx.input.justTouched()) {
			//destroy building
			this.planet.destroyBuilding(selectedBuildingSlot);
			//update building boxes
			dispedBuildings.set(selectedBuildingSlot, null);
			buildingBoxes.get(selectedBuildingSlot).clearChildren();
			buildingBoxes.get(selectedBuildingSlot).setColor(Color.WHITE);
			hideDestroy();
		}
		
		
		//update text fields
		this.planetCap.setText(Integer.toString(this.planet.resourceCapacity));
		this.planetPrioirity.setText(Integer.toString(this.planet.priority));
	}
	/**
	 * shows the buildBoxes
	 */
	
	private void showBuildBoxes() {
		for (int i = 0; i < buildBoxes.length; i++) {
			buildBoxes[i].setPosition(50, PS.viewHeight - 150 - i * 20);
		
		}
		buildBoxesShowing = true;
	}
	/**
	 * hide the buildBoxes
	 */
	private void hideBuildBoxes() {
		for (int i = 0; i < buildBoxes.length; i++) {
			buildBoxes[i].setPosition(-100, -100);
			this.destroyBuildingBox.setPosition(-100, -100);
		}
		buildBoxesShowing = false;
	}
	/**
	 * show destroy building option
	 */
	private void showDestroy() {
		this.destroyBuildingBox.setPosition(50, PS.viewHeight - 150);
	}
	/**
	 * hide destroy building option
	 */
	private void hideDestroy() {
		this.destroyBuildingBox.setPosition(-100, -100);
	}

}
