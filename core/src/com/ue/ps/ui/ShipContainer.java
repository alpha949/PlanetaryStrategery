package com.ue.ps.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.ue.ps.BaseActor;
import com.ue.ps.HoverPanel;
import com.ue.ps.PS;
import com.ue.ps.Planet;
import com.ue.ps.ships.Ship;
import com.ue.ps.ships.ShipType;
import com.ue.ps.systems.Action;
import com.ue.ps.systems.GameServerClient;

public class ShipContainer extends BaseActor implements UIElement{
	public static final int resourceInput = 3;
	public int shipProgressOutput;
	public int buildProgress;

	
	private Ship ship;
	private ShipType shiptype;
	private BaseActor shipImg = new BaseActor();
	private boolean isSelected;
	private String distinationName;
	private boolean isDestinationSet;
	public boolean isDestinationUnset;
	public Planet planet;

	private HoverPanel hoverPanel;
	private Label descriptor;
	private Label nameShow;
	
	//because fuck you not drawing once
	private ShapeRenderer shapeRenderer;
	private float hpratio;
	private int hpSize;
	private Color hpColor;
	private BaseActor healthBar = new BaseActor(Images.shipHealthBar);

	public boolean done = false; // if there is a finished, built, ship
	public boolean constructing = false; // if it is displaying the build menu
											// (only true if top) (pre-build)
	public boolean building = false; // if the ship shown is in the process of
										// being built

	private Button[] shipBuildBoxes; // the ships you can build

	public ShipContainer(Ship ship) { // a finished ship
		super(Images.shipStats);
		shipImg.setPosition(1, 18);
		this.addActor(shipImg);
		this.done = true;
		this.constructing = false;
		this.building = false;
		this.setShip(ship);
	    shapeRenderer = new ShapeRenderer();
		this.makeDisp();
		this.addActor(this.descriptor);
		this.addActor(this.nameShow);
		//colton you can change this
		healthBar.setPosition(1, 3);
		this.addActor(healthBar);
	}

	public ShipContainer() {
		super(Images.shipbuildbox);
		this.building = false;
		
		this.done = false;
		this.constructing = true;
		this.buildProgress = 0;
		shipBuildBoxes = new Button[ShipType.values().length];

		for (int i = 0; i < ShipType.values().length; i++) { //make a big set of "next buildable" ships
			shipBuildBoxes[i] = new Button(Images.ShipBuildBox);
			BaseActor shipImg = new BaseActor(GameServerClient.clientPlayer.faction.getShipTypeTexture(ShipType.values()[i]));
			shipImg.setPosition(1, 1);
			shipBuildBoxes[i].addActor(shipImg);
			this.addActor(shipBuildBoxes[i]);
		}
	    shapeRenderer = new ShapeRenderer();
		this.makeDisp();
		this.addActor(this.descriptor);
		this.addActor(this.nameShow);
		
	}
	
	public void makeDisp(){
		if (this.descriptor == null){this.descriptor = new Label("", PS.font);}
		if (this.nameShow == null){this.nameShow = new Label("", PS.font);}
		
		
		if (this.done){
			//health bar
			if (this.ship.maxhp > 0){
				this.hpratio = (float) this.ship.health / this.ship.maxhp;
				System.out.println(this.hpratio);
				this.hpColor = new Color(Math.max(225- Math.round(225*this.hpratio), 0), Math.max(Math.round(225*this.hpratio), 0), 0, 1);
				this.hpSize = Math.round(this.hpratio*40);
			
			} else {
				this.hpratio = 1;
				this.hpColor = new Color(200, 200, 200, 1);
				this.hpSize = 40;
			}
			
			this.descriptor.setPosition(2, 2);
			this.nameShow.setPosition(46, 18);
		}
		if (this.building){
			//build progress bar
			if (this.shiptype.getStat(2) > 0){
				this.hpratio = (float) this.buildProgress / this.shiptype.getStat(2);
				System.out.println(this.hpratio);
				this.hpColor = new Color(128, 128, 128, 1);
				this.hpSize = -Math.round(this.hpratio*40);
			} else {
				this.hpratio = 1;
				this.hpColor = new Color(200, 200, 200, 1);
				this.hpSize = 40;
			}
			
			this.descriptor.setPosition(2, 2);
			this.nameShow.setPosition(46, 18);
		}
	}
	
	public void setShip(Ship s) {
		this.ship = s;
		if (s != null) {
			this.shiptype = s.type;
			shipImg.setTexture(s.getTexture());
			this.nameShow = new Label(s.type.name(), PS.font);
			//this.nameShow.setColor(GameServerClient.getPlayerByUserName(ship.getOwnerName()).faction.color); //no, the color will be on the ships.
			
			this.hoverPanel = new HoverPanel(HoverPanel.shipInfo, ship.type.name(), Integer.toString(ship.health), "N/A");
			this.hoverPanel.setPosition(this.getWidth(), this.getHeight() / 2);
			this.addActor(this.hoverPanel);
			this.done = true;
			
		} else {
			shipImg.setTexture(Images.emptyTexture);
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
		//shapeRenderer.rect(46, 18, this.hpSize, 18);
	
	
		healthBar.setRegion(0,0, this.ship.health, 18);
	
		healthBar.setColor(hpColor);
		
		
	
		
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

				if (GameServerClient.isOwnedBy(GameServerClient.clientPlayer, this.ship)) {

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
