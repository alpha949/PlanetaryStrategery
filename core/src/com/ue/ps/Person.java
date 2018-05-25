package com.ue.ps;

import com.badlogic.gdx.math.MathUtils;

public class Person {
	
	public int health;
	public int damage;
	
	private Person(int health, int attack) {
		this.health = health;
		this.damage = attack;
	}
	
	public static Person genPerson() {
		return new Person(MathUtils.random(15,25), MathUtils.random(5, 15));
	}
}
