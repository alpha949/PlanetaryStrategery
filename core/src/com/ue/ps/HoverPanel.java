package com.ue.ps;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class HoverPanel extends BaseActor{
	
	private static Texture chunkTexture = Utils.getImg("chunkMiddle");
	private static Texture chunkTopTexture = Utils.getImg("chunkTop");
	private static Texture chunkBottomTexture = Utils.getImg("chunkBottom");
	
	public static String[] standardInfo = {"Name", "Health"};
	
	private ArrayList<Label> info = new ArrayList<Label>();
	private ArrayList<BaseActor> background = new ArrayList<BaseActor>();
	private BaseActor top;
	private BaseActor bottom;
	public HoverPanel(String[] info, String...data) {
		super(Utils.emptyTexture);
		
		
		for (int i = 0; i < info.length; i++) {
			BaseActor background;
			if (i == 0) {
				background = new BaseActor(chunkBottomTexture);
				bottom = background;
			} else if (i == info.length -1) {
				background = new BaseActor(chunkTopTexture);
				top = background;
			} else {
				background = new BaseActor(chunkTexture);
			}
			
			 
			background.setPosition(0, -i * 18);
			this.addActor(background);
			Label l = new Label(info[i] + ": " + data[i], PS.font);
			l.setPosition(3, -i * 18);
			l.setZIndex(1000);
			this.addActor(l);
			this.info.add(l);
		}
	}
	
	public void addInfo(String info, String data) {
		BaseActor newBack = new BaseActor(chunkTexture);
		Label newLabel = new Label(info + ": " + data, PS.font);
		this.info.add(newLabel);
		bottom.setPosition(0, -(this.info.size()-1 )* 18);
		newLabel.setPosition(3, -(this.info.size()-1 )* 18);
		newBack.setPosition(0, -(this.info.size()-2 )* 18);
		this.info.get(this.info.size()-2).setZIndex(1000);
		this.addActor(newBack);
		this.addActor(newLabel);
	}
	
	
}
