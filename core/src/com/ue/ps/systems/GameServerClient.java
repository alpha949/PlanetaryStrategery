package com.ue.ps.systems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.ue.ps.Faction;
import com.ue.ps.Planet;
import com.ue.ps.PlanetData;
import com.ue.ps.Player;
import com.ue.ps.ships.Ship;
import com.ue.ps.systems.GameServer.ServerCommands;

public class GameServerClient {

	public static String userIpAddress;

	public static Player clientPlayer;
	public static String user;

	private String receivedData = "";
	private String connectionResult = "";
	private String ip;
	private int port;

	public static Packet packet = new Packet();

	public static ArrayList<Player> players = new ArrayList<Player>();

	private Socket socket; 

	public enum ClientRecieveCommands {
		world, actions, players;

		private char id;

		public char getId() {
			return this.id;
		}

		public static ClientRecieveCommands getClientRecieveCommandById(char id) {
			for (ClientRecieveCommands crc : ClientRecieveCommands.values()) {
				if (crc.id == id) {
					return crc;
				}
			}
			return null;
		}

		static {
			world.id = 'w';
			actions.id = 'a';
			players.id = 'p';
		}
	}

	/**
	 * Creates a severClient connection to the given ip; this will only throw
	 * errors when calling sendRequest if the ip can't be found
	 * 
	 * @param ip the ip to connect to
	 */
	public GameServerClient(String ip, int port) {
		this.ip = ip;
		this.port = port;
		SocketHints socketHints = new SocketHints();
		// Socket will time our in 4 seconds
		socketHints.connectTimeout = 4000;
		// create the socket and connect to the server entered in the text box (
		// x.x.x.x format ) on port 9021
		socket = Gdx.net.newClientSocket(Protocol.TCP, ip, port, socketHints);
		this.sendRequest("", ServerCommands.initConnect);
		this.receive.start();

	}

	/**
	 * registers a player with the server, should only be called on first
	 * connection
	 * 
	 * @param p the player to register
	 */
	public void registerPlayer(Player p) {
		Json j = new Json();
		j.setOutputType(OutputType.json);

		String data = j.toJson(p.toPlayerData());

		this.sendRequest(data, GameServer.ServerCommands.registerUser);

	}

	/**
	 * sends data to the server
	 * 
	 * @param data the data to be sent
	 * @param com the command for the server to perform
	 */
	public void sendRequest(String jsonStringToSend, GameServer.ServerCommands com) {

		jsonStringToSend += com.getId() + "\n";
		if (com == GameServer.ServerCommands.genWorld) {
			System.out.println("Make me a world plz");
		}

		// System.out.println("sending " + com.name() + " request: " +
		// jsonStringToSend);
		try {
			// write our entered message to the stream

			socket.getOutputStream().write(jsonStringToSend.getBytes());
		} catch (Exception e) {
			// System.out.println("Socket write error");

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

				if (!data.equals(prevData)) {
					System.out.println("Recieved: " + data);
					receivedData = data;
				}
				prevData = data;

			}

		}

	});

	/**
	 * gets the data received from the server, may be an empty string
	 * 
	 * @return String the data recieve from the server
	 */
	public String getRecievedData() {

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
		clientPlayer = new Player(user, f);
	}

	public static boolean isCorrectDataType(String data, ClientRecieveCommands crc) {
		if (data.length() > 1 && data.charAt(data.length() - 1) == crc.id) {
			return true;
		} else {
			return false;
		}
	}

	public static Player getPlayerByUserName(String userName) {
		for (Player p : players) {
			if (p.getUser().equals(userName)) {
				return p;
			}
		}
		return null;
	}

	public void dispose() {
		socket.dispose();
	}
	
	public static boolean isOwnedBy(Player p, Planet P) {
		if (P.owner == p) {
			return true;
		} else {
			return false;
		}
	}
	public static boolean isOwnedBy(Player p, Ship s) {
		if (s.getOwnerName().equals(p.getUser())) {
			return true;
		} else {
			return false;
		}
	}

}
