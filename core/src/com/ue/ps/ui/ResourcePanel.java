package com.ue.ps.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.ue.ps.BaseActor;
import com.ue.ps.Faction;
import com.ue.ps.PS;
import com.ue.ps.Player;

public class ResourcePanel extends BaseActor {

	private Faction faction;
	private Player player;

	private Label peopleCounter = new Label("3", PS.font);
	private Label resourceCounter = new Label("6", PS.font);
	private Label techCounter = new Label("9", PS.font);

	private BaseActor peopleIcon = new BaseActor("assets/personel.png");
	private BaseActor resourceIcon = new BaseActor("assets/resource.png");
	private BaseActor techIcon = new BaseActor("assets/tech.png");
	private Vector2 copiedMousePos = new Vector2();
	private BaseActor localMouseBlot = new BaseActor("assets/stageMouseBlot.png");

	public ResourcePanel(Faction f, Player p) {
		super("assets/resourcePanel.png");
		this.faction = f;
		this.player = p;
		BaseActor factionIcon = new BaseActor("assets/Red.png");
		factionIcon.setPosition(1, 1);
		this.addActor(factionIcon);

		peopleIcon = new BaseActor("assets/personel.png");
		peopleIcon.setPosition(102, 33);
		this.addActor(peopleIcon);

		resourceIcon = new BaseActor("assets/resource.png");
		resourceIcon.setPosition(495, 33);
		this.addActor(resourceIcon);

		techIcon = new BaseActor("assets/tech.png");
		techIcon.setPosition(888, 33);
		this.addActor(techIcon);

		peopleCounter.setPosition(102 + 42, 39);
		resourceCounter.setPosition(495 + 42, 39);
		techCounter.setPosition(888 + 42, 39);

		this.addActor(peopleCounter);
		this.addActor(resourceCounter);
		this.addActor(techCounter);
		this.addActor(localMouseBlot);

	}

	public void update(Stage uiStage) {
		copiedMousePos.x = GameplayScreen.mouseBlot.getX();
		copiedMousePos.y = PS.viewHeight - GameplayScreen.mouseBlot.getY();

		Vector2 localMousePos = this.stageToLocalCoordinates(uiStage.screenToStageCoordinates(copiedMousePos));
		localMouseBlot.setPosition(localMousePos.x, localMousePos.y);

		if (this.techIcon.getBoundingRectangle().contains(localMouseBlot.center) && Gdx.input.justTouched()) {
			GameplayScreen.techTreePanel.open();

		}
	}

}
