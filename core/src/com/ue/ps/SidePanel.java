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
	
	public static BaseActor uiMouseBlot = new BaseActor("assets/stageMouseBlot.png");
	private Vector2 localMousePos = new Vector2();
	
	private Vector2 copiedMousePos = new Vector2();
	
	
	
	
	
	private ArrayList<BuildingContainer> buildingContainers = new ArrayList<BuildingContainer>();
	private ArrayList<Label> buildingCost = new ArrayList<Label>();
	
	private ArrayList<ShipContainer> shipContainers = new ArrayList<ShipContainer>();
	
	public static ArrayList<Ship> selectedShips = new ArrayList<Ship>();
	
	
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
			BaseActor buildingImg = new BaseActor(Building.allBuildings.get(i).getTexture());
			buildingImg.setPosition(1, 1);
			buildBoxes[i].addActor(buildingImg);
			
			Label l = new Label(Integer.toString(Building.allBuildings.get(i).resourceCost), PS.font);
			l.setPosition(23, 0);
			buildBoxes[i].addActor(l);
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
		for (BuildingContainer bbox : this.buildingContainers) {
			this.removeActor(bbox);
		}
		
		for (BaseActor sbox : this.shipContainers) {
			this.removeActor(sbox);
		}
		
	
		this.buildingContainers.clear();
		this.buildingCost.clear();

		
		this.shipContainers.clear();

		
		//setup building boxes
		for (int i = 0; i < this.planet.buildings.length; i++) {
			BuildingContainer bc = new BuildingContainer();
			bc.setPosition(10, PS.viewHeight - 150 - i * 20);
		
			this.buildingContainers.add(bc);
			this.addActor(bc);
			
			if (this.planet.buildings[i] != null) {
				bc.setBuilding(this.planet.buildings[i]);
			} else {
				bc.setBuilding(null);
			
				
			}

		}
		
		for (int i = 0; i < this.planet.orbitingShips.size(); i++) {
			ShipContainer sbox = new ShipContainer();
			sbox.setPosition(100, PS.viewHeight - 150 - i * 20);
	
			this.shipContainers.add(sbox);
			this.addActor(sbox);
			
			
			
			if (this.planet.orbitingShips.get(i) != null) {
				//for some strange reason, getting the ship's texture returns null
				sbox.setShip(this.planet.orbitingShips.get(i));
			}
			
			for (ShipPointer pointer : this.planet.pointers) {
				System.out.println(pointer);
				for (Ship s : pointer.ships) {
					System.out.println(s);
					if (s != null && getShipContainer(s) != null) {
						getShipContainer(s).setDestinationInfo(pointer.destination);
					} else {
						System.out.println("Missing ship container!");
					}
					
				}
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

		uiMouseBlot.setPosition(localMousePos.x , localMousePos.y);
		
		//update containers
		for (BuildingContainer bc : this.buildingContainers) {
			bc.update(uiMouseBlot.center);
		}
		for (ShipContainer sc : this.shipContainers) {
			sc.update(uiMouseBlot.center, shipContainers);
		}
		
		
		
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
		for (int i = 0; i < this.buildingContainers.size(); i++) {
			if (buildingContainers.get(i).isSelected()) {
				//show possible actions
				if (this.buildingContainers.get(i).getBuilding() == null) {
					showBuildBoxes();
					selectedBuildingSlot = i;
				} else {
					showDestroy();
					selectedBuildingSlot = i;
				}
		
				
				
				
			}
		}
		//check for clicking on buildBox
		if (buildBoxesShowing) {
			for (int i = 0; i < this.buildBoxes.length; i++) {
				if (buildBoxes[i].getBoundingRectangle().contains(uiMouseBlot.center) && Gdx.input.justTouched()) {
					if (selectedBuildingSlot != -1) {
						try {
							Building newBuilding = Building.allBuildings.get(i).getClass().newInstance();
							//add building to planet
							this.planet.addBuilding(newBuilding, selectedBuildingSlot);
							//update building boxes
							buildingContainers.get(selectedBuildingSlot).setBuilding(newBuilding);
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
			buildingContainers.get(selectedBuildingSlot).setBuilding(null);
			hideDestroy();
		}
		
		//update selectedships
		for (ShipContainer sc : this.shipContainers) {
			if (sc.isSelected()) {
				selectedShips.add(sc.getShip());
			} else {
				selectedShips.remove(sc.getShip());
			}
			ShipPointer deleteThisPointer = null;
			if (sc.isDestinationUnset) {
				for (ShipPointer sp : this.planet.pointers) {
					sp.ships.remove(sc.getShip());
					
					if (sp.ships.isEmpty()) {
						sp.delete();
						deleteThisPointer = sp;
					}
				}
			}
			this.planet.pointers.remove(deleteThisPointer);
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
	
	private ShipContainer getShipContainer(Ship s) {
		for (ShipContainer sc : this.shipContainers) {
			if (sc.getShip() == s) {
				return sc;
			} 
		}
		return null;
	}
	
	public void onDestinationSet(Planet destination) {
		for (Ship s : selectedShips) {
			getShipContainer(s).setDestinationInfo(destination);
		}
	}

}
