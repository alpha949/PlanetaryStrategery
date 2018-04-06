package com.ue.ps.ui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.ue.ps.BaseActor;
import com.ue.ps.PS;
import com.ue.ps.Utils;
import com.ue.ps.systems.GameServerClient;

public class TechTreePanel extends BaseActor {

	public boolean isOpen;
	private static final int itemDist = 150;
	private static Texture techLineTexture = Images.getImg("techLine");
	private static ArrayList<TechTreeItem> items = new ArrayList<TechTreeItem>();

	private boolean isSetup;

	public TechTreePanel() {
		super("assets/grayBack.png");
		items.add(TechTreeItem.baseTech);
		items.get(0).setCenter(PS.viewWidth / 2, 64);
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
		int branch = 12;
		boolean success = false;
		
		
		for (int i = 0; i < 8; i++) {
			
				
				
				
				
				
			if (!tti.preReq.branches[i]) {
				// find no goes from tii
				int notBranchTop = i + 3;
				int notBranchBottom = i - 3;
				if (notBranchTop > 7) {
					notBranchTop -= 8;
				}
				if (notBranchBottom < 0) {
					notBranchBottom += 8;

				}
				// find no goes from preReq
				int noGoBranchTop = i + 1;
				int noGoBranchBottom = i - 1;
				if (noGoBranchTop > 7) {
					noGoBranchTop -= 8;
				}
				if (noGoBranchBottom < 0) {
					noGoBranchBottom += 8;
				}

				tti.preReq.branches[i] = true;
				int oppositeBranch = i - 4;
				if (oppositeBranch < 0) {
					oppositeBranch = i + 4;
				}
				tti.branches[oppositeBranch] = true;

		
				tti.branches[notBranchTop] = true;
			
		
				tti.branches[notBranchBottom] = true;
				
				tti.preReq.branches[noGoBranchTop] = true;
				
				tti.preReq.branches[noGoBranchBottom] = true;
				
				
				
				branch = i;

				success = true;
				break;
			}
			

		}

		
		if (tti.preReq != null) {
			if (tti.preReq == TechTreeItem.baseTech) {
				tti.preReq.center.x = PS.viewWidth / 2;
				tti.preReq.center.y = PS.viewHeight / 2;
			}
			
			if (branch == 12) {
				System.out.println("Couldn't place: " + tti.name);
			}

			float angle = 360 / 8 * branch;
			Vector2 ttiPos = Utils.polarToRect(itemDist, angle, tti.preReq.center);
			tti.setCenter(ttiPos.x, ttiPos.y);
			this.addActor(tti);
			BaseActor techLine = new BaseActor(techLineTexture);
			techLine.setPosition(tti.preReq.center.x, tti.preReq.center.y);
			techLine.setRotation(angle);
			techLine.setOrigin(0, 0);
			this.addActor(techLine);
			

			
		}
		for (int i = 0; i < tti.branches.length; i++) {
			if (tti.branches[i]) {
				System.out.println(tti.name + ": " + i);
			}
		}

	}

	public static void registerTech(TechTreeItem tti) {
		items.add(tti);

	}
	
	public void update(BaseActor mouse) {
		for (TechTreeItem tti : items) {
			if (tti.Pressed(mouse.getBoundingRectangle())) {
				tti.purchase();
				
			}
			if (tti.preReq != null) {
				tti.unlock();
			}
			
		
		}
		
	}
}
