package com.ue.ps;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class ResourcePanel extends BaseActor{
	
	private Faction faction;
	private Player player;
	
	
	private Label peopleCounter = new Label("3", PS.font);
	private Label resourceCounter = new Label("6", PS.font);
	private Label techCounter = new Label("9", PS.font);
	
	
	
	public ResourcePanel(Faction f, Player p) {
		super("assets/resourcePanel.png");
		this.faction = f;
		this.player = p;
		BaseActor factionIcon = new BaseActor("assets/Red.png");
		factionIcon.setPosition(1, 1);
		this.addActor(factionIcon);
		
		BaseActor peopleIcon = new BaseActor("assets/personel.png");
		peopleIcon.setPosition(102, 33);
		this.addActor(peopleIcon);
		
		BaseActor resourceIcon = new BaseActor("assets/resource.png");
		resourceIcon.setPosition(495, 33);
		this.addActor(resourceIcon);
		
		BaseActor techIcon = new BaseActor("assets/tech.png");
		techIcon.setPosition(888, 33);
		this.addActor(techIcon);
		
		
		
		peopleCounter.setPosition(102 + 42, 39);
		resourceCounter.setPosition(495 + 42, 39);
		techCounter.setPosition(888 + 42, 39);
		
		this.addActor(peopleCounter);
		this.addActor(resourceCounter);
		this.addActor(techCounter);
		
	}

}
