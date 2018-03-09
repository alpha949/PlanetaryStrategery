package com.ue.ps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Button extends BaseActor{
	
	public Rectangle Hitbox;
	public boolean active;
	
	Button(Texture texture, int x, int y) {
		super(texture);
		this.setPosition(x, y);
		this.Hitbox = this.getBoundingRectangle();
	}
	
	Button(Texture texture) {
		super(texture);
		this.Hitbox = this.getBoundingRectangle();
	}
	
	public boolean Pressed(Rectangle mouse) {
		if (this.active && this.Hitbox.overlaps(mouse)) {
			return true;
		}
		return false;
	}
	
	// override the hitbox size of the button to be different from the texture
	public void setSize(int x, int y) { 
		this.Hitbox = new Rectangle(getX(), getY(), x, y);
	}
	
}
