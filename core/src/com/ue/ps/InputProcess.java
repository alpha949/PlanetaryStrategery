package com.ue.ps;

import com.badlogic.gdx.InputProcessor;

public class InputProcess implements InputProcessor {
	public char lastCharTyped;

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
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
		if (amount > 0) {
			GameplayScreen.zoomAmount *= 1.05;
			if (GameplayScreen.zoomAmount < 1) {
				GameplayScreen.zoomAmount += .05;
			}
		} else if (amount < 0) {
			GameplayScreen.zoomAmount *= 1 / 1.05;
			if (GameplayScreen.zoomAmount < 1) {
				GameplayScreen.zoomAmount -= .05;
			}
		}

		return false;
	}

}
