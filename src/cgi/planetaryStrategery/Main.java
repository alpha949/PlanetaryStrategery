package cgi.planetaryStrategery;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Main extends StateBasedGame{
	
	public static final String TITLE = "a title";
	public static final int XSIZE = 800;
	public static final int YSIZE = 600;
	
	public Main(String nameify) {
		super(nameify);
		this.addState(new GameState());
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		this.getState(0).init(gc, this);
		
		this.enterState(0);
	}
	
	public static void main(String[] args){
		AppGameContainer gc;
		try{
			gc = new AppGameContainer(new Main(TITLE));
			gc.setDisplayMode(XSIZE, YSIZE, false);
			gc.setTargetFrameRate(60);
			gc.setShowFPS(false);
			gc.start();
		}catch(SlickException e){
			e.printStackTrace();
		}
	}
}
