package com.ue.ps.ui;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ue.ps.BaseActor;
import com.ue.ps.Faction;
import com.ue.ps.PS;
import com.ue.ps.Particle;
import com.ue.ps.Planet;
import com.ue.ps.Player;
import com.ue.ps.Utils;
import com.ue.ps.World;
import com.ue.ps.WorldGen;
import com.ue.ps.buildings.Building;
import com.ue.ps.ships.Line;
import com.ue.ps.ships.Ship;
import com.ue.ps.ships.ShipPointer;
import com.ue.ps.ships.ShipType;
import com.ue.ps.systems.Action;
import com.ue.ps.systems.GameServer;
import com.ue.ps.systems.GameServerClient;
import com.ue.ps.systems.InputProcess;
import com.ue.ps.systems.Settings;

public class GameplayScreen implements Screen {

	public Stage mainStage;
	public Stage uiStage;
	public static BaseActor mouseBlot = new BaseActor("assets/mouseBlot.png");
	private BaseActor stageMouseBlot = new BaseActor("assets/stageMouseBlot.png");
	private BaseActor camPos = new BaseActor("");

	public Game game;
	private ShapeRenderer shapeRender;
	public static float zoomAmount = 5;
	public static Vector2 mousePos = new Vector2();

	private Planet targetPlanet;

	private boolean hasFocusedOnSelectedPlanet;

	private Camera camera;
	private Camera uiCamera;
	private Viewport viewport;
	private Viewport uiViewport;
	
	private Player evilPerson = new Player("EVIL PERSON", Faction.Braecious);

	public static int cameraOffsetX;
	public static int cameraOffsetY;

	
	private SidePanel sidePanel = new SidePanel();



	public static TechTreePanel techTreePanel = new TechTreePanel();
	private Label multMessage = new Label("HELLO!", PS.font);

	private Texture executeTex = Images.getImg("execute");
	private Texture waitTex = Images.getImg("wait");
	private BaseActor executeButton = new BaseActor(executeTex);
	private ShipPointer activePointer;



	private ResourcePanel resourcePanel;

	private boolean hasFocusedOnHome;
	private boolean turnActive = true;

	private boolean hasAskedServer = false;

	private Vector2 uiMousePos = new Vector2();

	public GameplayScreen(Game g) {
		game = g;
		create();
	}

	public void create() {
		resourcePanel = new ResourcePanel(GameServerClient.clientPlayer.faction, GameServerClient.clientPlayer);
		camera = new OrthographicCamera();
		uiCamera = new OrthographicCamera();
		viewport = new ScreenViewport(camera);
		uiViewport = new ScalingViewport(Scaling.fill, PS.viewWidth, PS.viewHeight, uiCamera);

		mainStage = new Stage(new ScreenViewport(camera));
		uiStage = new Stage(uiViewport);

		// Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());

		shapeRender = new ShapeRenderer();
		mouseBlot.setPosition(50, 50);
		uiStage.addActor(mouseBlot);
		mainStage.addActor(camPos);
		mainStage.addActor(stageMouseBlot);

		sidePanel.setPosition(0, PS.viewHeight - sidePanel.getHeight());
		uiStage.addActor(sidePanel);

		uiStage.addActor(resourcePanel);
		uiStage.addActor(techTreePanel);

		uiStage.addActor(multMessage);
		multMessage.setPosition(PS.viewWidth - 200, 25);

		uiStage.addActor(executeButton);
		executeButton.setPosition(PS.viewWidth - 128, 0);

		Gdx.input.setInputProcessor(new InputProcess());

		// TODO move to ui
		if (PS.useServer) {

			boolean getWorld = false;
			while (!getWorld) {
				if (PS.isHost) {
					PS.client.sendRequest("", GameServer.ServerCommands.genWorld);
				}
				PS.client.sendRequest("", GameServer.ServerCommands.getWorld);
				// System.out.println(PS.client.getRecievedData().length());
				if (GameServerClient.isCorrectDataType(PS.client.getRecievedData(), GameServerClient.ClientRecieveCommands.world)) {
					Json json = new Json();
					System.out.println("GOT THE W DATA");

					World.setWorld(
							json.fromJson(ArrayList.class, PS.client.getRecievedData().substring(0, PS.client.getRecievedData().length() - 1)));
					getWorld = true;
					break;
				}
			}

			for (Planet p : World.getWorld()) {
				mainStage.addActor(p);

				if (p.owner != null && p.owner.getUser().equals(GameServerClient.clientPlayer.getUser())) {
					System.out.println("ye");
					GameServerClient.clientPlayer.homePlanet = p;
				}
			}
			targetPlanet = GameServerClient.clientPlayer.homePlanet;
			hasFocusedOnHome = true;

		} else {
			 GameServerClient.setUpPlayer(Faction.Lelouk);
			 GameServerClient.players.add(GameServerClient.clientPlayer);
				GameServerClient.players.add(evilPerson);
			World.setWorld(WorldGen.generate(GameServerClient.players, 0, 0));

			for (Planet p : World.getWorld()) {
				mainStage.addActor(p);
				if (p.owner != null && p.owner.getUser().equals(GameServerClient.clientPlayer.getUser())) {
					System.out.println("ye");
					GameServerClient.clientPlayer.homePlanet = p;
				}

			}
			targetPlanet = GameServerClient.clientPlayer.homePlanet;
			hasFocusedOnHome = true;

		}
		
		for (Planet p : World.getWorld()) {
			if (p.isHomePlanet) {
				Ship.spawnShip(GameServerClient.clientPlayer, p, ShipType.dread, 10);
			
				Ship.spawnShip(evilPerson, p, ShipType.dread, 100);
				
			}
			BaseActor glow = new BaseActor(Images.planetGlow);
			
			glow.setSize(p.getWidth() * p.getSize(), p.getHeight() * p.getSize());
			//position is off by a little when the planet is scaled
			glow.setCenter(p.center.x, p.center.y);
			mainStage.addActor(glow);
			glow.setZIndex(0);
			p.glow = glow;
			
		}

	}

	public void render(float dt) {

		mainStage.act(dt);
		uiStage.act();

	

		resourcePanel.setPosition(0, PS.viewHeight - resourcePanel.getHeight());
		resourcePanel.update(uiStage);

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
		MathUtils.clamp(mouseBlot.center.x, 0, PS.viewWidth);
		MathUtils.clamp(mouseBlot.center.y, 0, PS.viewHeight);
		mousePos = mainStage.screenToStageCoordinates(uiStage.stageToScreenCoordinates(mouseBlot.center));

		mouseBlot.setPosition(Gdx.input.getX(), PS.viewHeight - Gdx.input.getY());
		uiMousePos = uiStage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
		// these numbers (8 and 13) seem really arbitrary, there's probably some
		// reason for them... 
		stageMouseBlot.setPosition(mousePos.x - 8 * zoomAmount, mousePos.y - 8 * zoomAmount);
		// stageMouseBlot.setScale(zoomAmount, zoomAmount);
		stageMouseBlot.setSize(16 * zoomAmount, 16 * zoomAmount);

		// mouseBlot.addAction(Actions.scaleTo(zoomAmount, zoomAmount));

		// clicking on planets
		for (Planet p : World.getWorld()) {
			if (p.getBoundingRectangle().overlaps(stageMouseBlot.getBoundingRectangle())) {

				if (Gdx.input.justTouched()) {

					if (!SidePanel.selectedShips.isEmpty()) {
						this.sidePanel.onDestinationSet(p);
						if (activePointer != null) {
							activePointer.setDestination(p);
							for (Ship s : SidePanel.selectedShips) {
								activePointer.ships.add(s);
							}

							System.out.println("selectedPlanetID: " + p.id);
							activePointer.location.pointers.add(activePointer.clone());
							SidePanel.selectedShips.clear();
							activePointer.delete();
							activePointer = null;
						} else {
							System.out.println("Tried to set null activePointer");
						}

					} else {
						targetPlanet = p;

						this.sidePanel.setPlanet(p);

					}

				}

			}
		}
		
		for (Planet p : World.getWorld()) {
			if (p.glow.getColor() != Color.BLACK) {
				p.glow.setColor(Color.BLACK);
			}
		}
		
		if (!SidePanel.selectedShips.isEmpty()) {
			for (Planet p : World.getWorld()) {
				if (SidePanel.selectedShips.get(0).location.distanceTo(p.center.x, p.center.y) < 15000) {
					p.glow.setColor(GameServerClient.clientPlayer.faction.color);
				}
			}
			if (activePointer == null) {
				activePointer = new ShipPointer(SidePanel.selectedShips.get(0).location);
			}
			if (activePointer != null) {
				activePointer.renderShipPointer(stageMouseBlot.center, mainStage);
			}
		} else {
			if (activePointer != null) {
				
				activePointer.delete();
				activePointer = null;
				SidePanel.selectedShips.clear();

			}
		}

		for (Planet p : World.getWorld()) {
			for (ShipPointer sp : p.pointers) {
				sp.staticRender(mainStage);
			}

		}

		if (targetPlanet != null && !this.hasFocusedOnSelectedPlanet) {

			if (this.focusCameraOnPlanet(targetPlanet, cam)) {

				targetPlanet = null;
			}
		}
		
		sidePanel.update(uiStage);
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

		if (Gdx.input.isKeyPressed(Keys.W)) {
			cam.position.y += Settings.moveSpeed * zoomAmount;
			cameraOffsetY += Settings.moveSpeed * zoomAmount;
		} else if (Gdx.input.isKeyPressed(Keys.A)) {
			cam.position.x -= Settings.moveSpeed * zoomAmount;
			cameraOffsetX -= Settings.moveSpeed * zoomAmount;
		} else if (Gdx.input.isKeyPressed(Keys.D)) {
			cam.position.x += Settings.moveSpeed * zoomAmount;
			cameraOffsetX += Settings.moveSpeed * zoomAmount;
		} else if (Gdx.input.isKeyPressed(Keys.S)) {
			cam.position.y -= Settings.moveSpeed * zoomAmount;
			cameraOffsetY -= Settings.moveSpeed * zoomAmount;
		}

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			if (PS.paused) {
				PS.paused = false;
			} else if (techTreePanel.isOpen) {
				techTreePanel.close();
			} else {
				PS.paused = true;
			}
			// Gdx.graphics.setWindowedMode(PS.viewWidth, PS.viewHeight);
		}
		// this doesn't work
		
		
	
		
		if (this.executeButton.getBoundingRectangle().contains(uiMousePos) && Gdx.input.justTouched()) {
			for (Planet p : World.getWorld()) {
				for (ShipPointer o : p.pointers) {
					GameServerClient.packet.addAction(
							Action.sendShips(Utils.getShipIds(o.ships), o.location.id, o.destination.id));
				
				}
			}
			
			this.sidePanel.setPlanet(this.sidePanel.getSelectedPlanet());
			this.sidePanel.setActiveTab(sidePanel.getActiveTab());
			
			if (!PS.useServer) {
				System.out.println("executing actions...");
				
				
				
				// temporary
				for (Action a : GameServerClient.packet.getActions()) {
					Action.execute(a, mainStage, GameServerClient.clientPlayer);
				}
				GameServerClient.packet.getActions().clear();
				// clear pointers
				for (Planet p : World.getWorld()) {
					for (ShipPointer sp : p.pointers) {
						sp.delete();
					}
					p.pointers.clear();

					// update buildings
					for (Building b : p.landBuildings) {
						if (b != null) {
							b.update(p);
						}

					}
					// perform combat
					p.onTurnUpdate();
					
					
				}
				//update ship containers
				for (ShipContainer sc : SidePanel.shipContainers) {
					sc.makeDisp();
				}
				
			

				//sidePanel.unset();
			} else {
				// send actions
				PS.client.sendRequest(GameServerClient.packet.getCompressedData(), GameServer.ServerCommands.recieveActions);
				turnActive = false;

				for (Planet p : World.getWorld()) {
					for (ShipPointer sp : p.pointers) {
						sp.delete();
					}
					p.pointers.clear();

				}
			}
		}

		if (!turnActive) {

			PS.client.sendRequest("", GameServer.ServerCommands.getAllActions);
			if (PS.client.getRecievedData().length() > 4 && PS.client.getRecievedData().charAt(PS.client.getRecievedData().length() - 1) == 'r') {
				GameServerClient.packet.setData(PS.client.getRecievedData().substring(0, PS.client.getRecievedData().length() - 1));
				for (Action a : GameServerClient.packet.getActions()) {
					Action.execute(a, mainStage, GameServerClient.clientPlayer);
				}
				for (Planet p : World.getWorld()) {
					for (Building b : p.landBuildings) {
						if (b != null) {
							b.update(p);
						}

					}
				}

				turnActive = true;
				hasAskedServer = false;
			}
			if (executeButton.getTexture() != waitTex) {
				executeButton.setTexture(waitTex);
			}

		} else {
			if (executeButton.getTexture() != executeTex) {
				executeButton.setTexture(executeTex);
			}
		}

		if (!PS.paused) {
			Gdx.input.setCursorCatched(true);
		} else {
			Gdx.input.setCursorCatched(false);
		}
		
		if (techTreePanel.isOpen) {
			techTreePanel.update(mouseBlot);
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
				zoomAmount = Planet.focusZoomAmount / zoomAmount + Planet.focusZoomAmount;
			}

			return false;
		}

	}

}
