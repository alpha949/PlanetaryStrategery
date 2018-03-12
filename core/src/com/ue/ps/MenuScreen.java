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
	
	private TextFieldStyle style = new TextFieldStyle(PS.theFont, Color.WHITE, null, null, null);
	private TextField ipInput;
	
	

	private Skin skin = new Skin();
	
	

	
	public MenuScreen(Game g) {
		game = g;
		create();
	}

	public void create() {
		camera = new OrthographicCamera();
		viewport = new ScreenViewport(camera);

		mainStage = new Stage(new ScalingViewport(Scaling.fill, PS.viewWidth, PS.viewHeight, camera));
		

	
		skin = new Skin(Gdx.files.internal("assets/skin/uiskin.json"));
		TextFieldStyle style = new TextFieldStyle();
        style.background = skin.getDrawable("textfield");
        style.selection = skin.getDrawable("selection");
        style.fontColor = new Color(0.41f, 0.75f, 0.99f, 1f);
        style.font = PS.theFont;
        style.disabledFontColor = new Color(0.79f, 0.79f, 0.79f, 0.1f);
        style.cursor = skin.getDrawable("cursor");
		
		ipInput = new TextField("ip here", style);
		ipInput.setPosition(100, 100);
		
		mainStage.addActor(ipInput);

		shapeRender = new ShapeRenderer();
		
	
		Gdx.input.setInputProcessor(new InputProcess());
		
		//TODO move to ui
		String[] config = ConfigReader.readFile("assets/config.json");
		
		
		mainStage.addActor(mouseBlot);
		
		//ipInput.setMessageText("test");
		Gdx.input.setInputProcessor(mainStage);
		ipInput.setTouchable(Touchable.enabled);
		ipInput.setTextFieldListener(new TextFieldListener() {

			@Override
			public void keyTyped(TextField textField, char c) {
				System.out.println(c);
				
			}
			
		});
	}

	public void render(float dt) {
	
		mainStage.act(dt);
		
		
	
		
		
	
	
		OrthographicCamera cam = (OrthographicCamera) mainStage.getCamera();
	

		cam.update();
	
	


		mouseBlot.setCenter(Gdx.input.getX(), PS.viewHeight - Gdx.input.getY());

		Gdx.gl.glClearColor(0.0F, 0.0F, 0, 1);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		mainStage.getViewport().apply();
		mainStage.draw();
	
	
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.input.setCursorCatched(false);
		} else {
			Gdx.input.setCursorCatched(true);
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.SHIFT_LEFT)) {
			this.game.setScreen(new GameplayScreen(this.game));
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
