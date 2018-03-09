package com.ue.ps;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class TechTreePanel extends BaseActor{
	
	public boolean isOpen;
	private static final int itemDist = 150;
	private static Texture techLineTexture = Images.getImg("techLine");
	private static ArrayList<TechTreeItem> items = new ArrayList<TechTreeItem>();
	
	private boolean isSetup;
	
	public TechTreePanel() {
		super("assets/grayBack.png");
		items.add(TechTreeItem.baseTech);
		items.get(0).setCenter(PS.viewWidth/2, PS.viewHeight/2);
		this.addActor(items.get(0));
		this.setVisible(false);
	}
	
	
	public void open() {
	
		this.setVisible(true);
		if (!isSetup) {
			for (TechTreeItem tti : items) {
				if (tti != TechTreeItem.baseTech) {
					this.placeItem(tti);
					tti.unlock();
				}
				
			}
			isSetup = true;
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
					//find no goes from tii
					int notBranchTop = i + 3;
					int notBranchBottom = i - 3;
					if (notBranchTop > 8) {
						notBranchTop -= 8;
					}
					if (notBranchBottom < 0) {
						notBranchBottom += 8;
						
					}
					//find no goes from preReq
					int noGoBranchTop = i +1;
					int noGoBranchBottom = i - 1;
					if (noGoBranchTop > 8) {
						noGoBranchTop -= 8;
					}
					if (noGoBranchBottom < 0) {
						noGoBranchBottom += 8;
					}
					
					tti.preReq.branches[i] = true;
					int oppositeBranch = l - 4;
					if (oppositeBranch < 0) {
						oppositeBranch = l  + 4;
					}
					tti.branches[oppositeBranch] = true;
					
					if (tti.preReq.branches[noGoBranchTop]) {
						tti.branches[notBranchTop] = true;
					}
					if (tti.preReq.branches[noGoBranchBottom]) {
						tti.branches[notBranchBottom] = true;
					}
					branch = i;
					
				
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
