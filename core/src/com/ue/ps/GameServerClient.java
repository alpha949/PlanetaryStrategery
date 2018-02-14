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
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;

public class GameServerClient {
	
	public static String ipAddress;
	public String message;
	
	private ArrayList<Player> allPlayers = new ArrayList<Player>();
	
	public GameServerClient(){
		
		
		 List<String> addresses = new ArrayList<String>();
	        try {
	            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
	            for(NetworkInterface ni : Collections.list(interfaces)){
	                for(InetAddress address : Collections.list(ni.getInetAddresses()))
	                {
	                    if(address instanceof Inet4Address){
	                        addresses.add(address.getHostAddress());
	                    }
	                }
	            }
	        } catch (SocketException e) {
	            e.printStackTrace();
	        }
	        
	        // Print the contents of our array to a string.  Yeah, should have used StringBuilder
	        ipAddress = new String("");
	        for(String str:addresses)
	        {
	            ipAddress = ipAddress + str + "\n";
	        }
	        
	        
	     // Now we create a thread that will listen for incoming socket connections
	     /*   new Thread(new Runnable(){

	            @Override
	            public void run() {
	                ServerSocketHints serverSocketHint = new ServerSocketHints();
	                // 0 means no timeout.  Probably not the greatest idea in production!
	                serverSocketHint.acceptTimeout = 0;
	                
	                // Create the socket server using TCP protocol and listening on 9021
	                // Only one app can listen to a port at a time, keep in mind many ports are reserved
	                // especially in the lower numbers ( like 21, 80, etc )
	                ServerSocket serverSocket = Gdx.net.newServerSocket(Protocol.TCP, 6001, serverSocketHint);
	                Socket socket;
	                // Loop forever
	                while(true){
	                	
	                	
	                    // Create a socket
	                	try{
	                		  socket = serverSocket.accept(null);
	                	} catch(GdxRuntimeException e){
	                		continue;
	                	}
	                  
	                    
	                    // Read data from the socket into a BufferedReader
	                    BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
	                    System.out.println("Connection Successful from: " + socket.getRemoteAddress());
	                    try {
	                        // Read to the next newline (\n) and display that text on labelMessage
	                    
                    		String line = buffer.readLine();  
                    		GameplayScreen.packet.setData(line);
                    		socket.dispose();
	                    	  
	                    	
	                    
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                }
	            }
	        }).start(); // And, start the thread running*/
	}
	
	public String getMessage() {
		return this.message;
	}
	
	
	
	public void send(String s, String ip, int port) {
		 String textToSend = s;
       
        textToSend += "\n";
      
       
       SocketHints socketHints = new SocketHints();
       // Socket will time our in 4 seconds
       socketHints.connectTimeout = 4000;
       //create the socket and connect to the server entered in the text box ( x.x.x.x format ) on port 9021
       try {
       	  Socket socket = Gdx.net.newClientSocket(Protocol.TCP, ip, port, socketHints);
       	  socket.getOutputStream().write(textToSend.getBytes());
       	  
       } catch (GdxRuntimeException e) {
       	System.out.println("Could not connect to: " + ip);
       } catch (IOException e) {
           e.printStackTrace();
       }
     
      
	}
	
	public void registerUser(Player p, String ip, int port){
		Json j = new Json();
		
		StringWriter sw = new StringWriter();
		j.setWriter(sw);
		j.writeObjectStart();
		j.writeValue("username", p.userName);
		j.writeObjectEnd();
		
		
		System.out.println(sw.toString());
		
		this.send(sw.toString(), ip, port);
	}
	
}
