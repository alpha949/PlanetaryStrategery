package com.ue.ps.ui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.ue.ps.BaseActor;
import com.ue.ps.HoverPanel;
import com.ue.ps.Planet;
import com.ue.ps.ships.Ship;
import com.ue.ps.ships.ShipType;
import com.ue.ps.systems.Action;
import com.ue.ps.systems.GameServerClient;

public class ShipContainer extends BaseActor implements UIElement{
	public static final int resourceInput = 3;
	public int shipProgressOutput;
	public int buildProgress;

	private Texture texture = Images.shipcontainer;
	private Ship ship;
	private ShipType shiptype;
	private BaseActor shipImg = new BaseActor();
	private boolean isSelected;
	private String distinationName;
	private boolean isDestinationSet;
	public boolean isDestinationUnset;
	public Planet planet;

	private HoverPanel hoverPanel;

	public boolean done = false; // if there is a finished, built, ship
	public boolean constructing = false; // if it is displaying the build menu
											// (only true if top) (pre-build)
	public boolean building = false; // if the ship shown is in the prosess of
										// being built

	private Button[] shipBuildBoxes; // the ships you can build

	public ShipContainer(Ship ship) { // a finished ship
		super(Images.shipcontainer);
		this.texture = Images.shipcontainer;
		shipImg.setPosition(1, 1);
		this.addActor(shipImg);
		this.done = true;
		this.constructing = false;
		this.building = false;
		this.setShip(ship);
	}

	public void setShip(Ship s) {
		ship = s;
		if (s != null) {

			shipImg.setTexture(s.getTexture());
			this.hoverPanel = new HoverPanel(HoverPanel.shipInfo, ship.type.name(), Integer.toString(ship.health), "N/A");
			this.hoverPanel.setPosition(this.getWidth(), this.getHeight() / 2);
			this.addActor(this.hoverPanel);
		} else {
			shipImg.setTexture(Images.emptyTexture);
		}

	}

	public ShipContainer() {
		super(Images.shipbuildbox);
		this.building = false;
		this.texture = Images.shipbuildbox;
		this.done = false;
		this.constructing = true;
		this.buildProgress = 0;
		shipBuildBoxes = new Button[ShipType.values().length];

		for (int i = 0; i < ShipType.values().length; i++) {
			shipBuildBoxes[i] = new Button(Images.ShipBuildBox);
			BaseActor shipImg = new BaseActor(GameServerClient.clientPlayer.faction.getShipTypeTexture(ShipType.values()[i]));
			shipImg.setPosition(1, 1);
			shipBuildBoxes[i].addActor(shipImg);
			this.addActor(shipBuildBoxes[i]);
		}
	}

	public Ship getShip() {
		return ship;
	}

	public void buildUpdate() {

		if (this.building) {

			// check to see if it can work on building the ship
			if (this.shiptype != null && this.planet.resource > resourceInput) { // TODO
																					// add
																					// tech
																					// modifiers
																					// of
																					// the
																					// owning
																					// player
				this.planet.resource -= resourceInput;

				this.buildProgress += shipProgressOutput;
				System.out.println("Ship Prog: " + buildProgress + "/" + this.shiptype.getStat(2));
			}
			// check to see if ship is built
			if (this.ship != null && buildProgress >= this.shiptype.getStat(2)) {

				Ship.spawnShip(this.planet.owner, this.planet, this.shiptype, 90);
				GameServerClient.packet.addAction(Action.buildShip(this.planet.id, this.shiptype.getId()));
				this.buildProgress = 0;
				this.shiptype = null;
				this.done = true;
			}
		}
	}

	public void update(Vector2 mousePos) {
		int numNotHovering = 0;
		for (ShipContainer sc : SidePanel.shipContainers) {
			if (!sc.getBoundingRectangle().contains(mousePos)) {
				numNotHovering += 1;
			}
		}
		if (numNotHovering == SidePanel.shipContainers.size() && Gdx.input.justTouched() && this.getColor() != Color.GREEN) {
			this.isSelected = false;
			this.setColor(Color.WHITE);
		}

		if (this.getBoundingRectangle().contains(mousePos)) {

			if (this.isDestinationSet) {
				// TODO show destination of ships here
			}

			if (Gdx.input.justTouched()) {

				if (this.ship.getOwnerName().equals(GameServerClient.clientPlayer.getUser())) {

					if (this.constructing) {
						for (int i = 0; i < this.shipBuildBoxes.length; i++) {
							if (shipBuildBoxes[i].Pressed(mousePos)) {
								// add as action to server?
								System.out.println("Factory has shipType: " + ShipType.values()[i].name());
								this.building = true;
								this.constructing = false;
								this.planet.BuildQueue.add(this);
							}
						}
					}

					if (this.building) {
						// click to remove from queue
						this.building = false;
						this.constructing = true;
					}

					if (this.done) {
						this.isSelected = true;
						if (this.isDestinationSet) {
							unsetDestination();
						}
						this.setColor(Color.LIGHT_GRAY);
					}
				}

			}

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
