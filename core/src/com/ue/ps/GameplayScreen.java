package com.ue.ps;



import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

public class GameplayScreen implements Screen {

	public Stage mainStage;
	public Stage uiStage;
	private int slowdown;
	public static BaseActor mouseBlot = new BaseActor("assets/mouseBlot.png");
	private BaseActor stageMouseBlot = new BaseActor("assets/stageMouseBlot.png");
	private BaseActor camPos = new BaseActor("");

	private float maxZoom = 2;
	private float minZoom = 0.1f;
	public Game game;
	private ShapeRenderer shapeRender;
	public static float zoomAmount = 5;
	public static Vector2 mousePos = new Vector2();

	private Planet targetPlanet;
	private Planet selectedPlanet = new Planet();
	private boolean running = false;
	private boolean hasFocusedOnSelectedPlanet;
	public static Player player = new Player(Faction.Xin);

	private Camera camera;
	private Camera uiCamera;
	private Viewport viewport;
	private Viewport uiViewport;

	public static int cameraOffsetX;
	public static int cameraOffsetY;

	private Planet planetX = new Planet();
	private SidePanel sidePanel = new SidePanel();
	
	private Line planetXLine;
	
	private Server server = new Server();
	
	
	public static TechTreePanel techTreePanel = new TechTreePanel();
	private Label multMessage = new Label("HELLO!", PS.font);
	
	private BaseActor executeButton = new BaseActor("assets/execute.png");
	
	private ShipPointer activePointer;
	
	public static Packet packet = new Packet();
	
	private ArrayList<ShipPointer> allPointers = new ArrayList<ShipPointer>();
	
	public GameplayScreen(Game g) {
		game = g;
		create();
	}

	public void create() {

		camera = new OrthographicCamera();
		uiCamera = new OrthographicCamera();
		viewport = new ScreenViewport(camera);
		uiViewport = new ScalingViewport(Scaling.fill, PS.viewWidth, PS.viewHeight, uiCamera);

		mainStage = new Stage(new ScreenViewport(camera));
		uiStage = new Stage(uiViewport);

		//Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());

		WorldGen.setup(mainStage);

		shapeRender = new ShapeRenderer();
		mouseBlot.setPosition(50, 50);
		uiStage.addActor(mouseBlot);
		mainStage.addActor(camPos);
		mainStage.addActor(stageMouseBlot);

		
		sidePanel.setPosition(0, 0);
		uiStage.addActor(sidePanel);
		

		uiStage.addActor(player.resourcePanel);
		uiStage.addActor(techTreePanel);
		
		uiStage.addActor(multMessage);
		multMessage.setPosition(PS.viewWidth- 200, 25);
		
		uiStage.addActor(executeButton);
		executeButton.setPosition(PS.viewWidth- 128, 0);
		
		Gdx.input.setInputProcessor(new InputProcess());
		
	

	}

	public void render(float dt) {

		mainStage.act(dt);
		uiStage.act();
		
		sidePanel.update(uiStage);
		
		
		
	
		player.resourcePanel.setPosition(0, PS.viewHeight - player.resourcePanel.getHeight());
		player.resourcePanel.update(uiStage);
		
		OrthographicCamera cam = (OrthographicCamera) mainStage.getCamera();
		Vector2 center = new Vector2();
		camPos.setCenter(cam.position.x, cam.position.y);

		center = WorldGen.planetBorder.getCenter(center);
		// cam.position.x = center.x;
		// cam.position.y = center.y;
		// cam.position.set(p.getX()+p.getOriginX(),p.getY()+p.getOriginY(),0);
		// bound camera to layout
		// cam.position.x=MathUtils.clamp(cam.position.x,PS.viewWidth/2,PS.mapWidth-PS.viewWidth/2);
		// cam.position.y=MathUtils.clamp(cam.position.y,PS.viewHeight/2,PS.mapHeight-PS.viewHeight/2);
		// zoomAmount = MathUtils.clamp(zoomAmount ,minZoom,maxZoom);

		cam.update();

		mouseBlot.setPosition(Gdx.input.getX(), PS.viewHeight - Gdx.input.getY());
		mousePos = mainStage.screenToStageCoordinates(uiStage.stageToScreenCoordinates(mouseBlot.center));
		// these numbers (8 and 13) seem really arbitrary, there's probably some
		// reason for them...
		stageMouseBlot.setPosition(mousePos.x - 8* zoomAmount, mousePos.y + 13*zoomAmount);
		// stageMouseBlot.setScale(zoomAmount, zoomAmount);
		stageMouseBlot.setSize(16 * zoomAmount, 16 * zoomAmount);
		
		
		
		// mouseBlot.addAction(Actions.scaleTo(zoomAmount, zoomAmount));
		if (!PS.recordingGeneration) {
			WorldGen.generate(mainStage, 0, 0);
		} else if (Gdx.input.isTouched()) {
			PS.recordingGeneration = false;
		}
		//clicking on planets
		for (Planet p : WorldGen.allPlanets) {
			if (p.getBoundingRectangle().overlaps(stageMouseBlot.getBoundingRectangle())) {
	
				if (Gdx.input.justTouched() ) {
					if (SidePanel.selectedShips.isEmpty() && p.owner == player) {
						targetPlanet = p;
						
						this.sidePanel.setPlanet(p);
					} else {
						this.sidePanel.onDestinationSet(p);
						if (activePointer !=null){
							activePointer.setDestination(p);
							activePointer.ships = SidePanel.selectedShips;
							packet.addAction(new Action(Utils.getShipIds(SidePanel.selectedShips), SidePanel.selectedShips.get(0).location, p));
							allPointers.add(activePointer);
							activePointer.delete();
							activePointer = null;
						}
						
					}
				
					

				}

			}
		}
		

		if (!SidePanel.selectedShips.isEmpty()) {
			if (activePointer == null){
				activePointer = new ShipPointer(SidePanel.selectedShips.get(0).location);
				
			}
			if (activePointer !=null){
				activePointer.renderShipPointer(stageMouseBlot.center, mainStage);
				
			}
		} else {
			if (activePointer !=null){
				activePointer.delete();
				
			}
		}
		
		for (ShipPointer sp : allPointers){
			sp.staticRender(mainStage);
		}	

		if (targetPlanet != null && !this.hasFocusedOnSelectedPlanet) {

			if (this.focusCameraOnPlanet(targetPlanet, cam)) {

				targetPlanet = null;
			}
		}
		
		

		
		if (Gdx.input.isKeyJustPressed(Keys.F)) {
			server.send("HELLO WORLD!", "10.0.0.181");
		}
		
		multMessage.setText(server.getMessage());
	


	

		Gdx.gl.glClearColor(0.0F, 0.0F, 0, 1);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		uiCamera.update();
		// camera
		// shapeRender.begin(ShapeType.Line);
		// shapeRender.setColor(Color.RED);
		// shapeRender.circle(0, 0, 200);
		// shapeRender.rect(1, 0, PS.viewWidth-1, PS.viewHeight-220);
		// shapeRender.end();
		mouseBlot.setZIndex(mainStage.getActors().size + uiStage.getActors().size);
		mainStage.getViewport().apply();
		mainStage.draw();
		uiStage.getViewport().apply();
		uiStage.draw();

		cam.zoom = zoomAmount;

		if (Gdx.input.isKeyPressed(Keys.UP)) {
			cam.position.y += 10 * zoomAmount;
			cameraOffsetY += 10 * zoomAmount;
		} else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			cam.position.x -= 10 * zoomAmount;
			cameraOffsetX -= 10 * zoomAmount;
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			cam.position.x += 10 * zoomAmount;
			cameraOffsetX += 10 * zoomAmount;
		} else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			cam.position.y -= 10 * zoomAmount;
			cameraOffsetY -= 10 * zoomAmount;
		}

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			if (PS.paused) {
				PS.paused = false;
			} else {
				PS.paused = true;
			}
			//Gdx.graphics.setWindowedMode(PS.viewWidth, PS.viewHeight);
		}
		
		if (executeButton.getBoundingRectangle().contains(mouseBlot.center) && Gdx.input.justTouched()) {
			//send ip here;
			packet.send("");
				
		}

		if (!PS.paused) {
			Gdx.input.setCursorCatched(true);
		} else {
			Gdx.input.setCursorCatched(false);
		}

	}
	
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		camera = new OrthographicCamera(PS.viewWidth, PS.viewHeight); // Aspect
																		// Ratio
																		// Maintenance

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

	public boolean focusCameraOnPlanet(Planet p, OrthographicCamera c) {
		if (Utils.glideCameraTo(p.center.x, p.center.y, c)) {
			return true;
		} else {
			if (zoomAmount > Planet.focusZoomAmount) {
				zoomAmount = Planet.focusZoomAmount/zoomAmount  + Planet.focusZoomAmount;
			}
			

			return false;
		}

	}

}
