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
import com.badlogic.gdx.utils.Json;


/*Server Command strings:
 * 
 * 
 */
public class GameServer {
	
	public enum ServerCommands{
		registerUser, recieveActions, getWorld;
		
		private char id;
		
		public static ServerCommands getServerCommandById(char id){
			for (ServerCommands com : ServerCommands.values()){
				if (com.id == id){
					return com;
				}
			}
			return null;
		}
		static{
			registerUser.id = 'u';
			recieveActions.id = 't';
			getWorld.id = 'w';
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
            String returnMessage = "";
            // Loop forever
            while(true){
                // Create a socket
                Socket socket = serverSocket.accept(null);
                
                // Read data from the socket into a BufferedReader
                BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
                
                try {
                    // 
                   String recievedData = buffer.readLine(); 
                   char serverCommand = recievedData.charAt(recievedData.length()-1);
                   String jsonData = recievedData.substring(0, recievedData.length()-2);
                   switch(ServerCommands.getServerCommandById(serverCommand)){
                   case registerUser:
                	   players.add(jsonHandler.fromJson(Player.class, jsonData));
                	   returnMessage = "welcome!";
                	 
                	   break;
                   case recieveActions:
                	   actions.add(jsonHandler.fromJson(Action.class, jsonData));
                	   returnMessage = "recievedTurnData!";
                	   break;
                   case getWorld:
                	   break;
                   }
                   
                   socket.getOutputStream().write(returnMessage.getBytes());
                	
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }); // And, start the thread running

	
	public static String setServerCommand(String jsonStringToSend, ServerCommands com){
		jsonStringToSend += com.id;
		return jsonStringToSend;
	}
}
