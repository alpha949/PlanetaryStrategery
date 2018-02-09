package com.ue.ps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class ParticleSpawner extends ParticleEffect{
	
	private Color color;
	private Boolean visable;
	
	public ParticleSpawner(String path) {
		this.loadEmitters(Gdx.files.internal(path));
		this.color = Color.WHITE;
	}
	
	public void setColor(Color c) {
		this.color = c;
	}
	
	public void setVisable(boolean visable) {
		this.visable = visable;
	}
	
	
	public void drawOn(Stage s) {
		Batch batch = s.getBatch();
	
		Color c = color;
		batch.setColor(c.r, c.g, c.b, c.a);
		if (this.visable) {
			this.draw(batch, 1.0f);
			

		}
		super.draw(batch,  1.0f);
	}
	
}
