package com.ue.ps.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.ue.ps.PS;
import com.ue.ps.ui.GameplayScreen;

public class InputProcess implements InputProcessor {
	public char lastCharTyped;

	private static boolean leftMouseDown;
	private static boolean rightMouseDown;

	public static int sidePanelScrollAmount = 0;

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		lastCharTyped = character;
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == 0) {
			leftMouseDown = true;
		} else if (button == 1) {
			rightMouseDown = true;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button == 0) {
			leftMouseDown = false;
		} else if (button == 1) {
			rightMouseDown = false;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {

		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// increase speed from 1.2 to 6, a range between viewing a single planet
		// and seeing its surroundings
		// zoomammount *= 1 + .5 * sensitivity; this makes sure that the
		// multiplier is always increasing or decreasing (compared to 1.05 *
		// sensitivity, which would reverse the scrolling past 1/1.05
		// sensitivity)

		if (Gdx.input.getX() < 253 && Gdx.input.getY() - PS.viewHeight < 637) {
			sidePanelScrollAmount += amount;

		} else {
			if (amount > 0) { // Zoom out
				if (GameplayScreen.zoomAmount <= 6 && GameplayScreen.zoomAmount > 1.2) {
					GameplayScreen.zoomAmount *= 1.0 + .1 * Settings.zoomSensitivity;
				} else {
					GameplayScreen.zoomAmount *= 1.0 + .05 * Settings.zoomSensitivity;
				}

			} else if (amount < 0) { // Zoom in
				if (GameplayScreen.zoomAmount <= 6 && GameplayScreen.zoomAmount > 1.2) {
					GameplayScreen.zoomAmount *= 1 / (1.0 + .1 * Settings.zoomSensitivity);
				} else {
					GameplayScreen.zoomAmount *= 1 / (1.0 + .05 * Settings.zoomSensitivity);
				}

				// Cap on zooming in, prevents inverse view
				if (GameplayScreen.zoomAmount < 0.2) {
					GameplayScreen.zoomAmount = (float) 0.2;
				}
			}
			// System.out.println("Current zoom at "+GameplayScreen.zoomAmount
			// +", changed by "+amount);

		}

		return false;
	}

	public static boolean leftMouseClicked() {
		return leftMouseDown;
	}

	public static boolean rightMouseClicked() {
		return rightMouseDown;
	}

}
