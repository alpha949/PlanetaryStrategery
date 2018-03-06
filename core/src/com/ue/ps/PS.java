package com.ue.ps;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class PS extends Game {
	public static final int viewWidth = 1280;
	public static final int viewHeight = 750;

	public static final float aspectRatio = (float) viewWidth / (float) viewHeight;
	public static int mapWidth = 1280 * 2;
	public static int mapHeight = 1280 * 2;
	public static Rectangle viewBorder;
	public static LabelStyle font;
	public static boolean paused;
	public static boolean recordingGeneration;
	
	public static Player p1;
	public static Player p2;
	public static Player[] allPlayers = new Player[2];
	public static BitmapFont theFont;
	
	public static boolean directToGame = true;
	
	public static Boolean useServer;
	
	@Override
	public void create() {
		// viewBorder = new Rectangle(1, 0, PS.viewWidth-1, PS.viewHeight-220);
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/AndromedaTV.TTF"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 12;
		 theFont = generator.generateFont(parameter); // font size 12
																// pixels
		generator.dispose(); // don't forget to dispose to avoid memory leaks!
		
		//Read user's data;
		String[] config = ConfigReader.readFile("assets/config.json");
		
		useServer = Boolean.parseBoolean(config[3]);
		GameServerClient.clientUser.setUserName(config[0]);
		GameServerClient.setUpPlayer(Faction.Xin);

		font = new LabelStyle(new BitmapFont(), Color.WHITE);
		p2 = new Player(new User("siv"), Faction.Efferent);
		allPlayers[0] = p2;
		allPlayers[1] = GameServerClient.clientPlayer;
		
		GameplayScreen gs = new GameplayScreen(this);
		//MenuScreen ms = new MenuScreen(this);
		if (directToGame) {
			setScreen(gs);
		} else {
			//setScreen(ms);
		}
		
	}

	public static int getViewHeight() {
		return viewHeight;
	}

	public static int getViewWidth() {

		return viewWidth;
	}
}
