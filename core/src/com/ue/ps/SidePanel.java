package com.ue.ps;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
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
	
	private Button incrementCapButton = new Button(Images.Increment);
	private Button incrementPriorityButton = new Button(Images.Increment);
	private Button deincrementCapButton = new Button(Images.Deincrement);
	private Button deincrementPriorityButton = new Button(Images.Deincrement);
	
	private BaseActor capContainer = new BaseActor("assets/numContainer.png");
	private BaseActor priorityContainer = new BaseActor("assets/numContainer.png");
	
	private BaseActor prioritySymbol = new BaseActor("assets/priority.png");
	private BaseActor capacitySymbol = new BaseActor("assets/capacity.png");
	
	public static BaseActor uiMouseBlot = new BaseActor("assets/stageMouseBlot.png");
	private Vector2 localMousePos = new Vector2();
	
	private Vector2 copiedMousePos = new Vector2();
	
	
	
	
	
	private ArrayList<BuildingContainer> buildingContainers = new ArrayList<BuildingContainer>(); //the blank buildings
	private ArrayList<Label> buildingCost = new ArrayList<Label>();
	
	private ArrayList<ShipContainer> shipContainers = new ArrayList<ShipContainer>();
	
	public static ArrayList<Ship> selectedShips = new ArrayList<Ship>();
	
	
	private Button[] shipBuildBoxes; //the ships you can build
	private int selectedBuildingSlot = -1;
	private boolean buildBoxesShowing; //TODO change to tab showing
	private boolean shipBuildBoxesShowing;
	
	
	private Label planetResource = new Label("", PS.font);
	
	
	public SidePanel() {
		super("assets/sidePanel.png");
		planetName.setPosition(98, this.getHeight() - 47);
		planetName.setFontScale(2);
		this.addActor(planetName);
		
		planetType.setPosition(70, this.getHeight() - 75);
		this.addActor(planetType);
		
		planetResource.setPosition(70 + 21 + 21 + 21 + 21, this.getHeight() - 75 - 16 - 10);
		this.addActor(planetResource);
		
		capacitySymbol.setPosition(70, this.getHeight() - 75 - 16 - 10);
		this.addActor(capacitySymbol);
		
		prioritySymbol.setPosition(70, this.getHeight() - 75 - 16 - 10 - 16 - 10);
		this.addActor(prioritySymbol);
		
		capContainer.setPosition(70 + 21, this.getHeight() - 75 - 16 - 10);
		this.addActor(capContainer);
		planetCap.setPosition(74 + 21,  this.getHeight() - 75 - 16 - 11);
		this.addActor(planetCap);
		incrementCapButton.setPosition(70 + 21 + 21, this.getHeight() - 75 - 16 - 10);
		this.addActor(incrementCapButton);
		deincrementCapButton.setPosition(70 + 21 + 21 + 21, this.getHeight() - 75 - 16 - 10);
		this.addActor(deincrementCapButton);
		
		
		
		
		priorityContainer.setPosition(70 + 21, this.getHeight() - 75 - 16 - 10 - 16 - 10);
		this.addActor(priorityContainer);
		planetPrioirity.setPosition(74 + 21,  this.getHeight() - 75 - 16 - 10 - 16 - 11);
		this.addActor(planetPrioirity);
		incrementPriorityButton.setPosition(70 + 21 + 21, this.getHeight() - 75 - 16 - 10 - 16 - 10);
		this.addActor(incrementPriorityButton);
		deincrementPriorityButton.setPosition(70 + 21 + 21 + 21, this.getHeight() - 75 - 16 - 10 - 16 - 10);
		this.addActor(deincrementPriorityButton);
		
		this.addActor(this.uiMouseBlot);
		
		
		shipBuildBoxes = new Button[ShipType.values().length];
		for (int i = 0; i < ShipType.values().length; i ++) {
			shipBuildBoxes[i] = new Button(Images.ShipBuildBox);
			shipBuildBoxes[i].setPosition(-100, -100);
			BaseActor shipImg = new BaseActor(GameServerClient.clientPlayer.faction.getShipTypeTexture(ShipType.values()[i]));
			shipImg.setPosition(1, 1);
			shipBuildBoxes[i].addActor(shipImg);
			this.addActor(shipBuildBoxes[i]);
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
		dispPlanet.setCenter(32, this.getHeight() - 98);
		
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
		
		//setup building boxes
		for (int i = 0; i < this.planet.buildings.length; i++) {
			BuildingContainer bc = new BuildingContainer(i, 10, PS.viewHeight - 150 - i * 50);
			bc.planet = p;
			this.buildingContainers.add(bc);
			this.addActor(bc);
			
			if (this.planet.buildings[i] != null) {
				bc.setBuilding(this.planet.buildings[i]);
			} else {
				bc.setBuilding(null);
			}

		}
		hideShipBuildBoxes();

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
		
		this.planetResource.setText("Resource: " + this.planet.resource); //TODO only update when resource updates
		
		//update containers
		for (BuildingContainer bc : this.buildingContainers) {
			bc.update(uiMouseBlot.center);
		}
		
		
		
		
		//check for clicking on increment/deincrement priority/capacity and increment/deincrement them
		if (Gdx.input.justTouched()){
			Rectangle mouse = uiMouseBlot.getBoundingRectangle(); //make this global and the buttons will be easier
			
			if (this.incrementCapButton.Pressed(mouse)) {
				this.planet.resourceCapacity += 1;
			} else if (this.incrementPriorityButton.Pressed(mouse)) {
				this.planet.priority += 1;
			} else if (this.deincrementCapButton.Pressed(mouse)) {
				this.planet.resourceCapacity -= 1;
			}  else if (this.deincrementPriorityButton.Pressed(mouse)) {
				this.planet.priority -= 1;
			}
		
			if (shipBuildBoxesShowing) {
				for (int i = 0; i < this.shipBuildBoxes.length; i++) {
					if (shipBuildBoxes[i].getBoundingRectangle().contains(uiMouseBlot.center) && Gdx.input.justTouched()) {
						buildingContainers.get(selectedBuildingSlot).setFactoryShip(ShipType.values()[i]);
						System.out.println("Factory has shipType: " + ShipType.values()[i].name() );
						hideShipBuildBoxes();
					}
				}	
			}
			
			//check for clicking on buildBox
			if (buildBoxesShowing) {
				
				
				
			}
			//check for destroying building
			/*if (this.destroyBuildingBox.getBoundingRectangle().contains(uiMouseBlot.center) && Gdx.input.justTouched()) {
				//destroy building
				this.planet.destroyBuilding(selectedBuildingSlot);
				//update building boxes
				buildingContainers.get(selectedBuildingSlot).setBuilding(null);
				hideDestroy();
			}*/
		}
		
		//update selectedships
		
		for (ShipContainer sc : this.shipContainers) {
			if (sc.isSelected()) {
				if  (!selectedShips.contains(sc.getShip())){
					selectedShips.add(sc.getShip());
				}
				
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
		
		for (ShipContainer sc : this.shipContainers) {
			sc.update(uiMouseBlot.center, shipContainers);
		}
		
		
		//update text fields
		this.planetCap.setText(Integer.toString(this.planet.resourceCapacity));
		this.planetPrioirity.setText(Integer.toString(this.planet.priority));
	}
	
	private void showShipBuildBoxes() {
		for (int i = 0; i < shipBuildBoxes.length; i++) {
			shipBuildBoxes[i].setPosition(50, PS.viewHeight - 150 - i * 20);
			
		}
		shipBuildBoxesShowing = true;
	}
	/**
	 * hide the buildBoxes
	 */
	private void hideShipBuildBoxes() {
		for (int i = 0; i < shipBuildBoxes.length; i++) {
			shipBuildBoxes[i].setPosition(-100, -100);
			
		}
		shipBuildBoxesShowing = false;
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
	
	public void unset() {
		//remove old planet
			this.setPlanet(this.planet);

			
	}
}
