package com.ue.ps.ui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.ue.ps.BaseActor;
import com.ue.ps.PS;
import com.ue.ps.Planet;
import com.ue.ps.ships.Ship;
import com.ue.ps.ships.ShipPointer;

public class SidePanel extends BaseActor {

	private Planet dispPlanet = new Planet();
	private Planet planet = new Planet();
	private Label planetName = new Label("", PS.font);
	private Label planetType = new Label("", PS.font);

	private Tab tabBuildings = new Tab(65, 637);
	private Tab tabShips = new Tab(65+100, 637);
	
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

	private ArrayList<BuildingContainer> buildingContainers = new ArrayList<BuildingContainer>(); // the
																									// blank
																									// buildings
	private ArrayList<Label> buildingCost = new ArrayList<Label>();

	public static ArrayList<ShipContainer> shipContainers = new ArrayList<ShipContainer>();

	public static ArrayList<Ship> selectedShips = new ArrayList<Ship>();

	private boolean buildBoxesShowing; // TODO change to tab showing
	private boolean shipBuildBoxesShowing;
	private int selectedBuildingSlot = -1;

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
		planetCap.setPosition(74 + 21, this.getHeight() - 75 - 16 - 11);
		this.addActor(planetCap);
		incrementCapButton.setPosition(70 + 21 + 21, this.getHeight() - 75 - 16 - 10);
		this.addActor(incrementCapButton);
		deincrementCapButton.setPosition(70 + 21 + 21 + 21, this.getHeight() - 75 - 16 - 10);
		this.addActor(deincrementCapButton);

		priorityContainer.setPosition(70 + 21, this.getHeight() - 75 - 16 - 10 - 16 - 10);
		this.addActor(priorityContainer);
		planetPrioirity.setPosition(74 + 21, this.getHeight() - 75 - 16 - 10 - 16 - 11);
		this.addActor(planetPrioirity);
		incrementPriorityButton.setPosition(70 + 21 + 21, this.getHeight() - 75 - 16 - 10 - 16 - 10);
		this.addActor(incrementPriorityButton);
		deincrementPriorityButton.setPosition(70 + 21 + 21 + 21, this.getHeight() - 75 - 16 - 10 - 16 - 10);
		this.addActor(deincrementPriorityButton);

		tabBuildings = new Tab(65, 637);
		tabShips = new Tab(65+100, 637);
		this.addActor(tabBuildings);
		this.addActor(tabShips);
		
		this.addActor(this.uiMouseBlot);

	}

	/**
	 * Sets the side panel to display a specific planet's stats.
	 * 
	 * @param p the planet to be displayed
	 */

	public void setPlanet(Planet p) {
		// remove old planet
		this.planet = p;
		this.removeActor(this.dispPlanet);
		// get displayable planet copy
		this.dispPlanet = p.copy();
		this.addActor(dispPlanet);
		dispPlanet.setSize(dispPlanet.getXbyY().x / 5, dispPlanet.getXbyY().y / 5);
		dispPlanet.setCenter(32, this.getHeight() - 98);

		// update text fields
		planetName.setText(this.planet.name);

		planetType.setText(this.planet.getPlanetType().name);
		// remove old building boxes
		for (BuildingContainer bbox : this.buildingContainers) {
			this.removeActor(bbox);
		}

		for (BaseActor sbox : this.shipContainers) {
			this.removeActor(sbox);
		}

		this.buildingContainers.clear();
		this.buildingCost.clear();
		shipContainers.clear();
		tabBuildings.reset();
		tabShips.reset();

		//Ship displaying (move)
		int localrand = 0;
		for (int i = 0; i < this.planet.getAlliedShips().size(); i++) {
			ShipContainer sbox = new ShipContainer(this.planet.getAlliedShips().get(i));
			//sbox.setPosition(100, PS.viewHeight - 150 - i * 20);
			sbox.setPosition(100, PS.viewHeight - 150 - i * 20); //TODO change?

			shipContainers.add(sbox);
			tabShips.addActor(sbox);

			for (ShipPointer pointer : this.planet.pointers) { // makes the
																// container
																// show where
																// the ship is
																// going
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
			localrand = i;
		}

		for (ShipContainer s : this.planet.BuildQueue) { // add ships being
															// built
			localrand++;
			s.setPosition(100, Tab.maxHeight - 10 - localrand * 20);
			shipContainers.add(s);
			tabShips.addActor(s);
			tabShips.internalHeight += 20; //add the height of this object (and buffer beneath) to the net height of the tab
		}

		// add final "next build" box
		ShipContainer sbox = new ShipContainer();
		shipContainers.add(sbox);
		this.addActor(sbox);

		//BUILDINGS TAB
		// setup building boxes
		for (int i = 0; i < this.planet.buildings.length; i++) {
			BuildingContainer bc = new BuildingContainer(i, 10, Tab.maxHeight - (i) * 50); //from bottom left
			//BuildingContainer bc = new BuildingContainer(i, 10, -10 - (i+1) * 50); //from top left
			bc.planet = p;
			this.buildingContainers.add(bc);
			tabBuildings.addActor(bc);
			tabBuildings.internalHeight += 50; //add the height of this object (and buffer beneath) to the net height of the tab

			if (this.planet.buildings[i] != null) {
				bc.setBuilding(this.planet.buildings[i]);
			} else {
				bc.setBuilding(null);
			}
		}
	}

	/**
	 * updates the sidepanel
	 * 
	 * @param uiStage the uiStage
	 */
	public void update(Stage uiStage) {
		// update local mouse position
		copiedMousePos.x = GameplayScreen.mouseBlot.getX();
		copiedMousePos.y = PS.viewHeight - GameplayScreen.mouseBlot.getY();

		localMousePos = this.stageToLocalCoordinates(uiStage.screenToStageCoordinates(copiedMousePos));
		
		uiMouseBlot.setPosition(localMousePos.x, localMousePos.y);
		//System.out.println(localMousePos);

		this.planetResource.setText("Resource: " + this.planet.resource); // TODO
																			// only
																			// update
																			// when
																			// resource
																			// updates

		this.tabBuildings.update(uiMouseBlot.center);
		this.tabShips.update(uiMouseBlot.center);
		
		// update containers
		//for (BuildingContainer bc : this.buildingContainers) {
		//	bc.update(uiMouseBlot.center);
		//}

		// check for clicking on increment/deincrement priority/capacity and
		// increment/deincrement them
		if (Gdx.input.justTouched()) {
			Rectangle mouse = uiMouseBlot.getBoundingRectangle(); // make this
																	// global
																	// and the
																	// buttons
																	// will be
																	// easier

			if (this.incrementCapButton.Pressed(mouse)) {
				this.planet.resourceCapacity += 1;
			} else if (this.incrementPriorityButton.Pressed(mouse)) {
				this.planet.priority += 1;
			} else if (this.deincrementCapButton.Pressed(mouse)) {
				this.planet.resourceCapacity -= 1;
			} else if (this.deincrementPriorityButton.Pressed(mouse)) {
				this.planet.priority -= 1;
			}

			// check for destroying building
			/*
			 * if (this.destroyBuildingBox.getBoundingRectangle().contains(
			 * uiMouseBlot.center) && Gdx.input.justTouched()) { //destroy
			 * building this.planet.destroyBuilding(selectedBuildingSlot);
			 * //update building boxes
			 * buildingContainers.get(selectedBuildingSlot).setBuilding(null);
			 * hideDestroy(); }
			 */
		}

		// update selectedships

		for (ShipContainer sc : shipContainers) {
			if (sc.done) {
				if (sc.isSelected()) {
					if (!selectedShips.contains(sc.getShip())) {
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
			sc.update(uiMouseBlot.center);
		}

		// update text fields
		this.planetCap.setText(Integer.toString(this.planet.resourceCapacity));
		this.planetPrioirity.setText(Integer.toString(this.planet.priority));
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
		// remove old planet
		this.setPlanet(this.planet);

	}
}
