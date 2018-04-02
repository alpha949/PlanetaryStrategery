package com.ue.ps.ui;

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
import com.ue.ps.BaseActor;
import com.ue.ps.Faction;
import com.ue.ps.PS;
import com.ue.ps.Player;
import com.ue.ps.PlayerData;
import com.ue.ps.systems.GameServer;
import com.ue.ps.systems.GameServer.ServerCommands;
import com.ue.ps.systems.GameServerClient;
import com.ue.ps.systems.UserConfigHandler;
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

	private Button[] factionBoxes = new Button[Faction.allFactions.length];

	private BaseActor activeOverlay = new BaseActor();
	private BaseActor labelBacking = new BaseActor(Images.emptyTexture);
	private Label feedbackLabel = new Label("", PS.font);

	private Label portLabel = new Label("Port: 0000", PS.font);

	private ArrayList<Label> inRoomUsers = new ArrayList<Label>();
	private Json jsonHandler = new Json();
	
	private Label[] connectedPlayers = new Label[6];

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

		editUserButton.setPosition(PS.viewWidth - editUserButton.getWidth(), PS.viewHeight - editUserButton.getHeight());
		mainOverlay.addActor(editUserButton);

		singleplayerButton.setPosition(PS.viewWidth / 2, PS.viewHeight / 2 + 32);
		mainOverlay.addActor(singleplayerButton);

		multiplayerButton.setPosition(PS.viewWidth / 2, PS.viewHeight / 2 - 32 - 64);
		mainOverlay.addActor(multiplayerButton);
		mainStage.addActor(mainOverlay);
		setActiveOverlay(this.mainOverlay);

		userNameInput = new TextField("", skin);
		userNameInput.setPosition(PS.getViewWidth() / 2, PS.getViewHeight() / 2);
		editUserOverlay.addActor(userNameInput);
		userPrompt.setPosition(PS.getViewWidth() / 2, PS.getViewHeight() / 2 + 32);
		editUserOverlay.addActor(userPrompt);
		doneButton.setPosition(PS.getViewWidth() / 2, PS.getViewHeight() / 2 - 32 - 64);
		editUserOverlay.addActor(doneButton);

		mainStage.addActor(editUserOverlay);
		editUserOverlay.setVisible(false);

		joinGameButton.setPosition(PS.getViewWidth() / 2, PS.getViewHeight() / 2);
		multChoiceOverlay.addActor(joinGameButton);
		hostGameButton.setPosition(PS.getViewWidth() / 2, PS.getViewHeight() / 2 - 64 - 32);
		multChoiceOverlay.addActor(hostGameButton);

		ipInput = new TextField("ip here", skin);
		ipInput.setPosition(PS.viewWidth / 2, PS.getViewHeight() / 2 + 64 + 32);
		multChoiceOverlay.addActor(ipInput);

		labelBacking.addActor(this.feedbackLabel);
		labelBacking.setPosition(PS.viewWidth / 2, PS.getViewHeight() / 2 + 64 + 32 + 64);
		multChoiceOverlay.addActor(labelBacking);

		mainStage.addActor(multChoiceOverlay);
		multChoiceOverlay.setVisible(false);

		waitingRoomOverlay.addActor(this.waitRoomBackButton);
		portLabel.setPosition(PS.viewWidth - portLabel.getWidth(), PS.viewHeight - portLabel.getHeight());

		waitingRoomOverlay.addActor(this.portLabel);

		mainStage.addActor(waitingRoomOverlay);

		shapeRender = new ShapeRenderer();

		for (int i = 0; i < factionBoxes.length; i++) {
			factionBoxes[i] = new Button(Faction.allFactions[i].getSymbolTexture());
			waitingRoomOverlay.addActor(factionBoxes[i]);
			factionBoxes[i].setPosition(25, PS.viewHeight - 25 * i);
		}

		beginButton.setPosition(PS.viewWidth - beginButton.getWidth(), 0);
		waitingRoomOverlay.addActor(beginButton);
		for (int i = 0; i < connectedPlayers.length; i++) {
			connectedPlayers[i] = new Label("nobody", PS.font);
			connectedPlayers[i].setPosition(100, PS.viewHeight - 100 - 18 * i);
			waitingRoomOverlay.addActor(connectedPlayers[i]);
		}
		waitingRoomOverlay.setVisible(false);

		// TODO move to ui
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

		// ipInput.setMessageText("test");

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
		//check if edit user pressed
		if (editUserButton.Pressed(mouseBlot.getBoundingRectangle()) && !editUserOverlay.isVisible()) {
			//set overlay
			setActiveOverlay(this.editUserOverlay);
			//move keyboard focus to username input
			mainStage.setKeyboardFocus(userNameInput);
		}
		//check if edit user done button pressed
		else if (doneButton.Pressed(mouseBlot.getBoundingRectangle()) && !userNameInput.getText().isEmpty()) {
			//set overlay back to main screen
			setActiveOverlay(this.mainOverlay);
			//create new user config with inputed data
			UserConfigHandler.createUserConfig(userNameInput.getText());
			//update client user
			GameServerClient.user = userNameInput.getText();
		}
		//check if multiplayer button pressed
		else if (multiplayerButton.Pressed(mouseBlot.getBoundingRectangle()) && !editUserOverlay.isVisible()) {
			//set overlay
			setActiveOverlay(this.multChoiceOverlay);
		} else if (singleplayerButton.Pressed(mouseBlot.getBoundingRectangle())) {
			//change to game screen without server
			PS.useServer = false;
			
			this.game.setScreen(new GameplayScreen(this.game));
		//check if join button pressed in mult select
		} else if (this.joinGameButton.Pressed(mouseBlot.getBoundingRectangle())) {
			boolean proceed = true;
			//get ip and port
			String ipText = ipInput.getText();
			System.out.println(ipText.substring(0, ipText.length() - 5));
			String ip = ipText.substring(0, ipText.length() - 5);
			String port = ipText.substring(ipText.length() - 4, ipText.length());
			System.out.println(port);
			//update ip and port
			MenuScreen.ip = ip;
			MenuScreen.port = Integer.parseInt(port);
			//test for valid connection
			try {
				GameServerClient testServerClient = new GameServerClient(MenuScreen.ip, MenuScreen.port);
				testServerClient.dispose();
			} catch (GdxRuntimeException e) {
				//feedback
				System.out.println("Could not connect to server");
				setFeedbackLabel("Could not connect to server");
				proceed = false;

			}
			if (proceed) {
				//go to waiting room
				PS.useServer = true;
				this.setActiveOverlay(this.waitingRoomOverlay);
				//register client
				PS.client = new GameServerClient(MenuScreen.ip, MenuScreen.port);
				PS.client.sendRequest(GameServerClient.user, GameServer.ServerCommands.registerUser);

			}
		//check for host game button pressed in mult select
		} else if (this.hostGameButton.Pressed(mouseBlot.getBoundingRectangle())) {
			boolean proceed = true;
			//get random port
			int port = MathUtils.random(1025, 9999);
			//test for port being used
			try {
				ServerSocket testSocket = Gdx.net.newServerSocket(Protocol.TCP, port, null);
				testSocket.dispose();
			} catch (GdxRuntimeException e) {
				//feedback
				setFeedbackLabel("port taken");
				proceed = false;

			}
			if (proceed) {
				//update port
				MenuScreen.port = port;
				//disp port
				portLabel.setText("Port: " + port);
				//go to waiting room
				PS.useServer = true;
				this.setActiveOverlay(this.waitingRoomOverlay);
				PS.isHost = true;
				//start server
				GameServer.Server.start();
				//set host client
				try {
					System.out.println(Inet4Address.getLocalHost().getHostAddress());
					PS.client = new GameServerClient(Inet4Address.getLocalHost().getHostAddress(), port);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//register the host
				GameServerClient.setUpPlayer(Faction.Xin);
				PS.client.sendRequest(jsonHandler.toJson(GameServerClient.clientPlayer.toPlayerData()), ServerCommands.registerUser);
				

			}
		//check if player leaves waiting room
		} else if (this.waitRoomBackButton.Pressed(mouseBlot.getBoundingRectangle())) {
			//go back to mult choice
			this.setActiveOverlay(this.multChoiceOverlay);
			PS.client = null;
			//close server
			if (GameServer.Server.isAlive()) {
				// WARNING ERROR PRONE
				GameServer.Server.interrupt();
			}
		//check if host starts game
		} else if (this.beginButton.Pressed(mouseBlot.getBoundingRectangle())) {
			if (PS.isHost) {
				PS.client.sendRequest("", GameServer.ServerCommands.beginGame);
			
				this.game.setScreen(new GameplayScreen(this.game));

			}

		}

		if (this.activeOverlay.equals(this.waitingRoomOverlay)) {
			// get the players connected
			PS.client.sendRequest("", GameServer.ServerCommands.getAllPlayers);
			if (GameServerClient.isCorrectDataType(PS.client.getRecievedData(), GameServerClient.ClientRecieveCommands.players)) {
				
				ArrayList<PlayerData> pds = jsonHandler.fromJson(ArrayList.class,
						PS.client.getRecievedData().substring(0, PS.client.getRecievedData().length() - 1));
				for (PlayerData pd : pds) {
					boolean canConnect = true;
					//check for valid player
					for (Player p : GameServerClient.players) {
						if (p.getUser().equals(pd.username)) {
							canConnect = false;
						}
					}
					if (canConnect) {
						GameServerClient.players.add(Player.fromPlayerData(pd));
						System.out.println(Player.fromPlayerData(pd).getUser() + " has connected!");
					}

				}

			}
			//check if game has started
			PS.client.sendRequest("", GameServer.ServerCommands.askBeginGame);
			if (Boolean.parseBoolean(PS.client.getRecievedData())) {
				this.game.setScreen(new GameplayScreen(this.game));
			}
			//display connected players
			System.out.println(GameServerClient.players.size());
			for (int i = 0; i < GameServerClient.players.size(); i++) {
				connectedPlayers[i].setText(GameServerClient.players.get(i).getUser() + ": " + GameServerClient.players.get(i).faction.name);

			}
			//update faction
			for (int i = 0; i < factionBoxes.length; i++) {
				if (factionBoxes[i].Pressed(mouseBlot.getBoundingRectangle())) {
					GameServerClient.setUpPlayer(Faction.allFactions[i]);
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

	public void setActiveOverlay(BaseActor b) {
		activeOverlay.setVisible(false);
		for (Actor a : activeOverlay.getChildren()) {
			if (a instanceof Button) {
				((Button) a).active = false;
			}
		}
		activeOverlay = b;
		for (Actor a : activeOverlay.getChildren()) {
			if (a instanceof Button) {
				((Button) a).active = true;
			}
		}
		activeOverlay.setVisible(true);
	}

	public void setFeedbackLabel(String text) {
		this.labelBacking.addAction(Actions.fadeIn(0));
		this.feedbackLabel.setText(text);
		this.labelBacking.addAction(Actions.fadeOut(3));
	}

}
