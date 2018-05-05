package com.ue.ps.systems;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class DebugLog {
	
	private static ArrayList<String> log = new ArrayList<String>();
	
	public static void log(String s) {
		log.add(s);
	}
	
	
	public static void output() {
		
		try {
			FileHandle f = Gdx.files.local("logs/log_" + LocalDateTime.now().toString() + ".txt");
			// Write Content

			for (String s : log) {
				f.writeString(s + "/n", false);
			}
	
			
		}


		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
