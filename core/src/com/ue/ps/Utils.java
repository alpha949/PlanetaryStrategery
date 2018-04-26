package com.ue.ps;

import java.util.ArrayList;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ue.ps.ships.Ship;
import com.ue.ps.ui.GameplayScreen;

public class Utils {

	public static Vector2 polarToRect(int r, double angle, Vector2 origin) {
		Vector2 v = new Vector2();
		v.x = (float) (r * Math.cos(Math.toRadians(angle))) + origin.x;
		v.y = (float) (r * Math.sin(Math.toRadians(angle))) + origin.y;

		return v;
	}

	public static void insertionSort(int[] ar) {
		for (int i = 1; i < ar.length; i++) {
			int index = ar[i];
			int j = i;
			while (j > 0 && ar[j - 1] > index) {
				ar[j] = ar[j - 1];
				j--;
			}
			ar[j] = index;
		}

	}

	@Deprecated // PLZ remove?
	public static Texture getImg(String path) {
		System.out.println("Depricated getImg used!");
		try {
			Texture t = new Texture(Gdx.files.internal("assets/" + path + ".png"));
			return t;

		} catch (Exception GdxRuntimeException) {
			System.out.println("Error: Could not find: " + path + " substituting...");
			Texture t = new Texture(Gdx.files.internal("assets/missingTex.png"));
			return t;
		}
	}

	public static boolean glideCameraTo(float x, float y, Camera c) {
		double angle = pointAt(c.position.x, c.position.y, x, y);

		Vector2 angleMult = convertVel((float) angle);

		double speed = distanceTo(c.position.x, c.position.y, x, y) / 5;

		c.position.x += speed * angleMult.x;
		c.position.y += speed * angleMult.y;
		GameplayScreen.cameraOffsetX += speed * angleMult.x;
		GameplayScreen.cameraOffsetY += speed * angleMult.y;

		if (distanceTo(c.position.x, c.position.y, x, y) < 16) {
			return true;
		} else {
			return false;
		}

	}

	public static double pointAt(float x, float y, float x2, float y2) {

		double yDiff = y - y2;
		double xDiff = x - x2;
		double newAngle = Math.toDegrees(Math.atan2(yDiff, xDiff)) + 180;

		return newAngle;

	}

	public static Vector2 convertVel(float angle) {
		double radians = Math.toRadians(angle);

		return new Vector2((float) Math.cos(radians), (float) Math.sin(radians));
	}

	public static double distanceTo(double x, double y, double x2, double y2) {

		return Math.hypot(Math.abs(x2 - x), Math.abs(y2 - y));
	}

	public static String genName() {
		int length = MathUtils.random(2, 4);
		String name = "";
		for (int i = 0; i < length; i++) {
			name += nameBits.get(MathUtils.random(0, nameBits.size - 1));
		}

		return name;
	}

	public static String genId() {
		UUID id = UUID.randomUUID();
		System.out.println(id.toString());
		return id.toString();
	}

	public static String[] getShipIds(ArrayList<Ship> ships) {
		String[] ids = new String[ships.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = ships.get(i).id;

		}

		return ids;
	}

	private static Array<String> nameBits;
	static {
		String[] vowels = { "a", "e", "i", "o", "u", "y" };
		String[] consonants = { "b", "c", "d", "f", "g", "h", "j", "k", "l", "m", "n", "p", "qu", "r", "s", "t", "v", "w", "x", "y", "z" };
		nameBits = new Array<String>();
		for (String v : vowels) {
			for (String c : consonants) {
				nameBits.add(c + v);
			}
		}

	}

	public static <T> T getRandObjFromArray(ArrayList<T> arr) {

		return arr.get(MathUtils.random(0, arr.size() - 1));

	}

}
