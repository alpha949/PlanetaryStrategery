package com.ue.ps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Utils {
	
	
	public static Vector2 polarToRect(int r, double angle, Vector2 origin){
		Vector2 v = new Vector2();
		v.x = (float) (r*Math.cos(Math.toRadians(angle))) + origin.x;
		v.y = (float) (r*Math.sin(Math.toRadians(angle))) + origin.y;
		
		return v;
	}
	
	public static void insertionSort(int[] ar)
	{
	   for (int i=1; i < ar.length; i++)
	   {
	      int index = ar[i]; int j = i;
	      while (j > 0 && ar[j-1] > index)
	      {
	           ar[j] = ar[j-1];
	           j--;
	      }
	      ar[j] = index;
	   }
	   
	}
	

	
	public static Texture getImg(String path){
		try{
			Texture t = new Texture(Gdx.files.internal("assets/" + path));
			return t;
			
		} catch (Exception GdxRuntimeException) {
			System.out.println("Error: Could not find: " + path + " substituting...");
			Texture t = new Texture(Gdx.files.internal("assets/missingTex.png"));
			return t;
		}
	}
	
	public static boolean glideCameraTo(float x, float y, Camera c){
		double angle = pointAt(c.position.x, c.position.y,x, y);
		
		
		Vector2 angleMult = convertVel((float) angle);
		
		double speed = distanceTo(c.position.x, c.position.y,x, y)/5;
		
		c.position.x += speed* angleMult.x;
		c.position.y +=	speed * angleMult.y;
		GameplayScreen.cameraOffsetX += speed* angleMult.x;
		GameplayScreen.cameraOffsetY += speed* angleMult.y;
		
		
		if (c.position.x == x && c.position.y == y){
			return true;
		} else {
			return false;
		}
		
		
	}
	
	public static double pointAt(float x, float y, float x2, float y2){
		
		double yDiff = y - y2;
		double xDiff = x - x2;
		double newAngle = Math.toDegrees(Math.atan2(yDiff, xDiff)) + 180;
		System.out.println(newAngle);
		
	
		
		
		return newAngle;
		
		
	}
	
	public static Vector2 convertVel(float angle){
		double radians = Math.toRadians(angle);
		
		return new Vector2((float) Math.cos(radians),(float) Math.sin(radians));
	}
	
	public static double distanceTo(double x, double y, double x2, double y2){
		
		return Math.hypot(Math.abs(x2 - x), Math.abs(y2 - y));
	}
	
}
