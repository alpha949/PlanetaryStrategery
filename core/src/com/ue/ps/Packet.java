package com.ue.ps;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

public class Packet {
	
	//format: [7:(s,4/d,2){m,}];[5:f,1] 
	//        [planetId:(ShipId,count/ShipId,count){}];[planetId:ShipId,count]
	

	
	private ArrayList<Action> actions = new ArrayList<Action>();
	private Json jsonData = new Json();
	private String data;
	
	
	public Packet() {
		
	}
	
	
	
	
	private void resolveData() {
		actions = (ArrayList<Action>) jsonData.fromJson(ArrayList.class, data);
	}
	
	private String compactData() {
		data = jsonData.toJson(actions);
		return data;
	
	}
	
	public void addAction(Action a) {
		actions.add(a);
	}
	
	public void setData(String data) {
		this.data = data;
		resolveData();
	}
	
	public ArrayList<Action> getActions(){
		return this.actions;
	}
	
	public void send(String ip) {
		 String textToSend = compactData();
       
        textToSend += "\n";
      
       
       SocketHints socketHints = new SocketHints();
       // Socket will time our in 4 seconds
       socketHints.connectTimeout = 4000;
       //create the socket and connect to the server entered in the text box ( x.x.x.x format ) on port 9021
       try {
       	  Socket socket = Gdx.net.newClientSocket(Protocol.TCP, ip, 6001, socketHints);
       	  socket.getOutputStream().write(textToSend.getBytes());
       	  
       } catch (GdxRuntimeException e) {
       	System.out.println("Could not connect to: " + ip);
       } catch (IOException e) {
           e.printStackTrace();
       }
     
      
	}
	
	
	
	
}
