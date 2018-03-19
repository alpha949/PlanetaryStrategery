package com.ue.ps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Button extends BaseActor{
	
	public Rectangle Hitbox;
	public boolean active;
	public boolean controled = true;
	
	Button(Texture texture, int x, int y) {
		super(texture);
		this.setPosition(x, y);
		this.Hitbox = this.getBoundingRectangle();
	}
	
	Button(Texture texture) {
		super(texture);
		this.Hitbox = this.getBoundingRectangle();
		System.out.println(this.Hitbox);
	}
	
	public boolean Pressed(Rectangle mouse) {
		if (this.isVisible() && Gdx.input.justTouched() && this.controled && this.Hitbox.overlaps(mouse)) {
			return true;
		}
		return false;
	}
	
	public boolean Pressed(Vector2 mouse) {
		if (this.isVisible() && Gdx.input.justTouched() && this.controled && this.Hitbox.contains(mouse)) {
			return true;
		}
		return false;
	}
	
	// override the hitbox size of the button to be different from the texture
	public void setSize(int x, int y) { 
		this.Hitbox = new Rectangle(getX(), getY(), x, y);
	}
	
	/**
	 * reset the hitbox to default
	 */
	public void redo(){
		this.Hitbox = this.getBoundingRectangle();
	}

	public void setPosition (float x, float y) {
		super.setPosition(x, y);
		this.Hitbox = this.getBoundingRectangle();
	}
}
