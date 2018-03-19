package com.ue.ps;

import com.badlogic.gdx.Input.TextInputListener;

public class TextInput implements TextInputListener {
	
		private String text;
	
	   @Override
	   public void input (String text) {
		  this.text = text;
	   }

	   @Override
	   public void canceled () {
	   }
	   
	   public String getText(){
		   return this.text;
	   }
	}
