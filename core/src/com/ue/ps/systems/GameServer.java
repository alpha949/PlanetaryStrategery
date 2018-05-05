package com.ue.ps.systems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.badlogic.gdx.utils.SerializationException;
import com.ue.ps.PlanetData;
import com.ue.ps.Player;
import com.ue.ps.PlayerData;
import com.ue.ps.WorldGen;
import com.ue.ps.ui.MenuScreen;

/*Server Command strings:
 * 
 * 
 */
public class GameServer {
	static ArrayList<Socket> connectedSockets = new ArrayList<Socket>();
	static ArrayList<String> comQueue = new ArrayList<String>();
	
	static JsonReader reader = new JsonReader();
	
	static Json jsonHandler = new Json();

	public enum ServerCommands {
		registerUser, recieveActions, getWorld, initConnect, getAllActions, genWorld, getAllPlayers, beginGame, askBeginGame,
		updatePlayer;

		private char id;

		public static ServerCommands getServerCommandById(char id) {
			for (ServerCommands com : ServerCommands.values()) {
				if (com.id == id) {
					return com;
				}
			}
			return null;
		}

		public char getId() {
			return this.id;
		}

		static {
			registerUser.id = 'u';
			recieveActions.id = 't';
			getWorld.id = 'w';
			initConnect.id = 'c';
			getAllActions.id = 'a';
			genWorld.id = 'g';
			getAllPlayers.id = 'p';
			beginGame.id = 'b';
			askBeginGame.id = 's';
			updatePlayer.id = 'l';
		}
	}

	public static Thread Server = new Thread(new Runnable() {

		@Override
		public void run() {
			System.out.println("Starting server");
			handleIncomingMessage.start();

			// Create the socket server using TCP protocol and listening on 9021
			// Only one app can listen to a port at a time, keep in mind many
			// ports are reserved
			// especially in the lower numbers ( like 21, 80, etc )

			ArrayList<PlayerData> players = new ArrayList<PlayerData>();
			ArrayList<Action> actions = new ArrayList<Action>();

			ArrayList<PlanetData> world = new ArrayList<PlanetData>();

		
			boolean isGenerating = false;
			boolean gameBegin = false;
			// Loop forever
			while (true) {
				// Create a socket
				for (Socket sock : connectedSockets) {
					BufferedReader buffer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
					try {

						if (buffer.ready()) {

							String data = buffer.readLine();
							System.out.println("got some data");
							comQueue.add(data);

						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				for (Socket sock : connectedSockets) {

					String returnMessage = "";

					for (String com : comQueue) {

						try {
							String jsonData = "";

							String recievedData = com;
							char serverCommand = recievedData.charAt(recievedData.length() - 1);
							if (recievedData.length() > 1) {
								jsonData = recievedData.substring(0, recievedData.length() - 1);

							} else {
								jsonData = "";
							}
							System.out.println("parsing: " + jsonData);
							switch (ServerCommands.getServerCommandById(serverCommand)) {
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
										System.out.println("gimme a sec");
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
										ArrayList<Player> plas = new ArrayList<Player>();
										for (PlayerData pd : players) {
											System.out.println("added player: " + pd.username);
											plas.add(Player.fromPlayerData(pd));
										}
										world = WorldGen.generate(plas, 0, 0);
										isGenerating = true;
									}
									returnMessage = "working on it";
									break;
								case getAllPlayers:
									returnMessage = formatReturnMessage(jsonHandler.toJson(players), GameServerClient.ClientRecieveCommands.players);
									break;
								case beginGame:
									gameBegin = true;
									break;
								case askBeginGame:
									returnMessage = Boolean.toString(gameBegin);
									break;
								case updatePlayer:
									JsonValue value = reader.parse(jsonData);
									String username = value.getString("username");
									String factionAbv = value.getString("factionAbv");
									

									

								
									for (PlayerData pd : players) {
										if (pd.username.equals(username)) {
											pd.factionAbv = factionAbv;
										}
									}
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

	public static Thread handleIncomingMessage = new Thread(new Runnable() {

		ArrayList<Socket> connSockets = new ArrayList<Socket>();

		@Override
		public void run() {
			ServerSocketHints serverSocketHint = new ServerSocketHints();
			// 0 means no timeout. Probably not the greatest idea in production!
			serverSocketHint.acceptTimeout = 0;
			ServerSocket serverSocket = null;

			try {
				serverSocket = Gdx.net.newServerSocket(Protocol.TCP, MenuScreen.port, serverSocketHint);
			} catch (GdxRuntimeException e) {
				System.out.println("port taken");
				Gdx.app.exit();

			}

			while (true) {
				try {
					Socket socket = serverSocket.accept(null);

					if (!connSockets.contains(socket)) {
						connSockets.add(socket);
					}
				} catch (GdxRuntimeException e) {
					e.printStackTrace();
				}

				connectedSockets = connSockets;

			}
		}

	});
	
	public static String formatUpdatePlayer(String username, String factionAbv) {
		StringWriter sw = new StringWriter();
		Json handler = new Json();
		handler.setOutputType(OutputType.json);
		handler.setWriter(sw);
		handler.writeObjectStart();
		handler.writeValue("username", username);
		handler.writeValue("factionAbv", factionAbv);
		handler.writeObjectEnd();
		return sw.toString();
	}

}
