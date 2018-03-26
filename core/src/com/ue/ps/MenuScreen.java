package com.ue.ps;



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
	private BaseActor beginButton = new BaseActor();
	
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
	private BaseActor grayBack = new BaseActor(Images.getImg("grayBack"));
	private TextField userNameInput;
	private Label userPrompt = new Label("Enter User Name:", PS.font);
	
	private String selectedFaction = "Xin";
	
	private Label[] factionNames = new Label[Faction.allFactions.length];
	
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
	
		
		ipInput = new TextField("ip here", skin);
		ipInput.setPosition(100, 100);
		mainStage.addActor(ipInput);
		
		editUserButton.setPosition(PS.viewWidth - editUserButton.getWidth(), PS.viewHeight -editUserButton.getHeight());
		mainStage.addActor(editUserButton);
		
		singleplayerButton.setPosition(PS.viewWidth/2, PS.viewHeight/2 + 32);
		mainStage.addActor(singleplayerButton);
		
		multiplayerButton.setPosition(PS.viewWidth/2, PS.viewHeight/2 - 32 - 64);
		mainStage.addActor(multiplayerButton);
		
		userNameInput = new TextField("", skin);
		userNameInput.setPosition(PS.getViewWidth()/2, PS.getViewHeight()/2);
		grayBack.addActor(userNameInput);
		userPrompt.setPosition(PS.getViewWidth()/2, PS.getViewHeight()/2 + 32);
		grayBack.addActor(userPrompt);
		doneButton.setPosition(PS.getViewWidth()/2, PS.getViewHeight()/2 - 32 - 64);
		grayBack.addActor(doneButton);
		
		mainStage.addActor(grayBack);
		grayBack.setVisible(false);
		
		shapeRender = new ShapeRenderer();
		
		for (int i = 0; i < factionNames.length; i++) {
			factionNames[i] = new Label(Faction.allFactions[i].name, PS.font);
			mainStage.addActor(factionNames[i]);
			factionNames[i].setPosition(25, PS.viewHeight - 25 * i);
		}
	
	
	
		
		//TODO move to ui
		if (UserConfigHandler.canReadFile("assets/userConfig.json")) {
			String[] config = UserConfigHandler.readFile("assets/userConfig.json");
			GameServerClient.user = config[0];
		
			GameServerClient.setUpPlayer(Faction.Xin);
		} else {
			promptUserCreation = true;
		}
		
		if (promptUserCreation) {
			grayBack.setVisible(true);
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
		shapeRender.begin(ShapeType.Line);
		shapeRender.setColor(Color.GREEN);
		shapeRender.rect(fSelectBox.x, fSelectBox.y, fSelectBox.width, fSelectBox.height);
		shapeRender.end();
		
		if (editUserButton.Pressed(mouseBlot.getBoundingRectangle())) {
			grayBack.setVisible(true);
			mainStage.setKeyboardFocus(userNameInput);
		}
		
		if (doneButton.Pressed(mouseBlot.getBoundingRectangle()) && !userNameInput.getText().isEmpty()) {
			grayBack.setVisible(false);
			UserConfigHandler.createUserConfig(userNameInput.getText());
			System.out.println(userNameInput.getText());
			mainStage.setKeyboardFocus(ipInput);
			GameServerClient.user = userNameInput.getText();
		}
	
		if (multiplayerButton.Pressed(mouseBlot.getBoundingRectangle()) && !ipInput.getText().isEmpty()) {
			PS.useServer = true;
			String serverIp = ipInput.getText();
			String[] data = serverIp.split(":");
			ip = data[0];
			port = Integer.parseInt(data[1]);
			this.game.setScreen(new GameplayScreen(this.game));
		} else if (singleplayerButton.Pressed(mouseBlot.getBoundingRectangle())) {
			
			PS.useServer = false;
			this.game.setScreen(new GameplayScreen(this.game));
		}
		
		for (Label l : factionNames) {
			if (new Rectangle(l.getX(), l.getY(), l.getWidth(), l.getHeight()).contains(mouseBlot.center)) {
				l.setColor(Color.RED);
				if (Gdx.input.justTouched()) {
					l.setColor(Color.GREEN);
					selectedFaction = l.getText() + "";
					System.out.println(selectedFaction);
					fSelectBox = new Rectangle(l.getX(), l.getY(), l.getWidth(), l.getHeight());
				}
			} else {
				if (l.getText() + "" != selectedFaction) {
					l.setColor(Color.WHITE);
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

	

}
