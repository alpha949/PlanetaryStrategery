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
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.net.HttpRequestHeader;

public class GameServerClient {
	
	public static String userIpAddress;

	
	
	public static Player clientPlayer = new Player(Faction.Xin);
	
	private HttpRequest http = new HttpRequest(HttpMethods.GET);
	
	private String recievedData = "";
	private String connectionResult = "";
	private String ip;
	/**
	 * Creates a severClient connection to the given ip;
	 * this will only throw errors when calling sendRequest if the ip can't be found
	 * @param ip the ip to connect to
	 */
	public GameServerClient(String ip){
		this.ip = ip;
		

	        
	}
	
	
	


	
	
	/**
	 * registers a player with the server, should only be called on first connection
	 * @param p the player to register
	 */
	public void registerUser(Player p){
		Json j = new Json();
		j.setOutputType(OutputType.json);
		
		StringWriter sw = new StringWriter();
		j.setWriter(sw);
		j.writeObjectStart();
		j.writeValue("username", p.userName);
		j.writeObjectEnd();
		
		
		this.sendRequest(sw.toString(), "client");
		
		if (!this.getRecievedData().isEmpty()){
			p.clientToken = this.getRecievedData();
		}
		
		
	}
	/**
	 * sends data to the server
	 * @param data the data to be sent
	 * @param endpoint the endpoint to send the data to, usually client
	 */
	public void sendRequest(String data, String endpoint){
		http.setUrl(this.ip + "/" + endpoint);
	
		if (!data.isEmpty()){
			http.setMethod(HttpMethods.POST);
			http.setContent(data);
			http.setHeader(HttpRequestHeader.ContentType, "application/json");
		} else {
			http.setContent("");
			http.setMethod(HttpMethods.GET);
			
		}
		this.recievedData = "";
	
		Thread severConnector = new Thread(new Runnable(){
            @Override
            public void run() {
            	Gdx.net.sendHttpRequest (http, new HttpResponseListener() {
			        public void handleHttpResponse(HttpResponse httpResponse) {
			        	connectionResult = "success";
			        	recievedData = httpResponse.getResultAsString();
			                //do stuff here based on response
			        }
			 
			        public void failed(Throwable t) {
			        	connectionResult = "failed";
			           //do stuff here based on the failed attempt
			        }

					@Override
					public void cancelled() {
						connectionResult = "cancelled";
						// TODO Auto-generated method stub
						
					}
			
			
            	});
            	
            }
            	
        });
		severConnector.start();
		
		
		
	
	}
	/**
	 * gets the data recieved from the server, may be an empty string
	 * @return String the data recieve from the server
	 */
	public String getRecievedData(){
		if (connectionResult.equals("success")){
			return recievedData;
		} else {
			System.out.println("No valid data to obtain");
			return "";
		}
	}
	
	
}
