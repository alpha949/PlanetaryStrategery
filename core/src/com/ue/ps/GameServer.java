package com.ue.ps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.SerializationException;


/*Server Command strings:
 * 
 * 
 */
public class GameServer {
	  static ArrayList<Socket> connectedSockets = new ArrayList<Socket>();
	  static ArrayList<String> comQueue = new ArrayList<String>();
	public enum ServerCommands{
		registerUser, recieveActions, getWorld, initConnect, getAllActions, genWorld, getAllPlayers;
		
		private char id;
		
		public static ServerCommands getServerCommandById(char id){
			for (ServerCommands com : ServerCommands.values()){
				if (com.id == id){
					return com;
				}
			}
			return null;
		}
		
		public char getId() {
			return this.id;
		}
		
		static{
			registerUser.id = 'u';
			recieveActions.id = 't';
			getWorld.id = 'w';
			initConnect.id = 'c';
			getAllActions.id = 'a';
			genWorld.id = 'g';
			getAllPlayers.id = 'p';
		}
	}
	
	
	public static Thread Server =  new Thread(new Runnable(){

        @Override
        public void run() {
        	System.out.println("Starting server");
        	handleIncomingMessage.start();
            ServerSocketHints serverSocketHint = new ServerSocketHints();
            // 0 means no timeout.  Probably not the greatest idea in production!
            serverSocketHint.acceptTimeout = 4000;
            
            // Create the socket server using TCP protocol and listening on 9021
            // Only one app can listen to a port at a time, keep in mind many ports are reserved
            // especially in the lower numbers ( like 21, 80, etc )
       
         
            ArrayList<PlayerData> players = new ArrayList<PlayerData>();
            ArrayList<Action> actions = new ArrayList<Action>();
          
            ArrayList<PlanetData> world = new ArrayList<PlanetData>();
      
            
            Json jsonHandler = new Json();
            boolean isGenerating = false;
            // Loop forever
            while(true){
                // Create a socket
            
               
                for (Socket sock : connectedSockets) {
                	
                	  String returnMessage = "";
                      // Read data from the socket into a BufferedReader
                  
                      for (String com : comQueue){
                    	  try {
   				           String jsonData = "";
   				           
   				           String recievedData = com; 
   				           char serverCommand = recievedData.charAt(recievedData.length()-1);
   				           if (recievedData.length() > 1) {
   				        	   jsonData = recievedData.substring(0, recievedData.length()-1);
   				                
   				           } else {
   				        	   jsonData = "";
   				           }
   				           System.out.println("parsing: " + jsonData);
   				           switch(ServerCommands.getServerCommandById(serverCommand)){
   				           case registerUser:
   				        	   if (jsonData.length() < 2) {
   				         		  System.out.println("ERROR: Empty registerUser command"); 
   				         	   }
   				        	   players.add(jsonHandler.fromJson(PlayerData.class, jsonData));
   				        	   returnMessage = "welcome!";
   				        	   System.out.println("succesful connection");
   				        	 
   				        	   break;
   				           case recieveActions:
   				        	   if (jsonData.length() < 2) {
   				        		  System.out.println("ERROR: Empty recieveActions command"); 
   				        	   }
   				        	   try {
   				        		   actions.addAll(jsonHandler.fromJson(ArrayList.class, jsonData));
   				        	   } catch (SerializationException e) {
   				        		   e.printStackTrace();
   				        		   System.out.println("ERROR: Attempted to parse invalid Json: " + jsonData);
   				        	   }
   				        	
   				        	   returnMessage = "recievedTurnData!";
   				        	   break;
   				           case getWorld:
   				          	
   				          	if (!world.isEmpty()) {
   				          		returnMessage = formatReturnMessage(jsonHandler.toJson(world), GameServerClient.ClientRecieveCommands.world);
   				          	} else {
   				          		 System.out.println("Gimme a sec");
   				          	}
   				        	   break;
   				           case initConnect:
   				        	   returnMessage = "HELLO MY FRIEND";
   				        	   break;
   				        	
   				           case getAllActions:
   				          	 returnMessage = jsonHandler.toJson(actions) + "r";
   				          	 actions.clear();
   				        	   break;
   				           case genWorld:
   				          	 System.out.println("Generating world");
   				          	 if (!isGenerating) {
   				          		world = WorldGen.generate(players.size(), 0, 0);
   				          		isGenerating = true;
   				          	 }
   				          	 returnMessage = "working on it";
   				          	 break;
   				           case getAllPlayers:
   				          	 returnMessage = formatReturnMessage(jsonHandler.toJson(players), GameServerClient.ClientRecieveCommands.players);
   				          	 break;
   				           }
   				        
   				            System.out.println("Sending back: " + returnMessage);
   				            returnMessage += "\n";
   				            sock.getOutputStream().write(returnMessage.getBytes());
   				        	
   				        } catch (IOException e) {
   				            e.printStackTrace();
   				        }
                    	
                      }
                     
                      comQueue.clear();  
                }
                
                   
              
            }
        }
        
        public String formatReturnMessage(String data, GameServerClient.ClientRecieveCommands crc) {
        	return data + crc.getId();
        }
        
    }); // And, start the thread running
	
	public static Thread handleIncomingMessage =  new Thread(new Runnable(){
		
		 ArrayList<String> coms = new ArrayList<String>();
		 ArrayList<Socket> connSockets = new ArrayList<Socket>();
		@Override
		public void run() {
		    ServerSocketHints serverSocketHint = new ServerSocketHints();
            // 0 means no timeout.  Probably not the greatest idea in production!
            serverSocketHint.acceptTimeout = 4000;
			ServerSocket serverSocket = Gdx.net.newServerSocket(Protocol.TCP, 7777, serverSocketHint);
			while (true){ 
				try {
	       		 	Socket socket = serverSocket.accept(null);
	       		
	                if (!connSockets.contains(socket)) {
	                	connSockets.add(socket);
	                }
		       	} catch (GdxRuntimeException e) {
		       		System.out.println("Error: tried to connect when already connected");
		       	}
				 for (Socket sock : connSockets) {
					  BufferedReader buffer = new BufferedReader(new InputStreamReader(sock.getInputStream())); 
			            try {
							if (buffer.ready()){
								 coms.add(buffer.readLine());
							  }
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				 }
				
					 
				 connectedSockets = connSockets;
				 comQueue = coms;
					
					 
				 
				
			
				
			
			
			 
			}
		}
		
		  
		
		
	});

}
