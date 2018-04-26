package com.ue.ps.ui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.ue.ps.PS;
import com.ue.ps.ships.ShipType;
import com.ue.ps.systems.InputProcess;

public class Tab extends Button {

	public static ArrayList<Tab> tabs = new ArrayList<Tab>();
	
	public Rectangle tabHitbox;
	
	public Texture FullImg;
	public boolean selected = false;
	public float scrollOffset;
	//Tab interior height, change with the screen. Static for now, change if more tab locations.
	public static int maxHeight = PS.viewHeight - 130;
	//net size of all internal parts that take up space.
	public int internalHeight;
	
	public Tab(int tabx, int taby) {
		super(Images.BuildBox, tabx, taby); //temporary image
		
		//Selector hitbox
		this.tabHitbox = new Rectangle(tabx, taby, this.getTexture().getWidth(), this.getTexture().getHeight());
		
		//Fixed size / placement of tab belly
		this.Hitbox = new Rectangle(0, 0, 250, 636);
		//this.setCullingArea(this.Hitbox); // culling broke
		this.internalHeight = 10; //buffer
		
		Tab.tabs.add(this); //Currently only one list of tabs. Make more and an input method to chose to add more.
	}

	public void update(Vector2 mousePos) {
		if (this.selected && Hitbox.contains(mousePos)){
			//Scroll check

			//Update contents
			//for ()
		}
		
		//time to select it
		if (!this.selected && tabHitbox.contains(mousePos) && Gdx.input.justTouched()) {
			System.out.println("Tab Selected");
			for (int t = 0; t < Tab.tabs.size(); t++)
				{Tab.tabs.get(t).selected = false;}
			this.selected = true;
		}
	}
	
	public void draw(Batch batch){
		
		if (this.selected){
			this.drawChildren(batch, 0);
		}
	}
	
	//Will be used to "select a ship, shows up on the side menu" (may change to take in object)
	public void BringUp(int offset){
		//Set tab focus
		for (int t = 0; t < Tab.tabs.size(); t++){
			Tab.tabs.get(t).selected = false;
		}
		this.selected = true;
		
		//Include offset, take scrolling weirdness into account
	}
	
	public void BringUp(){
		BringUp(0);
	}
}
