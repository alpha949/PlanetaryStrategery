package cgi.planetaryStrategery;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameState extends BasicGameState {

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		
	}

	/*self-note: delta is milliseconds since last update.
	 * for instance, if you're are 2fps, then updates are 500ms apart
	 * therefore delta is 500.*/
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.drawString("Hello World", 1, 15);
	}

	@Override
	public int getID() {
		return 0;
	}

}
