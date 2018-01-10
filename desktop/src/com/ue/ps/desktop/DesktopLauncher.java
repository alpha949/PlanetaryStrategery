package com.ue.ps.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ue.ps.PS;

public class DesktopLauncher {
	public static void main (String[] arg) {
		PS theGame = new PS();
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Planetary Strategery";
		cfg.height = PS.viewHeight;
		cfg.width =  PS.viewWidth;
	
		cfg.addIcon("assets/icon.png", FileType.Internal);
	
		LwjglApplication launcher = new LwjglApplication(theGame, cfg);
	}
}
