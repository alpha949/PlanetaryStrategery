package com.ue.ps;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class FileReader {
	
	private static JsonReader reader = new JsonReader();
	public static String[] readFile(String file){
		String username = "";
		String serverip = "";
		String serverport = "";
		try {
			JsonValue value = reader.parse(file);
			username = value.getString("username");
			serverip = value.getString("server_ip");
			serverport = value.getString("server_port");
			
		} catch(GdxRuntimeException e){
			System.out.println("Couldn't open: " + file);
		}
		
		String[] output = {username, serverip, serverport};
		return output;
	}
	
}
