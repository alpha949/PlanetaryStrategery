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
	
	public enum ServerCommands{
		registerUser, recieveActions, getWorld, initConnect, getAllActions;
		
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
		}
	}
	
	
	public static Thread Server =  new Thread(new Runnable(){

        @Override
        public void run() {
        	System.out.println("Starting server");
        	
            ServerSocketHints serverSocketHint = new ServerSocketHints();
            // 0 means no timeout.  Probably not the greatest idea in production!
            serverSocketHint.acceptTimeout = 4000;
            
            // Create the socket server using TCP protocol and listening on 9021
            // Only one app can listen to a port at a time, keep in mind many ports are reserved
            // especially in the lower numbers ( like 21, 80, etc )
       
            ServerSocket serverSocket = Gdx.net.newServerSocket(Protocol.TCP, 9021, serverSocketHint);
           
          
            ArrayList<User> users = new ArrayList<User>();
            ArrayList<Action> actions = new ArrayList<Action>();
            ArrayList<Socket> connectedSockets = new ArrayList<Socket>();
            Json jsonHandler = new Json();
          
            // Loop forever
            while(true){
                // Create a socket
            	try {
            		 Socket socket = serverSocket.accept(null);
                     if (!connectedSockets.contains(socket)) {
                     	connectedSockets.add(socket);
                     }
            	} catch (GdxRuntimeException e) {
            		System.out.println("Error: tried to connect when already connected");
            	}
               
                for (Socket sock : connectedSockets) {
                	  String returnMessage = "";
                      // Read data from the socket into a BufferedReader
                      BufferedReader buffer = new BufferedReader(new InputStreamReader(sock.getInputStream())); 
                      
                      try {
                         String jsonData = "";
                         String recievedData = buffer.readLine(); 
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
                      	   users.add(jsonHandler.fromJson(User.class, jsonData));
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
                      	   break;
                         case initConnect:
                      	   returnMessage = "HELLO MY FRIEND";
                      	   break;
                      	
                         case getAllActions:
                        	 returnMessage = jsonHandler.toJson(actions) + "r";
                      	   break;
                         }
                          System.out.println("Sending back: " + returnMessage);
                          returnMessage += "\n";
                          sock.getOutputStream().write(returnMessage.getBytes());
                      	
                      } catch (IOException e) {
                          e.printStackTrace();
                      }
                }
              
            }
        }
    }); // And, start the thread running


}
