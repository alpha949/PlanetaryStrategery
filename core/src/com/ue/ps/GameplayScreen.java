package com.ue.ps;

import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;


public class GameplayScreen implements Screen{
	
	public Stage mainStage;
	public Stage uiStage;
	private int slowdown;
	private BaseActor p = new BaseActor("");
			
			
	private float maxZoom = 2;
	private float minZoom = 0.1f;
	public Game game;
	private ShapeRenderer shapeRender;
	public static float zoomAmount;
	public static Vector2 mousePos = new Vector2();

	
	

	public GameplayScreen(Game g){
		game = g;
		create();
	}
	
	public void create() {
		
		
		mainStage = new Stage();
		uiStage = new Stage();

		

		WorldGen.setup(mainStage);
		
		shapeRender = new ShapeRenderer();
		p.setPosition(50, 50);
		mainStage.addActor(p);
		Gdx.input.setInputProcessor(new InputProcess());

	}
	
	public void render(float dt){
		
		
		
		
		mainStage.act(dt);
		uiStage.act();
		
		
		OrthographicCamera cam = (OrthographicCamera) mainStage.getCamera();
		Vector2 center = new Vector2();
				
		center = WorldGen.planetBorder.getCenter(center);
		//cam.position.x = center.x;
		//cam.position.y = center.y;
		//cam.position.set(p.getX()+p.getOriginX(),p.getY()+p.getOriginY(),0);
		//	bound	camera	to	layout 
		//cam.position.x=MathUtils.clamp(cam.position.x,PS.viewWidth/2,PS.mapWidth-PS.viewWidth/2); 
		//cam.position.y=MathUtils.clamp(cam.position.y,PS.viewHeight/2,PS.mapHeight-PS.viewHeight/2); 
		//zoomAmount = MathUtils.clamp(zoomAmount ,minZoom,maxZoom); 
		
		cam.update(); 
		
		
		mousePos.x = (Gdx.input.getX() * zoomAmount);
		mousePos.y = (cam.viewportHeight- Gdx.input.getY())  * zoomAmount;
		p.setPosition(mousePos.x, mousePos.y);
		
		
		if (slowdown % 1 == 0){
			WorldGen.generate(mainStage, 0, 0);
		}
		
		
		slowdown += 1;
		
		Gdx.gl.glClearColor(0.0F, 0.0F, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		
		//camera
		shapeRender.begin(ShapeType.Line);
		shapeRender.setColor(Color.RED);
		
		shapeRender.rect(1, 0, PS.viewWidth-1, PS.viewHeight-220);
		shapeRender.end();
		
		mainStage.draw();
	
		uiStage.draw();
		
	
		cam.zoom = zoomAmount;
		System.out.println("z: " + cam.zoom);
		
		if (Gdx.input.isKeyPressed(Keys.UP)){
			cam.position.y += 10;
		} else if (Gdx.input.isKeyPressed(Keys.LEFT)){
			cam.position.x -= 10;
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)){
			cam.position.x += 10;
		} else if (Gdx.input.isKeyPressed(Keys.DOWN)){
			cam.position.y -= 10;
		}
	
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
