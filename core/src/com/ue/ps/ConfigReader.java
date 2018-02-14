package com.ue.ps;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class ConfigReader {
	
	private static JsonReader reader = new JsonReader();
	
	/**
	 * reads a config file and returns a list of strings:
	 * username, ip, port, useServer
	 * @param file
	 * @return
	 */
	
	public static String[] readFile(String file){
		String username = "";
		String serverip = "";
		String serverport = "";
		String useServer = "";
		
		

        // This will reference one line at a time
        String line = null;
        String json = "";

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(file);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
               json += line;
            }   

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                file + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + file + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
		
		try {
			JsonValue value = reader.parse(json);
		
			username = value.getString("username");
			serverip = value.getString("server_ip");
			serverport = value.getString("server_port");
			useServer = value.getString("use_server");
			
			
		} catch(GdxRuntimeException e){
			System.out.println("Couldn't open: " + file);
		}
		
		String[] output = {username, serverip, serverport, useServer};
		return output;
	}
	
	
	
	
}
