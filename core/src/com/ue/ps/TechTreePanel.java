package com.ue.ps;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class TechTreePanel extends BaseActor{
	
	public boolean isOpen;
	private static final int itemDist = 100;
	private static Texture techLineTexture = Utils.getImg("techLine");
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
			if (tti != TechTreeItem.baseTech) {
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
		int branch = -10;
		boolean success = false;
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
					System.out.println("success: " + tti.name);
					success = true;
					break;
				}
			}
			if (success) {
				break;
			}
		
			
		}
		
		if (branch == -10) {
			
			branch = -10;
			System.out.println("error: " + tti.name);
				
		}
		if (tti.preReq != null) {
			if (tti.preReq == TechTreeItem.baseTech) {
				tti.preReq.center.x = PS.viewWidth/2;
				tti.preReq.center.y = PS.viewHeight/2;
			}
	
			float angle = 360/8 * branch;
			Vector2 ttiPos = Utils.polarToRect(itemDist, angle, tti.preReq.center);
			tti.setCenter(ttiPos.x, ttiPos.y);
			this.addActor(tti);
			BaseActor techLine = new BaseActor(techLineTexture);
			techLine.setPosition(tti.preReq.center.x, tti.preReq.center.y);
			techLine.setRotation(angle);
			techLine.setOrigin(0, 0);
			this.addActor(techLine);
		}
		
		
	}
	
	public static void registerTech(TechTreeItem tti) {
		items.add(tti);
		
	}
}
