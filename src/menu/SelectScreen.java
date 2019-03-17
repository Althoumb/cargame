package menu;

import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
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

import game.Game;
import utils.Pair;

public class SelectScreen extends BasicGameState implements KeyListener, InputProviderListener {

	// ID we return to class 'Application'
	public static final int ID = 2;
	
	File mapfolder = new File("res/maps");
	File[] maps = mapfolder.listFiles();
	private int levelID = 0;
	
	boolean select;
	boolean exit;
	
	// space between top of text options
	int optionydelta = 20;
	
	Image backgroundimage;	

	TrueTypeFont ttf;

	// init-method for initializing all resources
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		backgroundimage = new Image("res/selectscreen/selectbackground.jpg");		

		Font font = new Font("Verdana", Font.BOLD, 20);
		ttf = new TrueTypeFont(font, true);
	}
	
	// enter-method for starting things when state entered
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) {
		select = false;
		exit = false;
		
		InputProvider provider = new InputProvider(gc.getInput());
		provider.addListener(this);
		
		for (String key : Options.keybindings.keySet()) {
			provider.bindCommand(new KeyControl(Options.keybindings.get(key)), new BasicCommand(key));
		}
	}
	
	// render-method for all the things happening on-screen
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		// scale and draw the menu background
		backgroundimage.getScaledCopy(gc.getWidth(), gc.getHeight()).draw(0,0);
		
		g.draw(new Rectangle(0, gc.getHeight() / 2 - optionydelta / 2, gc.getWidth() + 1, optionydelta));
		
		for (int i = 0; i < maps.length; i++) {
			drawString(maps[i].getName(), gc.getHeight() / 2 + (i - levelID) * optionydelta, true, ttf, gc);
		}
	}
	
	private void drawString(String string, int Height, boolean right, TrueTypeFont ttf, GameContainer gc) {
		if (right) {
			ttf.drawString(gc.getWidth() / 2, Height - ttf.getHeight(string) / 2, string);
		} else {
			ttf.drawString(gc.getWidth() / 2 - ttf.getWidth(string), Height - ttf.getHeight(string) / 2, string);
		}
	}
	
	private void selectMap(int levelID) {
		Game.setLevel(maps[levelID].getName());
	}

	// update-method with all the magic happening in it
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int arg2) throws SlickException {
		if (select) {
			sbg.enterState(3, new FadeOutTransition(), new FadeInTransition());
		}
		if (exit) {
			sbg.enterState(1, new FadeOutTransition(), new FadeInTransition());
		}
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
				if (levelID > 0) {
					levelID -= 1;
				}
				break;
			case "[Command=down]":
				if (levelID < maps.length - 1) {
					levelID += 1;
				}
				break;
			case "[Command=select]":
				selectMap(levelID);
				select = true;
				break;
			case "[Command=exit]":
				exit = true;
				break;
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
