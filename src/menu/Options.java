package menu;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.command.BasicCommand;
import org.newdawn.slick.command.Command;
import org.newdawn.slick.command.InputProvider;
import org.newdawn.slick.command.InputProviderListener;
import org.newdawn.slick.command.KeyControl;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class Options extends BasicGameState implements KeyListener, InputProviderListener{

	// ID we return to class 'Application'
	public static final int ID = 4;
	
	Image backgroundimage;
	
	public static Map<String, Integer> keybindings = new LinkedHashMap<String, Integer>();
	 
	// space between top of text options
	int optionydelta = 20;
	
	// initial position of options
	int ypos = 0;
	
	// selected key in hashmap
	String selectedkey;
	
	// is a key being selected?
	Boolean choosekey = false;
	
	// should input trigger commands?
	Boolean acceptinginput = true;
	
	// key to wait on
	int waitingkey;
	
	// InputProvider
	InputProvider provider;
	
	Boolean exit;
	
	TrueTypeFont ttf;

	// init-method for initializing all resources
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		backgroundimage = new Image("res/options/optionsbackground.jpg");
		
		Options.keybindings.put("up", Input.KEY_W);
		Options.keybindings.put("down", Input.KEY_S);
		Options.keybindings.put("left", Input.KEY_A);
		Options.keybindings.put( "right", Input.KEY_D);
		Options.keybindings.put("select", Input.KEY_ENTER);
		Options.keybindings.put("exit", Input.KEY_ESCAPE);
		
		Font font = new Font("Verdana", Font.BOLD, 20);
		ttf = new TrueTypeFont(font, true);
	}
	
	// enter-method for starting things when state entered
		@Override
		public void enter(GameContainer gc, StateBasedGame sbg) {
			provider = new InputProvider(gc.getInput());
			provider.addListener(this);
			
			exit = false;
			
			for (String key : Options.keybindings.keySet()) {
				provider.bindCommand(new KeyControl(Options.keybindings.get(key)), new BasicCommand(key));
			}
		}
	// render-method for all the things happening on-screen
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// scale and draw the menu background
		//backgroundimage.getScaledCopy(gc.getWidth(), gc.getHeight()).draw(0,0);
		
		for(String key : Options.keybindings.keySet()) {
			drawString(key.concat(":"), gc.getHeight() / 2 + ypos, false, ttf, gc);
			if (Options.keybindings.get(key) != -1) {
				drawString(" ".concat(Input.getKeyName(Options.keybindings.get(key))), gc.getHeight() / 2 + ypos, true, ttf, gc);
			}
			if (ypos == 0) {
				selectedkey = key;
			}
			ypos += optionydelta;
		}
		ypos -= optionydelta * Options.keybindings.size();
		
		g.draw(new Rectangle(0, gc.getHeight() / 2 - optionydelta / 2, gc.getWidth() + 1, optionydelta));
	}

	private void drawString(String string, int Height, boolean right, TrueTypeFont ttf, GameContainer gc) {
		if (right) {
			ttf.drawString(gc.getWidth() / 2, Height - ttf.getHeight(string) / 2, string);
		} else {
			ttf.drawString(gc.getWidth() / 2 - ttf.getWidth(string), Height - ttf.getHeight(string) / 2, string);
		}
	}
	
	// update-method with all the magic happening in it
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int arg2) throws SlickException {		
		if (exit) {
			provider.removeListener(this);
			sbg.enterState(1, new FadeOutTransition(), new FadeInTransition());
		}
	}

	// Returning 'ID' from class 'Options'
	@Override
	public int getID() {
		return Options.ID;
	}
	
	@SuppressWarnings("unchecked")
	private void resetBindings() {
		List<Command> commands = provider.getUniqueCommands();
		for (Command command : commands) {
			provider.clearCommand(command);
		}
		for (String key : Options.keybindings.keySet()) {
			provider.bindCommand(new KeyControl(Options.keybindings.get(key)), new BasicCommand(key));
		}
	}
	
	@Override
	public void keyPressed(int key, char c) {
		if (choosekey) {
			acceptinginput = false;
			for(String command : Options.keybindings.keySet()) {
				if (Options.keybindings.get(command).equals(key)){
					Options.keybindings.replace(command, key, -1);
				}
			}
			Options.keybindings.replace(selectedkey, Options.keybindings.get(selectedkey), key);
			resetBindings();
			choosekey = false;
			waitingkey = key;
		}
	}
	
	@Override
	public void keyReleased(int key, char c) {
		if (key == waitingkey) {
			acceptinginput = true;
		}
	}
	
	@Override
	public void controlPressed(Command arg0) {
		// TODO Auto-generated method stub
		String commandstring = arg0.toString();
		if (!choosekey && acceptinginput) {
			switch (commandstring) {
			case "[Command=up]":
				ypos += optionydelta;
				break;
			case "[Command=down]":
				ypos -= optionydelta;
				break;
			case "[Command=select]":
				choosekey = true;
				break;
			case "[Command=exit]":
				exit = true;
				break;
			}
		}
	}

	@Override
	public void controlReleased(Command arg0) {
		// TODO Auto-generated method stub
		String commandstring = arg0.toString();
		switch (commandstring) {
		}
	}
}
