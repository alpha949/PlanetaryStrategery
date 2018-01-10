package com.ue.ps;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class ResourcePanel extends BaseActor{
	
	private Faction faction;
	private Player player;
	
	
	private Label peopleCounter = new Label("", PS.font);
	private Label resourceCounter = new Label("", PS.font);
	private Label techCounter = new Label("", PS.font);
	
	
	public ResourcePanel(Faction f, Player p) {
		super("assets/resourcePanel.png");
		this.faction = f;
		this.player = p;
	}

}
