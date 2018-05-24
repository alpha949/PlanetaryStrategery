package com.ue.ps.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.ue.ps.ui.Images;

public enum BuildingType {
	colony, factory, mine, cannon, spacefactory;
	
	public int health;
	public int resourceCost;
	public Texture texture;
	public boolean isSpace = false;
	public char id;
	
	static {
		colony.id = 'c';
		colony.health = 100;
		colony.resourceCost = 6;
		colony.texture = Images.colony;
		
		factory.id = 'f';
		factory.health = 60;
		factory.resourceCost = 5;
		factory.texture = Images.factory;

		spacefactory.id = 's';
		spacefactory.health = 50;
		spacefactory.resourceCost = 5; //should cost more but build ships cheaper/faster
		spacefactory.texture = Images.spaceFactory;
		spacefactory.isSpace = true;

		mine.id = 'm';
		mine.health = 50;
		mine.resourceCost = 5;
		mine.texture = Images.mine;
		
		cannon.id = 'd';
		cannon.health = 120;
		cannon.resourceCost = 8;
		cannon.texture = Images.STAcannon;

	}
}
