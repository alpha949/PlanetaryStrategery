package com.ue.ps.ui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.ue.ps.BaseActor;
import com.ue.ps.PS;

//Valid contents require implementation of UIElement
public class Tab extends BaseActor {

	public static ArrayList<Tab> tabs = new ArrayList<Tab>();
	
	public Rectangle tabHitbox;
	public Rectangle Hitbox;
	
	public Texture FullImg;
	public boolean selected = false;
	public float scrollOffset;
	public double scrollVel;
	//Tab interior height, change with the screen. Static for now, change if more tab locations.
	public static int maxHeight = PS.viewHeight - 162; //should be 130 - 32 extra pixels where?
	//net size of all internal parts that take up space.
	public int internalHeight;
	
	public Tab(int tabx, int taby) {
		super(Images.BuildBox); //temporary image
		this.setPosition(0, 0);
		
		
		//Selector hitbox
		this.tabHitbox = new Rectangle(tabx, taby, this.getTexture().getWidth(), this.getTexture().getHeight());
		
		//Fixed size / placement of tab belly
		this.Hitbox = new Rectangle(0, 0, 250, 636);
		//this.setCullingArea(this.Hitbox); // culling broke
		this.internalHeight = 10; //buffer
		
		Tab.tabs.add(this); //Currently only one list of tabs. Make more and an input method to choose which to add more.
	}

	public void update(Vector2 mousePos) {
		if (this.selected && Hitbox.contains(mousePos)){

			Vector2 localMousePos = this.parentToLocalCoordinates(mousePos);
			localMousePos = new Vector2(localMousePos.x, localMousePos.y+this.scrollOffset);
			//Scroll check + scroll mod

			//Update contents
			for (Actor i : this.getChildren()){
				if (i instanceof UIElement) {
					((UIElement) i).update(localMousePos);
				}
				
			}
		}
		
		//time to select it
		if (!this.selected && tabHitbox.contains(mousePos) && Gdx.input.justTouched()) { //Add other button stuff like controlled, visible...
			System.out.println("Tab Selected");
			this.BringUp();
		}
		
		if (this.scrollVel != 0){
			if (Math.abs(this.scrollVel) < .2) {this.scrollVel = 0;}
			else {
				this.scrollVel = (this.scrollVel > 0) ? this.scrollVel - .2 : this.scrollVel + .2;
			}
		}
		
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// region.setRegion(anim.getKeyFrame(elapsedTime));
		Color c = getColor();
		batch.setColor(c.r, c.g, c.b, c.a); //I want to get rid of this but I'm scared

		if (this.selected){
			this.drawChildren(batch, parentAlpha);
		}
		
		if (isVisible()) {
			batch.draw(this.getTexture(), tabHitbox.x, tabHitbox.y-this.scrollOffset);
			
		}
		super.draw(batch, parentAlpha); //commenting it out gets rid of the unwanted corner thing, but also makes all the build options go up a bit in visuals only because WHO KNOWS
	}
	
	//Will be used to "select a ship, shows up on the side menu" (may change to take in object, may not be used at all)
	public void BringUp(int offset){
		//Set tab focus
		for (int t = 0; t < Tab.tabs.size(); t++){
			if (Tab.tabs.get(t) != this){
				Tab.tabs.get(t).selected = false;
				for (Actor b : Tab.tabs.get(t).getChildren()){
					if (b instanceof BaseActor) {
						((BaseActor)b).setVisible(false);
					}
				
				}
			}
		}
		this.selected = true;
		
		for (Actor b : this.getChildren()){
			if (b instanceof BaseActor) {
				((BaseActor)b).setVisible(true);  // <--- Commenting this out fixes the everything disappears bug, which is strange, because it makes things visible
			}
			
		}
		
		//Include offset, take scrolling weirdness into account
		if (offset != 0){}
	}
	
	public void BringUp(){
		BringUp(0);
	}
	
	public void reset(){
		this.clearChildren();
		this.internalHeight = 10;
		this.selected = false;
	}
}
