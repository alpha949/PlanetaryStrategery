package com.ue.ps;

import java.awt.DisplayMode;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
	private Player player = new Player(Faction.Xin);

	private Camera camera;
	private Camera uiCamera;
	private Viewport viewport;
	private Viewport uiViewport;

	public static int cameraOffsetX;
	public static int cameraOffsetY;


	private SidePanel sidePanel = new SidePanel();

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
		Gdx.input.setInputProcessor(new InputProcess());

	}

	public void render(float dt) {

		mainStage.act(dt);
		uiStage.act();
		
		sidePanel.update(uiStage);
		
		// Again with the weird arbitrary 24...
		player.resourcePanel.setPosition(0, PS.viewHeight - player.resourcePanel.getHeight());

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
		// these numbers (8 and 53) seem really arbitrary, there's probably some
		// reason for them...
		stageMouseBlot.setPosition(mousePos.x - 8 * zoomAmount, mousePos.y - 8 * zoomAmount);
		// stageMouseBlot.setScale(zoomAmount, zoomAmount);
		stageMouseBlot.setSize(16 * zoomAmount, 16 * zoomAmount);

		// mouseBlot.addAction(Actions.scaleTo(zoomAmount, zoomAmount));
		if (!PS.recordingGeneration) {
			WorldGen.generate(mainStage, 0, 0);
		} else if (Gdx.input.isTouched()) {
			PS.recordingGeneration = false;
		}

		for (Planet p : WorldGen.allPlanets) {
			if (p.getBoundingRectangle().overlaps(stageMouseBlot.getBoundingRectangle())) {
	
				if (Gdx.input.justTouched()) {
					targetPlanet = p;

				
					this.sidePanel.setPlanet(p);
					

				}

			}
		}

		if (targetPlanet != null && !this.hasFocusedOnSelectedPlanet) {

			if (this.focusCameraOnPlanet(targetPlanet, cam)) {

				targetPlanet = null;
			}
		}

		
		
	


		if (WorldGen.allPlanets.size() > 2) {
			//Line.genLine(new Line(WorldGen.allPlanets.get(0), stageMouseBlot, Faction.Braecious), mainStage);

		}

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
			// This should glide in more smoothly
			zoomAmount = Planet.focusZoomAmount;

			return false;
		}

	}

}
