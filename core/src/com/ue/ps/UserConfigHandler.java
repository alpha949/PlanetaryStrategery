package com.ue.ps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

public class UserConfigHandler {
	
	private static JsonReader reader = new JsonReader();
	private static Json jsonHandler = new Json();
	
	/**
	 * reads a config file and returns a list of strings:
	 * username, ip, port, useServer
	 * @param file
	 * @return
	 */
	
	public static String[] readFile(String file){
		String username = "";
	
		
		

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
	
			
			
		} catch(GdxRuntimeException e){
			System.out.println("Couldn't open: " + file);
		}
		
		String[] output = {username};
		return output;
	}
	
	public static boolean canReadFile(String file){
		 try {
	            // FileReader reads text files in the default encoding.
	            FileReader fileReader = 
	                new FileReader(file);

	            // Always wrap FileReader in BufferedReader.
	            BufferedReader bufferedReader = 
	                new BufferedReader(fileReader);

	          

	            // Always close files.
	            bufferedReader.close();  
	        	return true;
	        }
	        catch(FileNotFoundException ex) {
	        	return false;              
	        } catch (IOException e) {
				// TODO Auto-generated catch block
	        	return false;
			}
	}
	
	public static void createUserConfig(String userName){
		FileHandle userConfig;
	
		  
		//Create the file
		
		try {
			userConfig = Gdx.files.local("assets/userConfig.json");
			
			StringWriter sw = new StringWriter();
			jsonHandler.setOutputType(OutputType.json);
			jsonHandler.setWriter(sw);
			jsonHandler.writeObjectStart();
			jsonHandler.writeValue("username", userName);
			jsonHandler.writeObjectEnd();
			userConfig.writeString(sw.toString(), false);
		}
		 
		//Write Content
	
		 catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
	
	
	
}
