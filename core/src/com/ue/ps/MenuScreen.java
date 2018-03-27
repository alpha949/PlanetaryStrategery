package com.ue.ps;



import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.OnscreenKeyboard;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

public class MenuScreen implements Screen {

	public Stage mainStage;



	public Game game;
	private ShapeRenderer shapeRender;
	public static Vector2 mousePos = new Vector2();
	public static BaseActor mouseBlot = new BaseActor("assets/mouseBlot.png");

	

	private Camera camera;
	private Viewport viewport;

	


	
	private BaseActor title = new BaseActor();
	
	private BaseActor quitButton = new BaseActor();
	private Button beginButton = new Button(Images.getImg("beginGameButton"));
	
	private Button singleplayerButton = new Button(Images.getImg("singButton"));
	private Button multiplayerButton = new Button(Images.getImg("multButton"));
	
	private TextFieldStyle style = new TextFieldStyle(PS.theFont, Color.WHITE, null, null, null);
	private TextField ipInput;
	private String text;
	
	public static int port;
	public static String ip;
	private Skin skin = new Skin();
	
	private Rectangle fSelectBox = new Rectangle();

	private boolean promptUserCreation;
	private Button editUserButton = new Button(Images.getImg("editUserButton"));
	private Button doneButton = new Button(Images.getImg("done"));
	private BaseActor editUserOverlay = new BaseActor(Images.getImg("grayBack"));
	
	private BaseActor mainOverlay = new BaseActor(Images.getImg("grayBack"));
	
	private Button joinGameButton = new Button(Images.getImg("joinGameButton"));
	private Button hostGameButton = new Button(Images.getImg("hostGameButton"));
	private BaseActor multChoiceOverlay = new BaseActor(Images.getImg("grayBack"));
	
	private Button multChoiceBackButton = new Button(Images.getImg("backButton"));
	private Button waitRoomBackButton = new Button(Images.getImg("backButton"));
	
	private BaseActor waitingRoomOverlay = new BaseActor(Images.getImg("grayBack"));
	private TextField userNameInput;
	private Label userPrompt = new Label("Enter User Name:", PS.font);
	
	private String selectedFaction = "Xin";
	
	private BaseActor[] factionBoxes = new BaseActor[Faction.allFactions.length];
	
	private BaseActor activeOverlay = new BaseActor();
	private BaseActor labelBacking = new BaseActor(Images.emptyTexture);
	private Label feedbackLabel = new Label("", PS.font);
	
	private Label portLabel = new Label("Port: 0000", PS.font);
	
	private ArrayList<Label> inRoomUsers = new ArrayList<Label>();
	
	
	
	public MenuScreen(Game g) {
		game = g;
		create();
	}

	public void create() {
		camera = new OrthographicCamera();
		viewport = new ScreenViewport(camera);

		mainStage = new Stage();
		

		Gdx.input.setInputProcessor(mainStage);
		skin = new Skin(Gdx.files.internal("assets/skin/uiskin.json"));
	
		
		
		
		editUserButton.setPosition(PS.viewWidth - editUserButton.getWidth(), PS.viewHeight -editUserButton.getHeight());
		mainOverlay.addActor(editUserButton);
		
		singleplayerButton.setPosition(PS.viewWidth/2, PS.viewHeight/2 + 32);
		mainOverlay.addActor(singleplayerButton);
		
		multiplayerButton.setPosition(PS.viewWidth/2, PS.viewHeight/2 - 32 - 64);
		mainOverlay.addActor(multiplayerButton);
		mainStage.addActor(mainOverlay);
		setActiveOverlay(this.mainOverlay);
		
		userNameInput = new TextField("", skin);
		userNameInput.setPosition(PS.getViewWidth()/2, PS.getViewHeight()/2);
		editUserOverlay.addActor(userNameInput);
		userPrompt.setPosition(PS.getViewWidth()/2, PS.getViewHeight()/2 + 32);
		editUserOverlay.addActor(userPrompt);
		doneButton.setPosition(PS.getViewWidth()/2, PS.getViewHeight()/2 - 32 - 64);
		editUserOverlay.addActor(doneButton);
		
		mainStage.addActor(editUserOverlay);
		editUserOverlay.setVisible(false);
		
		joinGameButton.setPosition(PS.getViewWidth()/2, PS.getViewHeight()/2);
		multChoiceOverlay.addActor(joinGameButton);
		hostGameButton.setPosition(PS.getViewWidth()/2, PS.getViewHeight()/2 - 64 - 32);
		multChoiceOverlay.addActor(hostGameButton);
		
		ipInput = new TextField("ip here", skin);
		ipInput.setPosition(PS.viewWidth/2, PS.getViewHeight()/2 + 64 + 32);
		multChoiceOverlay.addActor(ipInput);
		
		labelBacking.addActor(this.feedbackLabel);
		labelBacking.setPosition(PS.viewWidth/2, PS.getViewHeight()/2 + 64 + 32 + 64);
		multChoiceOverlay.addActor(labelBacking);
		
		mainStage.addActor(multChoiceOverlay);
		multChoiceOverlay.setVisible(false);
		
		waitingRoomOverlay.addActor(this.waitRoomBackButton);
		portLabel.setPosition(PS.viewWidth - portLabel.getWidth(), PS.viewHeight - portLabel.getHeight());
		
		waitingRoomOverlay.addActor(this.portLabel);
		
		mainStage.addActor(waitingRoomOverlay);
		
		shapeRender = new ShapeRenderer();
		
		for (int i = 0; i < factionBoxes.length; i++) {
			factionBoxes[i] = new BaseActor(Faction.allFactions[i].getSymbolTexture());
			waitingRoomOverlay.addActor(factionBoxes[i]);
			factionBoxes[i].setPosition(25, PS.viewHeight - 25 * i);
		}
		
		beginButton.setPosition(PS.viewWidth-beginButton.getWidth(), 0);
		waitingRoomOverlay.addActor(beginButton);
		waitingRoomOverlay.setVisible(false);
	
	
	
		
		//TODO move to ui
		if (UserConfigHandler.canReadFile("assets/userConfig.json")) {
			String[] config = UserConfigHandler.readFile("assets/userConfig.json");
			GameServerClient.user = config[0];
		
			GameServerClient.setUpPlayer(Faction.Xin);
		} else {
			promptUserCreation = true;
		}
		
		if (promptUserCreation) {
			editUserOverlay.setVisible(true);
			mainStage.setKeyboardFocus(userNameInput);
		}
	
		
		
		mainStage.addActor(mouseBlot);
	
		//ipInput.setMessageText("test");
	
		
		mainStage.setKeyboardFocus(ipInput);
		
		
		System.out.println(ip);
		System.out.println(port);
		GameServerClient.setUpPlayer(Faction.Xin);
		
	}

	public void render(float dt) {
	
		mainStage.act(dt);
		
		
		mouseBlot.setCenter(Gdx.input.getX(), PS.viewHeight - Gdx.input.getY());

		Gdx.gl.glClearColor(0.0F, 0.0F, 0, 1);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		mainStage.draw();
		
		
		if (editUserButton.Pressed(mouseBlot.getBoundingRectangle()) && !editUserOverlay.isVisible()) {
			
			setActiveOverlay(this.editUserOverlay);
			mainStage.setKeyboardFocus(userNameInput);
		}
		
		else if (doneButton.Pressed(mouseBlot.getBoundingRectangle()) && !userNameInput.getText().isEmpty()) {
			setActiveOverlay(this.mainOverlay);
			UserConfigHandler.createUserConfig(userNameInput.getText());
			System.out.println(userNameInput.getText());
			mainStage.setKeyboardFocus(ipInput);
			GameServerClient.user = userNameInput.getText();
		}
	
		else if (multiplayerButton.Pressed(mouseBlot.getBoundingRectangle()) && !editUserOverlay.isVisible()) {
			
			
			setActiveOverlay(this.multChoiceOverlay);
		} else if (singleplayerButton.Pressed(mouseBlot.getBoundingRectangle())) {
			
			PS.useServer = false;
			this.game.setScreen(new GameplayScreen(this.game));
		} else if (this.joinGameButton.Pressed(mouseBlot.getBoundingRectangle())){
			boolean proceed = true;
			String ipText = ipInput.getText();
			System.out.println(ipText.substring(0, ipText.length()-5));
			String ip = ipText.substring(0, ipText.length()-5);
			String port = ipText.substring(ipText.length()-4, ipText.length());
			System.out.println(port);
			MenuScreen.ip = ip;
			MenuScreen.port = Integer.parseInt(port);
			 try {
				 GameServerClient testServerClient = new GameServerClient(MenuScreen.ip, MenuScreen.port);
				 testServerClient.dispose();
	            } catch (GdxRuntimeException e) {
	            	
	            	System.out.println("Could not connect to server");
	            	setFeedbackLabel("Could not connect to server");
	            	proceed = false;
	            	
	            }
			 if (proceed){
				PS.useServer = true;
				 this.setActiveOverlay(this.waitingRoomOverlay);
				 
				
				PS.client = new GameServerClient(MenuScreen.ip, MenuScreen.port);
				PS.client.sendRequest(GameServerClient.user, GameServer.ServerCommands.registerUser);
				
			 }
			 
			 
		} else if (this.hostGameButton.Pressed(mouseBlot.getBoundingRectangle())){
			boolean proceed = true;
			int port = MathUtils.random(1025, 9999);
			 try {
	            	ServerSocket testSocket = Gdx.net.newServerSocket(Protocol.TCP, port, null);
	            	testSocket.dispose();
	            } catch (GdxRuntimeException e) {
	            	setFeedbackLabel("port taken");
	            	proceed = false;
	            	
	            }
			 if (proceed){
				 MenuScreen.port = port;
					
				 portLabel.setText("Port: " + port);
				 PS.useServer = true;
				 this.setActiveOverlay(this.waitingRoomOverlay);
				 PS.isHost = true;
				 GameServer.Server.start();
				 try {
					 PS.client = new GameServerClient(Inet4Address.getLocalHost().getHostAddress(), port);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
			 }
			 
			 
		} else if (this.waitRoomBackButton.Pressed(mouseBlot.getBoundingRectangle())){
			this.setActiveOverlay(this.multChoiceOverlay);
			PS.client = null;
			if (GameServer.Server.isAlive()){
				//WARNING ERROR PRONE
				GameServer.Server.interrupt();
			}
		
		} else if (this.beginButton.Pressed(mouseBlot.getBoundingRectangle())){
			if (PS.isHost){
				PS.client.sendRequest("", GameServer.ServerCommands.beginGame);
				PS.client.sendRequest("", GameServer.ServerCommands.genWorld);
				this.game.setScreen(new GameplayScreen(this.game));
				
			}
			
		}
		
		
		
		if (this.activeOverlay.equals(this.waitingRoomOverlay)){
			//get the players connected
			PS.client.sendRequest("", GameServer.ServerCommands.getAllPlayers);
			if (GameServerClient.isCorrectDataType(PS.client.getRecievedData(), GameServerClient.ClientRecieveCommands.players)) {
				Json json = new Json();
				ArrayList<PlayerData> pds = json.fromJson(ArrayList.class, PS.client.getRecievedData().substring(0, PS.client.getRecievedData().length()-1));
				for (PlayerData pd : pds){
					if (!GameServerClient.players.contains(Player.fromPlayerData(pd))){
						GameServerClient.players.add(Player.fromPlayerData(pd));
					}
					
				}
				
				
			}
			
			PS.client.sendRequest("", GameServer.ServerCommands.askBeginGame);
			if (Boolean.parseBoolean(PS.client.getRecievedData())){
				this.game.setScreen(new GameplayScreen(this.game));
			}
			
			for (int i = 0; i< GameServerClient.players.size(); i++){
				for (Actor a : waitingRoomOverlay.getChildren()){
					if (a instanceof Label){
						String[] strs = (((Label) a).getText() + "").split(":");
						if (!strs[0].equals(GameServerClient.players.get(i).getUser())){
							Label l = new Label(GameServerClient.players.get(i).getUser() + ": " + GameServerClient.players.get(i).faction.name, PS.font);
							l.setPosition(PS.viewWidth/2, i * 16);
							waitingRoomOverlay.addActor(l);
						}
					}
				}
				
			}
			
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
	
	public void setActiveOverlay(BaseActor b){
		activeOverlay.setVisible(false);
		for (Actor a : activeOverlay.getChildren()){
			if (a instanceof Button){
				((Button) a).active = false;
			}
		}
		activeOverlay = b;
		for (Actor a : activeOverlay.getChildren()){
			if (a instanceof Button){
				((Button) a).active = true;
			}
		}
		activeOverlay.setVisible(true);
	}
	
	public void setFeedbackLabel(String text){
		this.labelBacking.addAction(Actions.fadeIn(0));
		this.feedbackLabel.setText(text);
		this.labelBacking.addAction(Actions.fadeOut(3));
	}

	

}
