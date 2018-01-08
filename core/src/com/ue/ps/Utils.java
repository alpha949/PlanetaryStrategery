package com.ue.ps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

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
	
}
