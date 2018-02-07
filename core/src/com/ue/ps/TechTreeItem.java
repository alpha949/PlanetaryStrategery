package com.ue.ps;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class TechTreeItem extends BaseActor{
	
	public int cost;
	public String name;
	public TechTreeItem preReq;
	private Label nameLabel = new Label("", PS.font);

	public boolean[] branches = new boolean[8];
	
	public TechTreeItem() {
		super("assets/techTreeItemHolder.png");
	}
	public TechTreeItem(TechTreeItem preReq, String name) {
		super("assets/techTreeItemHolder.png");
		this.preReq = preReq;
		this.name = name;
		this.nameLabel.setText(name);
		nameLabel.setPosition(this.getOriginX(), this.getOriginY());
		this.addActor(nameLabel);
	}
	public static TechTreeItem baseTech = new TechTreeItem(null, "Base");
	public static TechTreeItem tech = new TechTreeItem(baseTech, "1");
	public static TechTreeItem tech2 = new TechTreeItem(tech, "2");
	public static TechTreeItem tech3 = new TechTreeItem(tech2, "3");
	public static TechTreeItem tech4 = new TechTreeItem(baseTech, "4");
	public static TechTreeItem tech5 = new TechTreeItem(tech3, "5");
	public static TechTreeItem tech6 = new TechTreeItem(baseTech, "6");
	public static TechTreeItem tech7 = new TechTreeItem(tech2, "7");
	public static TechTreeItem tech8 = new TechTreeItem(tech2, "8");
	public static TechTreeItem tech9 = new TechTreeItem(tech2, "9");
	public static TechTreeItem tech10 = new TechTreeItem(tech2, "10");
	
	static {
		TechTreePanel.registerTech(tech);
		TechTreePanel.registerTech(tech2);
		TechTreePanel.registerTech(tech3);
		TechTreePanel.registerTech(tech4);
		TechTreePanel.registerTech(tech5);
		TechTreePanel.registerTech(tech6);
		TechTreePanel.registerTech(tech7);
		TechTreePanel.registerTech(tech8);
		TechTreePanel.registerTech(tech9);
		TechTreePanel.registerTech(tech10);
	}
}
