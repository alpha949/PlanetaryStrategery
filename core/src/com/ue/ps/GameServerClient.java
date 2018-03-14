package com.ue.ps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.HttpRequestHeader;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

public class GameServerClient {
	
	public static String userIpAddress;

	
	
	public static Player clientPlayer;
	public static User clientUser = new User("");
	

	
	private String receivedData = "";
	private String connectionResult = "";
	private String ip;
	private int port;



	public static Packet packet = new Packet();
	

	
	
	
	private Socket socket;

	
	/**
	 * Creates a severClient connection to the given ip;
	 * this will only throw errors when calling sendRequest if the ip can't be found
	 * @param ip the ip to connect to
	 */
	public GameServerClient(String ip, int port){
		this.ip = ip;
		this.port = port;
		SocketHints socketHints = new SocketHints();
        // Socket will time our in 4 seconds
        socketHints.connectTimeout = 4000;
        //create the socket and connect to the server entered in the text box ( x.x.x.x format ) on port 9021
        socket = Gdx.net.newClientSocket(Protocol.TCP, ip, port, socketHints);
        this.receive.start();
	        
	}
	
	
	


	
	
	/**
	 * registers a player with the server, should only be called on first connection
	 * @param p the player to register
	 */
	public void registerUser(User p){
		Json j = new Json();
		j.setOutputType(OutputType.json);
		
		StringWriter sw = new StringWriter();
		j.setWriter(sw);
		j.writeObjectStart();
		j.writeValue("username", p.getUserName());
		j.writeObjectEnd();
		
		
		this.sendRequest(sw.toString(), GameServer.ServerCommands.registerUser);
	
		
		
	}
	/**
	 * sends data to the server
	 * @param data the data to be sent
	 * @param com the command for the server to perform
	 */
	public void sendRequest(String jsonStringToSend, GameServer.ServerCommands com){
		
		jsonStringToSend += com.getId() + "\n";
		
		
		 System.out.println("sending " + com.name() + " request: " + jsonStringToSend);
         try {
             // write our entered message to the stream
        	
             socket.getOutputStream().write(jsonStringToSend.getBytes());
         } catch (Exception e) {
             e.printStackTrace();
         }
		
		
	
	}
	
	private Thread receive = new Thread(new Runnable() {
		
		@Override
		public void run() {

			String data = "";
			String prevData = "";
			while (true) {
				 BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
				
					try {
						
						if (buffer.ready()) {
							data = buffer.readLine();
							
							
						}
					} catch (IOException e) {
						System.out.println("IO Exception");
						data = "error";
					}
					
					final String finalData = data;
					if (!data.equals(prevData)) {
						System.out.println("Recieved: " + data);
					}
					prevData = data;
					
					Gdx.app.postRunnable(new Runnable() {
				         @Override
				         public void run() {
				        	
				            // process the result, e.g. add it to an Array<Result> field of the ApplicationListener.
				        	 receivedData = finalData;
				         }
				      });
			}
			
			
		}
		
	});
	
	/**
	 * gets the data received from the server, may be an empty string
	 * @return String the data recieve from the server
	 */
	public String getRecievedData(){
	
		return this.receivedData;
	}
	
	public Planet constructPlanet(String validJsonData) {
		Json j = new Json();
		PlanetData planetData = j.fromJson(PlanetData.class, validJsonData);
		Planet p = new Planet(planetData.type, planetData.size);
		p.setPosition(planetData.x, planetData.y);
		p.isHomePlanet = planetData.isHomePlanet;
		return p;
	}
	
	public static void setUpPlayer(Faction f) {
		clientPlayer = new Player(clientUser, f);
	}
	
}
