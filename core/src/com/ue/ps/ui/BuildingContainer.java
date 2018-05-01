package com.ue.ps.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.ue.ps.BaseActor;
import com.ue.ps.HoverPanel;
import com.ue.ps.PS;
import com.ue.ps.Planet;
import com.ue.ps.buildings.Building;
import com.ue.ps.buildings.Factory;
import com.ue.ps.systems.Action;
import com.ue.ps.systems.GameServerClient;

public class BuildingContainer extends Button implements UIElement{
	private Building building;
	private BaseActor buildingImg = new BaseActor();
	private boolean isSelected;
	private HoverPanel hoverPanel;
	private BaseActor destroyBuildingBox = new BaseActor("assets/destoryBuilding.png");
	private Button[] buildBoxes; // the buildings you can build

	public int id;

	public Planet planet;

	public boolean done = false; // if there is a finished, built, building
	public boolean constructing = false; // if you are building in it (have hit
											// the +)
	public boolean destroying = false; // if the destroy button is up

	public BuildingContainer(int id, int x, int y) {
		super(Images.panelAdd, x, y);
		buildingImg.setPosition(1, 1);
		this.addActor(buildingImg);
		this.id = id;
		this.destroyBuildingBox.setPosition(-100, -100);
		this.addActor(destroyBuildingBox);

		buildBoxes = new Button[Building.allBuildings.size()]; // PLZ is this
																// the ammount
																// of buildings
																// you can put
																// on a planet?

		for (int i = 0; i < buildBoxes.length; i++) {
			buildBoxes[i] = new Button(Images.BuildBox, (i * 80), 0);
			buildBoxes[i].setVisible(false);
			BaseActor buildingImg = new BaseActor(Building.allBuildings.get(i).getTexture());
	
			buildingImg.setPosition(1, 1);
			buildBoxes[i].addActor(buildingImg);

			Label l = new Label(Integer.toString(Building.allBuildings.get(i).resourceCost), PS.font);
			l.setPosition(23, 0);
			buildBoxes[i].addActor(l);
			this.addActor(buildBoxes[i]);
		}
	}

	public void setBuilding(Building b) {
		building = b;

		if (b != null) {
			for (int i = 0; i < 3; i++) {
				buildBoxes[i].setVisible(false);
			}
			this.done = true;
			this.setTexture(Images.panelStats);
			this.redo();
			buildingImg.setTexture(b.getTexture());
			if (b instanceof Factory) {
				hoverPanel = new HoverPanel(HoverPanel.factoryInfo, "Factory", Integer.toString(b.health), "N/A", "N/A");
			} else {
				hoverPanel = new HoverPanel(HoverPanel.standardInfo, "building", Integer.toString(b.health));
			}
			this.hoverPanel.setPosition(this.getWidth(), this.getHeight() / 2);
			this.addActor(this.hoverPanel);
			hoverPanel.setZIndex(1000);
		} else {
			buildingImg.setTexture(Images.emptyTexture);
		}
	}

	public Building getBuilding() {
		return building;
	}

	public void update(Vector2 mousePos) {
		if (Gdx.input.justTouched()) {
			if (this.Pressed(mousePos)) {
				Vector2 localMouseCoords = this.parentToLocalCoordinates(mousePos);
				this.isSelected = true;
				this.setColor(Color.LIGHT_GRAY);

				if (hoverPanel != null) {
					this.hoverPanel.setVisible(true);
					if (this.building != null && this.building instanceof Factory && ((Factory) this.building).buildingShip != null) {
						this.hoverPanel.getInfo().get(2).setText("Ship: " + ((Factory) this.building).buildingShip.name());
						this.hoverPanel.getInfo().get(3).setText(
								"Progress: " + ((Factory) this.building).buildProgress + "/" + ((Factory) this.building).buildingShip.getStat(2));
					}
				}

				if (!this.done && !this.constructing) { // if clicked on while a
														// + button
					this.constructing = true;
					this.setSize(240, 40);
					for (int i = 0; i < buildBoxes.length; i++) {
						buildBoxes[i].setVisible(true);
					}
				}

				else if (this.constructing && this.isSelected) {
					for (int i = 0; i < buildBoxes.length; i++) {
						if (buildBoxes[i].Pressed(localMouseCoords)) {
							try {
								Building newBuilding = Building.allBuildings.get(i).getClass().newInstance();
								// add building to planet
								this.planet.addBuilding(newBuilding, this.id);
								// update building boxes
								this.setBuilding(newBuilding);
								GameServerClient.packet.addAction(Action.buildBuilding(planet.id, this.id, newBuilding.id));
								this.constructing = false;

							} catch (InstantiationException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}
						}
					}
				}

			} else {
				this.isSelected = false;
				for (int i = 0; i < 3; i++) {
					buildBoxes[i].setVisible(false);
				}
				this.constructing = false;
				this.redo();
				this.setColor(Color.WHITE);
			}
		} else {
			if (hoverPanel != null && this.hoverPanel.isVisible()) {
				this.hoverPanel.setVisible(false);
			}
		}
	}

	public boolean isSelected() {
		return isSelected;
	}

	/*
	 * Unused but useful
	 * 
	 * 
	 * 
	 * 
	 *
	 */

}
