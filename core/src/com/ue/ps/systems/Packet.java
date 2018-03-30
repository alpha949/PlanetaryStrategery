package com.ue.ps.systems;

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
import com.badlogic.gdx.utils.SerializationException;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

public class Packet {

	// format: [7:(s,4/d,2){m,}];[5:f,1]
	// [planetId:(ShipId,count/ShipId,count){}];[planetId:ShipId,count]

	private ArrayList<Action> actions = new ArrayList<Action>();
	private Json jsonData = new Json();
	private String data = "";

	public Packet() {
		jsonData.setOutputType(OutputType.json);
	}

	private void resolveData() {
		System.out.println("Resolving: " + data);
		if (data != null) {
			try {
				actions = (ArrayList<Action>) jsonData.fromJson(ArrayList.class, data);
			} catch (SerializationException e) {
				e.printStackTrace();
				System.out.println("ERROR: Attempted to parse invalid Json: " + data);

			}

		}

	}

	private String compactData() {

		return jsonData.toJson(actions);

	}

	public void addAction(Action a) {
		System.out.println("added an action");
		actions.add(a);
	}

	public void setData(String data) {
		this.data = data;
		this.actions.clear();
		resolveData();
	}

	public ArrayList<Action> getActions() {
		return this.actions;
	}

	public String getData() {
		return this.data;
	}

	public String getCompressedData() {
		return compactData();
	}

}
