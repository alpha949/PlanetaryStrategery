package com.ue.ps;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class TechTreeItem extends Button{ //PLZ change(d) to extends button? would prob help in future, make easier
	
	public int cost;
	public String name;
	private int level;
	private int preReqLevelReq;
	private int maxLevel;
	private float costMult;
	public TechTreeItem preReq;
	private Label nameLabel = new Label("", PS.font);
	private boolean isUnlocked;
	
	private Texture lockTex = Images.getImg("lockTech");
	private BaseActor lock = new BaseActor(lockTex);

	public boolean[] branches = new boolean[8];
	
	public TechTreeItem() {
		super(Images.TechTreeHolder);
	}
	public TechTreeItem(TechTreeItem preReq, int preReqLevelReq, String name, int cost, int maxLevel, float costMult) {
		super(Images.TechTreeHolder);
		this.preReq = preReq;
		this.name = name;
		this.nameLabel.setText(name);
		nameLabel.setPosition(this.getOriginX(), this.getOriginY());
		
		this.cost = cost;
		this.preReqLevelReq = preReqLevelReq;
		this.maxLevel = maxLevel;
		this.costMult = costMult;
		lock.setCenter(this.getOriginX(), this.getOriginY());
		this.addActor(lock);
		this.addActor(nameLabel);
	}
	
	public void obtain() {
		if (GameServerClient.clientPlayer.techPoints >= this.cost) {
			GameServerClient.clientPlayer.techPoints -= this.cost;
			if (this.level < this.maxLevel) {
				this.level += 1;
				this.cost *= this.costMult;
			}
			
		}
	
	}
	
	public void unlock() {
		if (this.preReq.level >= this.preReqLevelReq ) {
			this.isUnlocked = true;
			this.lock.setVisible(false);
		}
	}
	
	public static TechTreeItem baseTech = new TechTreeItem(null, 0, "Base", 0, 0, 0);
	public static TechTreeItem weapons = new TechTreeItem(baseTech, 0, "weapons", 0, 10, 1.5f);
	public static TechTreeItem hull = new TechTreeItem(baseTech, 0, "hull", 0, 10, 1.5f);
	public static TechTreeItem shield = new TechTreeItem(baseTech, 0, "shield", 0, 10, 1.5f);
	public static TechTreeItem planetShield = new TechTreeItem(shield, 5, "planetShield", 0, 1, 0);
	public static TechTreeItem intelMining = new TechTreeItem(baseTech, 0, "intelMining", 0, 10, 1.4f );
	public static TechTreeItem production = new TechTreeItem(intelMining, 2, "production", 0, 10, 1.7f);
	public static TechTreeItem massProduction = new TechTreeItem(intelMining, 2, "massProduction", 0, 10, 1.8f);
	public static TechTreeItem colonyResearch = new TechTreeItem(baseTech,0, "colonyResearch", 0, 10, 1.5f);
	public static TechTreeItem telescopes = new TechTreeItem(baseTech, 1, "telescopes", 0, 10, 1.6f);
	public static TechTreeItem radar = new TechTreeItem(telescopes, 9, "radar", 0, 1, 0);
	public static TechTreeItem science = new TechTreeItem(baseTech, 1, "science", 0, 10, 2f);
	
	static {
		TechTreePanel.registerTech(weapons);
		TechTreePanel.registerTech(hull);
		TechTreePanel.registerTech(shield);
		TechTreePanel.registerTech(planetShield);
		TechTreePanel.registerTech(intelMining);
		TechTreePanel.registerTech(production);
		TechTreePanel.registerTech(massProduction);
		TechTreePanel.registerTech(colonyResearch);
		TechTreePanel.registerTech(telescopes);
		TechTreePanel.registerTech(radar);
		TechTreePanel.registerTech(science);
	}
}
