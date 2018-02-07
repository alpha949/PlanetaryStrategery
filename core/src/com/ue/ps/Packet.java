package com.ue.ps;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

public class Packet {
	
	//format: [7:(s,4/d,2){m,}];[5:f,1] 
	//        [planetId:(ShipId,count/ShipId,count){}];[planetId:ShipId,count]
	
	private String testData = "[7:s,4/d,2];[5:f,1]";
	private String data;
	

	
	public Packet() {
		
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public String getData() {
		return this.data;
	}
	
	
	private void resloveData() {
		String[] planets = data.split(";");
		for (String p : planets) {
			int planetId = p.charAt(1);
			String planetShips = p.substring(3, p.length()-1);
			String[] ships = planetShips.split("/");
			for (String s : ships) {
				ShipType shipType = ShipType.getShipType(s.charAt(0));
				String shipCounts = s.substring(2, s.length()-1);
				int shipCount = 0;
				if (shipCounts.length() > 1) {
					shipCount = shipCounts.charAt(0) * 10 + shipCounts.charAt(1);
				} else {
					shipCount = shipCounts.charAt(0);
				}
			}
			
			
		}
	}
	
	
	
	
	
}
