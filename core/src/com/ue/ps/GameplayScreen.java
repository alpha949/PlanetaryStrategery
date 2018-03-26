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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
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
	

	private Camera camera;
	private Camera uiCamera;
	private Viewport viewport;
	private Viewport uiViewport;

	public static int cameraOffsetX;
	public static int cameraOffsetY;

	private Planet planetX = new Planet();
	private SidePanel sidePanel = new SidePanel();
	
	private Line planetXLine;
	
	private GameServerClient server;
	
	
	public static TechTreePanel techTreePanel = new TechTreePanel();
	private Label multMessage = new Label("HELLO!", PS.font);
	

	private Texture executeTex = Images.getImg("execute");
	private Texture waitTex = Images.getImg("wait");
	private BaseActor executeButton = new BaseActor(executeTex);
	private ShipPointer activePointer;
	
	private Rectangle screenBounds = new Rectangle(0,0, PS.viewWidth, PS.viewHeight);
	
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

		//Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());

		WorldGen.setup(mainStage);

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
		multMessage.setPosition(PS.viewWidth- 200, 25);
		
		uiStage.addActor(executeButton);
		executeButton.setPosition(PS.viewWidth- 128, 0);
		
		Gdx.input.setInputProcessor(new InputProcess());
		
		//TODO move to ui
		if (PS.useServer){
			//start server
			 try {
	            	ServerSocket testSocket = Gdx.net.newServerSocket(Protocol.TCP, MenuScreen.port, null);
	            	testSocket.dispose();
	            } catch (GdxRuntimeException e) {
	            	System.out.println("port taken");
	            	 int i = 10/0;
	            	
	            	
	            }
				GameServer.Server.start();
				server = new GameServerClient(MenuScreen.ip, MenuScreen.port);
				this.server.registerPlayer(GameServerClient.clientPlayer);
				
			
				boolean getPlayers = false;
				while (!getPlayers){
					this.server.sendRequest("", GameServer.ServerCommands.getAllPlayers);
					//System.out.println(this.server.getRecievedData().length());
					if (GameServerClient.isCorrectDataType(this.server.getRecievedData(), GameServerClient.ClientRecieveCommands.players)) {
						Json json = new Json();
						System.out.println("GOT THE P DATA");
						
						
						ArrayList<PlayerData> pds = json.fromJson(ArrayList.class, this.server.getRecievedData().substring(0, this.server.getRecievedData().length()-1));
						for (PlayerData pd : pds){
							
							GameServerClient.players.add(Player.fromPlayerData(pd));
						}
						getPlayers = true;
						break;
					}
				}
				
				
				
				
				boolean getWorld = false;
				while (!getWorld) {
					this.server.sendRequest("", GameServer.ServerCommands.genWorld);
					this.server.sendRequest("", GameServer.ServerCommands.getWorld);
					//System.out.println(this.server.getRecievedData().length());
					if (GameServerClient.isCorrectDataType(this.server.getRecievedData(), GameServerClient.ClientRecieveCommands.world)) {
						Json json = new Json();
						System.out.println("GOT THE W DATA");
						
						World.setWorld(json.fromJson(ArrayList.class, this.server.getRecievedData().substring(0, this.server.getRecievedData().length()-1)));
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
	
			World.setWorld(WorldGen.generate(GameServerClient.players, 0, 0));
			
			for (Planet p : World.getWorld()) {
				mainStage.addActor(p);
				
			}
			targetPlanet = GameServerClient.clientPlayer.homePlanet;
			hasFocusedOnHome = true;
			
			
			
				
		}
		
		
		
		
		
		
	}

	public void render(float dt) {

		mainStage.act(dt);
		uiStage.act();
		
		sidePanel.update(uiStage);
	 
		
		
	
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
		stageMouseBlot.setPosition(mousePos.x - 8* zoomAmount, mousePos.y - 8*zoomAmount);
		// stageMouseBlot.setScale(zoomAmount, zoomAmount);
		stageMouseBlot.setSize(16 * zoomAmount, 16 * zoomAmount);
		
		
		
		// mouseBlot.addAction(Actions.scaleTo(zoomAmount, zoomAmount));
	
		//clicking on planets
		for (Planet p : World.getWorld()) {
			if (p.getBoundingRectangle().overlaps(stageMouseBlot.getBoundingRectangle())) {
	
				if (Gdx.input.justTouched() ) {
					
					
					if (!SidePanel.selectedShips.isEmpty()) {
						this.sidePanel.onDestinationSet(p);
						if (activePointer !=null){
							activePointer.setDestination(p);
							for (Ship s : SidePanel.selectedShips) {
								activePointer.ships.add(s);
							}
							//-----------------------------------------------------
							//TODO unset packet actions
							//-----------------------------------------------------
							activePointer.location.pointers.add(activePointer.clone());
							GameServerClient.packet.addAction(Action.sendShips(Utils.getShipIds(SidePanel.selectedShips), SidePanel.selectedShips.get(0).location.id, p.id));
							SidePanel.selectedShips.clear();
							activePointer.delete();
							activePointer = null;
						}
						
					} else {
						targetPlanet = p;
						
						this.sidePanel.setPlanet(p);
						
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
				activePointer = null;
				SidePanel.selectedShips.clear();
				
			}
		}
		
		for (Planet p : World.getWorld()){
			for (ShipPointer sp : p.pointers) {
				sp.staticRender(mainStage);
			}
			
		}	

		if (targetPlanet != null && !this.hasFocusedOnSelectedPlanet) {

			if (this.focusCameraOnPlanet(targetPlanet, cam)) {

				targetPlanet = null;
			}
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
			//Gdx.graphics.setWindowedMode(PS.viewWidth, PS.viewHeight);
		}
		//this doesn't work
	
		if (this.executeButton.getBoundingRectangle().contains(uiMousePos) && Gdx.input.justTouched()){
			if (!PS.useServer) {
				System.out.println("executing actions...");
				//temporary
				for (Action a : GameServerClient.packet.getActions()) {
					Action.execute(a, mainStage, GameServerClient.clientPlayer);
				}
				GameServerClient.packet.getActions().clear();
				//clear pointers
				for (Planet p : World.getWorld()) {
					for (ShipPointer sp : p.pointers) {
						sp.delete();
					}
					p.pointers.clear();
					
					//update buildings
					for (Building b : p.buildings) {
						if (b != null) {
							b.update(p);
						}
						
					}
					//perform combat
					p.onTurnUpdate();
				}
				
				
				sidePanel.unset();
			} else {
				//send actions
				server.sendRequest(GameServerClient.packet.getCompressedData(), GameServer.ServerCommands.recieveActions);
				turnActive = false;
				
				for (Planet p : World.getWorld()) {
					for (ShipPointer sp : p.pointers) {
						sp.delete();
					}
					p.pointers.clear();
					
				
				}
			}
		}
		
		
		
		if (!turnActive){
			
			server.sendRequest("", GameServer.ServerCommands.getAllActions);
			if (server.getRecievedData().length() > 4 && server.getRecievedData().charAt(server.getRecievedData().length()-1) == 'r'){
				GameServerClient.packet.setData(server.getRecievedData().substring(0, server.getRecievedData().length()-1));
				for (Action a : GameServerClient.packet.getActions()) {
					Action.execute(a, mainStage, GameServerClient.clientPlayer);
				}
				for (Planet p : World.getWorld()) {
					for (Building b : p.buildings) {
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
