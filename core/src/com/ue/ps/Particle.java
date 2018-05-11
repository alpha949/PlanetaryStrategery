package com.ue.ps;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Particle extends BaseActor{
	
	
	public float vel;
	public float accel;
	public float angle;
	
	private float x;
	private float y;
	
	private Vector2 angleMult = new Vector2();
	
	public Particle(Texture t, float angle, float vel, float accel) {
		super(t);
		this.angle = angle;
		this.vel = vel;
		this.accel = accel;
	}
	
	public void act(float dt) {
		angleMult = Utils.convertVel(this.angle);
		x += vel * angleMult.x;
		y += vel * angleMult.y;
		
		vel += accel;
		
		this.setRotation(this.angle);
		
		this.setCenter(x, y);
		
	}
	
	
	public static Particle spawnParticle(Stage m, float x, float y, float vel, float accel, float angle, Texture t) {
		Particle p = new Particle(t, angle, vel, accel);
		p.x = x;
		p.y = y;
		p.setCenter(x, y);
		m.addActor(p);
		return p;
	}
	public static Particle spawnParticle(BaseActor m, float x, float y, float vel, float accel, float angle, Texture t) {
		Particle p = new Particle(t, angle, vel, accel);
		p.x = x;
		p.y = y;
		p.setCenter(x, y);
		m.addActor(p);
		return p;
	}
	
	
	
}
