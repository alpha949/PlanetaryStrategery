package com.ue.ps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;


public class BaseActor extends Group{
	public TextureRegion region;
	public Rectangle boundary;
	public Vector2 vel;
	protected Polygon boundingPolygon;
	public float centerX;
	public float centerY;
	public Vector2 center;

	protected float elapsedTime;
	private String path;
	
	public BaseActor(String path){
		super();
		region = new TextureRegion();
		boundary = new Rectangle();
		vel = new Vector2(0,0);
		elapsedTime = 0;
		this.center = new Vector2();
		this.path = path;
		genTexture(this.path);
		this.setOriginX(this.getWidth()/2);
		this.setOriginY(this.getHeight()/2);
		this.setRectangleBoundary();
	
	}
	
	public BaseActor(Texture t){
		super();
		region = new TextureRegion();
		boundary = new Rectangle();
		vel = new Vector2(0,0);
		elapsedTime = 0;
		this.center = new Vector2();
		this.setTexture(t);
		this.setOriginX(this.getWidth()/2);
		this.setOriginY(this.getHeight()/2);
		this.setRectangleBoundary();
	
	}
	
	public void setTexture(Texture t){
		int w = t.getWidth();
		int h = t.getHeight();
		setWidth(w);
		setHeight(h);
		region.setRegion(t);
		
	}
	
	public void genTexture(String path){
		try{
			Texture t = new Texture(Gdx.files.internal(path));
			setTexture(t);
			
		} catch (Exception GdxRuntimeException) {
			System.out.println("Error: Could not find: " + path + " substituting...");
			Texture t = new Texture(Gdx.files.internal("assets/missingTex.png"));
			setTexture(t);
		}
	}
	
	//Currently broken
	public void genAnimation(String path, int frames){
		TextureRegion[] frameImgs = new TextureRegion[frames];
		for (int n = 0; n < frames; n++){
			Texture tex = new Texture(Gdx.files.internal(path + n + ".png"));
			tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			frameImgs[n] = new TextureRegion(tex);
		}
		Array<TextureRegion> framesArray = new Array<TextureRegion>(frames);
		Animation anim = new Animation(0.1f, framesArray, Animation.PlayMode.LOOP_PINGPONG);
		//Texture baseTex = anim.getKeyFrame(0);
	}
	
	public Rectangle getBoundingRectangle(){
		boundary.set(getX(), getY(), getWidth(), getHeight());
		return boundary;
		
	}
	
	public void setRectangleBoundary(){
		float w = getWidth();
		float h = getHeight();
		float[] vertices = {0,0, w,0, w,h, 0,h};
		boundingPolygon = new Polygon(vertices);
		boundingPolygon.setOrigin(getOriginX(), getOriginY());
	}
	
	public Polygon getBoundingPolygon(){
		boundingPolygon.setPosition(getX(), getY());
		boundingPolygon.getRotation();
		return boundingPolygon;
	}
	
	public boolean overlaps(BaseActor other, boolean resolve){
		Polygon poly1 = this.getBoundingPolygon();
		Polygon poly2 = other.getBoundingPolygon();
		
		if (!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle())) return false;
		
		MinimumTranslationVector mtv = new MinimumTranslationVector();
		boolean polyOverlap = Intersector.overlapConvexPolygons(poly1, poly2, mtv);
		
		if (polyOverlap && resolve){
			this.moveBy(mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth);
		}
		float significant = 0.5f;
		return (polyOverlap && (mtv.depth > significant));
		
	}
	
	public void act(float dt){
		super.act(dt);
		moveBy(vel.x * dt, vel.y * dt);
		elapsedTime += dt;
		center.x = this.getX() + this.getWidth()/2;
		center.y = this.getY() + this.getHeight()/2;
		
	}
	
	public void draw(Batch batch, float parentAlpha){
		//region.setRegion(anim.getKeyFrame(elapsedTime));
		
		Color c = getColor();
		batch.setColor(c.r, c.g, c.b, c.a);
		if (isVisible()){
			batch.draw(region, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		
		}
		super.draw(batch, parentAlpha);
	}
	
	public void copy(BaseActor orig){
		this.region = new TextureRegion(orig.region);
		if (orig.boundingPolygon != null){
			this.boundingPolygon = new Polygon(orig.boundingPolygon.getVertices());
			this.boundingPolygon.setOrigin(orig.getOriginX(), orig.getOriginY());
			
		}
		this.setPosition(orig.getX(), orig.getY());
		this.setOriginX(orig.getOriginX());
		this.setOriginY(orig.getOriginY());
		this.setWidth(orig.getWidth());
		this.setHeight(orig.getHeight());
		this.setColor(orig.getColor());
		this.setVisible(orig.isVisible());
		
	}
	
	public BaseActor clone(){
		BaseActor newbie = new BaseActor(this.path);
		newbie.copy(this);
		return newbie;
	}
	
	public double pointAt(double x, double y, float speed, boolean rotate){
		
		double yDiff = y - centerY;
		double xDiff = x - centerX;
		double newAngle = Math.toDegrees(Math.atan2(yDiff, xDiff));
		
		
	
		
		if (rotate){
			this.addAction(Actions.rotateTo((float) newAngle, speed));
		}
		return newAngle;
		
		
	}
	
	
	public double distanceTo(double x, double y){
		
		return Math.hypot(Math.abs(this.center.x - x), Math.abs(this.center.y - y));
	}
	
	public void setCenter(float x, float y){
		this.setPosition(x + this.getWidth()/2, y + this.getHeight()/2);
	}
	
	
	
	
}
