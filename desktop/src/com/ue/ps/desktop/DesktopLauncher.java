package com.ue.ps.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ue.ps.PS;

public class DesktopLauncher {
	
	public static boolean directToGame = true;
	public static void main (String[] arg) {
		PS theGame = new PS();
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Planetary Strategery";
		cfg.height = PS.viewHeight;
		cfg.width =  PS.viewWidth;
	
		cfg.addIcon("assets/icon.png", FileType.Internal);

		for (int i = 0; i < arg.length - 1; i++){
			System.out.println(arg[i]);
			if (arg[i].equals("recording") || arg[i].equals("r")){
				PS.recordingGeneration = true;
			}
			else if (arg[i].equals("menu") || arg[i].equals("m")){
				directToGame = false;
				System.out.println("to the menu!");
			}
		}
	
		LwjglApplication launcher = new LwjglApplication(theGame, cfg);
	}
}
