package com.ue.ps;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class GameplayScreen implements Screen{
	
	public Stage mainStage;
	public Stage uiStage;
	private int slowdown;
	private BaseActor mouseBlot = new BaseActor("assets/mouseBlot.png");
	private BaseActor camPos = new BaseActor("");
			
			
	private float maxZoom = 2;
	private float minZoom = 0.1f;
	public Game game;
	private ShapeRenderer shapeRender;
	public static float zoomAmount = 5;
	public static Vector2 mousePos = new Vector2();
	
	private Planet selectedPlanet;
	private boolean running = false;
	private boolean hasFocusedOnSelectedPlanet;
	private Player player = new Player(Faction.Xin);
	

	private Camera camera;
	private Camera uiCamera;
	private Viewport viewport;

	public static int cameraOffsetX;
	public static int cameraOffsetY;
	
	private BaseActor sidePanel = new BaseActor("assets/sidePanel.png");
	

	public GameplayScreen(Game g){
		game = g;
		create();
	}
	
	public void create() {
		
		camera = new OrthographicCamera();
		uiCamera = new OrthographicCamera();
		viewport = new ScreenViewport(camera);
	
		mainStage = new Stage(new ScreenViewport(camera));
		uiStage = new Stage(new ScreenViewport(uiCamera));

		
		
		WorldGen.setup(mainStage);
		
		shapeRender = new ShapeRenderer();
		mouseBlot.setPosition(50, 50);
		uiStage.addActor(mouseBlot);
		mainStage.addActor(camPos);
		
		sidePanel.setPosition(0, 0);
		uiStage.addActor(sidePanel);
		
		uiStage.addActor(player.resourcePanel);
		Gdx.input.setInputProcessor(new InputProcess());
		
	}
	
	public void render(float dt){
		
		mainStage.act(dt);
		uiStage.act();
		player.resourcePanel.setPosition(0,viewport.getScreenHeight()-player.resourcePanel.getHeight());
		
		OrthographicCamera cam = (OrthographicCamera) mainStage.getCamera();
		Vector2 center = new Vector2();
		camPos.setCenter(cam.position.x, cam.position.y);
		
		center = WorldGen.planetBorder.getCenter(center);
		//cam.position.x = center.x;
		//cam.position.y = center.y;
		//cam.position.set(p.getX()+p.getOriginX(),p.getY()+p.getOriginY(),0);
		//	bound	camera	to	layout 
		//cam.position.x=MathUtils.clamp(cam.position.x,PS.viewWidth/2,PS.mapWidth-PS.viewWidth/2); 
		//cam.position.y=MathUtils.clamp(cam.position.y,PS.viewHeight/2,PS.mapHeight-PS.viewHeight/2); 
		//zoomAmount = MathUtils.clamp(zoomAmount ,minZoom,maxZoom); 
		
		cam.update(); 
		

		
		mouseBlot.setPosition(Gdx.input.getX(), PS.viewHeight - Gdx.input.getY());
		mousePos = mainStage.screenToStageCoordinates(uiStage.stageToScreenCoordinates(mouseBlot.center));
		
	
	
//		mouseBlot.addAction(Actions.scaleTo(zoomAmount, zoomAmount));
	
		WorldGen.generate(mainStage, 0, 0);
		
		
		
		for (Planet p : WorldGen.allPlanets){
			if (p.getBoundingRectangle().contains(mousePos) && Gdx.input.justTouched()){
				
				selectedPlanet = p;
			}
		}
		
		if (selectedPlanet != null && !this.hasFocusedOnSelectedPlanet){
		
			System.out.println(selectedPlanet.center.x + ", " + selectedPlanet.center.y);
			if (this.focusCameraOnPlanet(selectedPlanet, cam)){
				
				selectedPlanet = null;
			}
		}
		
		Gdx.gl.glClearColor(0.0F, 0.0F, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		
		uiCamera.update();
		//camera
//		shapeRender.begin(ShapeType.Line);
//		shapeRender.setColor(Color.RED);
//		shapeRender.circle(0, 0, 200);
//		shapeRender.rect(1, 0, PS.viewWidth-1, PS.viewHeight-220);
//		shapeRender.end();
		mouseBlot.setZIndex(mainStage.getActors().size + uiStage.getActors().size);
		mainStage.getViewport().apply();
		mainStage.draw();
		uiStage.getViewport().apply();
		uiStage.draw();
		
	
		cam.zoom = zoomAmount;
	
		
		if (Gdx.input.isKeyPressed(Keys.UP)){
			cam.position.y += 10 * zoomAmount;
			cameraOffsetY += 10 * zoomAmount;
		} else if (Gdx.input.isKeyPressed(Keys.LEFT)){
			cam.position.x -= 10 * zoomAmount;
			cameraOffsetX -= 10 * zoomAmount;
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)){
			cam.position.x += 10 * zoomAmount;
			cameraOffsetX += 10 * zoomAmount;
		} else if (Gdx.input.isKeyPressed(Keys.DOWN)){
			cam.position.y -= 10 * zoomAmount;
			cameraOffsetY -= 10 * zoomAmount;
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			if (PS.paused) {
				PS.paused = false;
			} else {
				PS.paused = true;
			}
		}
		
		if (!PS.paused) {
			Gdx.input.setCursorCatched(true);
		} else {
			Gdx.input.setCursorCatched(false);
		}
		
		
		//this is just a patch, should find some way to fix
		/*for (Actor a : uiStage.getRoot().getChildren()) {
			a.addAction(Actions.scaleTo(zoomAmount, zoomAmount));
		}*/
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		camera = new OrthographicCamera(PS.viewWidth, PS.viewHeight); //Aspect Ratio Maintenance

	}

	@Override
	public void resize(int width, int height) {
		mainStage.getViewport().update(width, height, false);
		uiStage.getViewport().update(width, height, false);
		viewport.update(width, height);
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
	
	
	public boolean focusCameraOnPlanet(Planet p, OrthographicCamera c){
		if (Utils.glideCameraTo(p.center.x , p.center.y, c)){
			return true;
		} else{
			//This should glide in more smoothly
			zoomAmount = Planet.focusZoomAmount;
			
			
			return false;
		}
	
	
	}

}
