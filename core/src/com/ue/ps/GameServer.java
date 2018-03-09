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
            ServerSocketHints serverSocketHint = new ServerSocketHints();
            // 0 means no timeout.  Probably not the greatest idea in production!
            serverSocketHint.acceptTimeout = 0;
            
            // Create the socket server using TCP protocol and listening on 9021
            // Only one app can listen to a port at a time, keep in mind many ports are reserved
            // especially in the lower numbers ( like 21, 80, etc )
       
            ServerSocket serverSocket = Gdx.net.newServerSocket(Protocol.TCP, 9021, serverSocketHint);
           
          
            ArrayList<Player> players = new ArrayList<Player>();
            ArrayList<Action> actions = new ArrayList<Action>();
            Json jsonHandler = new Json();
          
            // Loop forever
            while(true){
                // Create a socket
                Socket socket = serverSocket.accept(null);
                String returnMessage = "";
                // Read data from the socket into a BufferedReader
                BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
                
                try {
                   String jsonData = "";
                   String recievedData = buffer.readLine(); 
                   char serverCommand = recievedData.charAt(recievedData.length()-1);
                   if (recievedData.length() > 1) {
                	   jsonData = recievedData.substring(0, recievedData.length()-2);
                        
                   } else {
                	   jsonData = "";
                   }
                   switch(ServerCommands.getServerCommandById(serverCommand)){
                   case registerUser:
                	   if (jsonData.length() < 2) {
                 		  System.out.println("ERROR: Empty registerUser command"); 
                 	   }
                	   players.add(jsonHandler.fromJson(Player.class, jsonData));
                	   returnMessage = "welcome!";
                	   System.out.println("succesful connection");
                	 
                	   break;
                   case recieveActions:
                	   if (jsonData.length() < 2) {
                		  System.out.println("ERROR: Empty recieveActions command"); 
                	   }
                	   try {
                		   actions.add(jsonHandler.fromJson(Action.class, jsonData));
                	   } catch (SerializationException e) {
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
                	   returnMessage = "here are actions";
                	   break;
                   }
                    System.out.println("Sending back: " + returnMessage);
                    returnMessage += "\n";
                   socket.getOutputStream().write(returnMessage.getBytes());
                	
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }); // And, start the thread running


}
