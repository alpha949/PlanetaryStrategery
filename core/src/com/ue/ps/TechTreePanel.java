package com.ue.ps;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class TechTreePanel extends BaseActor{
	
	public boolean isOpen;
	private static final int itemDist = 100;
	
	private static ArrayList<TechTreeItem> items = new ArrayList<TechTreeItem>();
	
	public TechTreePanel() {
		super("assets/grayBack.png");
		items.add(TechTreeItem.baseTech);
		items.get(0).setCenter(PS.viewWidth/2, PS.viewHeight/2);
		this.addActor(items.get(0));
		this.setVisible(false);
	}
	
	
	public void open() {
		this.setVisible(true);
		for (TechTreeItem tti : items) {
			if (tti != items.get(0)) {
				this.placeItem(tti);
			}
			
		}
		this.isOpen = true;
	}
	
	public void close() {
		this.setVisible(false);
		this.isOpen = false;
	}
	
	private void placeItem(TechTreeItem tti) {
		int branch = 0;
		for (int i = 0; i< 8; i++) {
			for (int l = 0; l < 8; l++) {
				if (!tti.preReq.branches[i] && !tti.branches[l]) {
					tti.preReq.branches[i] = true;
					int oppositeBranch = l - 4;
					if (oppositeBranch < 0) {
						oppositeBranch = l + 4;
					}
					tti.branches[oppositeBranch] = true;
					branch = i;
				}
			}
			
		}
		if (tti.preReq != null) {
			if (tti.preReq == TechTreeItem.baseTech) {
				tti.preReq.center.x = PS.viewWidth/2;
				tti.preReq.center.y = PS.viewHeight/2;
			}
			System.out.println(tti.preReq.center);
			float angle = 360/8 * branch;
			Vector2 ttiPos = Utils.polarToRect(itemDist, angle, tti.preReq.center);
			tti.setCenter(ttiPos.x, ttiPos.y);
			
			this.addActor(tti);
		}
		
		
	}
	
	public static void registerTech(TechTreeItem tti) {
		items.add(tti);
		
	}
}
