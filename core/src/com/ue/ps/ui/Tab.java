package com.ue.ps.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.ue.ps.BaseActor;

public class Tab extends BaseActor {

	public Tab(Texture t) {
		super(t);
		this.setCullingArea(this.getBoundingRectangle());
	}

	public void update(Vector2 mousePos) {

	}
}
