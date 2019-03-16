package menu;

import java.util.HashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.command.BasicCommand;
import org.newdawn.slick.command.Command;
import org.newdawn.slick.command.InputProvider;
import org.newdawn.slick.command.InputProviderListener;
import org.newdawn.slick.command.KeyControl;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import utils.Pair;

public class SelectScreen extends BasicGameState implements KeyListener, InputProviderListener {

	// ID we return to class 'Application'
	public static final int ID = 2;
	
	Image backgroundimage;
	
	HashMap<Integer, Pair<Integer, Integer>> cursorcoordinates = new HashMap<Integer, Pair<Integer, Integer>>();
	HashMap<Integer, Pair<Integer, Integer>> cursorspeed = new HashMap<Integer, Pair<Integer, Integer>>();
	int cursorvelocity = 4;
	
	int players = 1;

	// init-method for initializing all resources
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		backgroundimage = new Image("res/selectscreen/selectbackground.jpg");
	}
	
	// enter-method for starting things when state entered
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		InputProvider provider = new InputProvider(gc.getInput());
		provider.addListener(this);
		
		for (String key : Options.keybindings.keySet()) {
			provider.bindCommand(new KeyControl(Options.keybindings.get(key)), new BasicCommand(key));
		}
		
		for (int i = 1; i <= players; i++) {
			cursorcoordinates.put(i, new Pair<Integer, Integer>(gc.getWidth() / 2, gc.getHeight() / 2));
			cursorspeed.put(i, new Pair<Integer, Integer>(0, 0));
		}
	}
	
	// render-method for all the things happening on-screen
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// scale and draw the menu background
		backgroundimage.getScaledCopy(gc.getWidth(), gc.getHeight()).draw(0,0);
		g.drawRect(cursorcoordinates.get(1).getL(), cursorcoordinates.get(1).getR(), 10, 10);
	}

	// update-method with all the magic happening in it
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int arg2) throws SlickException {
		cursorcoordinates.get(1).setR(cursorcoordinates.get(1).getR() + cursorspeed.get(1).getR());
		cursorcoordinates.get(1).setL(cursorcoordinates.get(1).getL() + cursorspeed.get(1).getL());
	}

	// Returning 'ID' from class 'MainMenu'
	@Override
	public int getID() {
		return SelectScreen.ID;
	}

	@Override
	public void controlPressed(Command arg0) {
		// TODO Auto-generated method stub
		String commandstring = arg0.toString();
		switch (commandstring) {
			case "[Command=up]":
				cursorspeed.get(1).setR(cursorspeed.get(1).getR() + (-cursorvelocity));
				break;
			case "[Command=down]":
				cursorspeed.get(1).setR(cursorspeed.get(1).getR() + (cursorvelocity));
				break;
			case "[Command=left]":
				cursorspeed.get(1).setL(cursorspeed.get(1).getL() + (-cursorvelocity));
				break;
			case "[Command=right]":
				cursorspeed.get(1).setL(cursorspeed.get(1).getL() + (cursorvelocity));
				break;
		}
	}

	@Override
	public void controlReleased(Command arg0) {
		// TODO Auto-generated method stub
		String commandstring = arg0.toString();
		switch (commandstring) {
			case "[Command=up]":
				cursorspeed.get(1).setR(cursorspeed.get(1).getR() + (cursorvelocity));
				break;
			case "[Command=down]":
				cursorspeed.get(1).setR(cursorspeed.get(1).getR() + (-cursorvelocity));
				break;
			case "[Command=left]":
				cursorspeed.get(1).setL(cursorspeed.get(1).getL() + (cursorvelocity));
				break;
			case "[Command=right]":
				cursorspeed.get(1).setL(cursorspeed.get(1).getL() + (-cursorvelocity));
				break;
		}
	}
}
