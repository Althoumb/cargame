package menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.command.Command;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.state.transition.VerticalSplitTransition;

import utils.Timing;

public class MainMenu extends BasicGameState {

	// ID we return to class 'Application'
	public static final int ID = 1;
	
	Image menuimage;
	Image playbuttonimage;
	Image optionbuttonimage;
	
	long runtime = 0;
	long lastframetime = 0;
	long buttonpressedtime = 0;
	public final static int TRANSITION_TIME = 1000;
	
	float selectionscalefactor = 1;
	float selectionwobbletime = 2000;
	float selectionwobbleintensity = (float) 0.25;
	
	int selection = 0;
	int numberofoptions = 2;
	
	Input input;
	
	// init-method for initializing all resources
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		
		// get main menu background image
		menuimage = new Image("res/mainmenu/samplemainmenu.jpg");
		playbuttonimage = new Image("res/mainmenu/play.png");
		optionbuttonimage = new Image("res/mainmenu/options.png");
		
		// get gamecontainer input
		input = gc.getInput();
	}

	// render-method for all the things happening on-screen
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		
		// scale and draw the menu background
		menuimage.getScaledCopy(gc.getWidth(), gc.getHeight()).draw(0,0);
		
		// scale and draw the play button
		playbuttonimage = playbuttonimage.getScaledCopy(gc.getWidth() / 10, gc.getWidth() / 10);
		if (selection == 0) {
			playbuttonimage = playbuttonimage.getScaledCopy(selectionscalefactor);
		}
		playbuttonimage.draw((gc.getWidth() - playbuttonimage.getWidth()) / 2, (gc.getHeight() / 3) - (playbuttonimage.getHeight()) / 2);
		
		// scale and draw the options button
		optionbuttonimage = optionbuttonimage.getScaledCopy(gc.getWidth() / 10, gc.getWidth() / 10);
		if (selection == 1) {
			optionbuttonimage = optionbuttonimage.getScaledCopy(selectionscalefactor);
		}
		optionbuttonimage.draw((gc.getWidth() - optionbuttonimage.getWidth()) / 2, (2 * gc.getHeight() / 3) - (optionbuttonimage.getHeight()) / 2);
	}

	// update-method with all the magic happening in it
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int arg2) throws SlickException {

		// initialize timing
		if (lastframetime == 0) {
			lastframetime = Timing.getTimeMs();
		}
		
		// updates runtime with delta
		runtime += Timing.getTimeMs() - lastframetime;
		buttonpressedtime += Timing.getTimeMs() - lastframetime;
		
		// get inputs
		if (input.isKeyPressed(Options.keybindings.get("up"))){
			selection -= 1;
			if (selection < 0) {
				selection = numberofoptions - 1;
			}
			buttonpressedtime = 0;
		} else if (input.isKeyPressed(Options.keybindings.get("down"))){
			selection += 1;
			if (selection >= numberofoptions) {
				selection = 0;
			}
			buttonpressedtime = 0;
		} else if (input.isKeyPressed(Options.keybindings.get("select"))) {
			switch (selection) {
			case 0:
				sbg.enterState(3, null, new VerticalSplitTransition());
				break;
			case 1:
				sbg.enterState(4, new FadeOutTransition(), new FadeInTransition());
			}
		}
		
		// change button wobble
		selectionscalefactor = (float) (selectionwobbleintensity * Math.pow(Math.sin((buttonpressedtime * Math.PI) / (selectionwobbletime)), 2)) + 1.0f;
		
		
		// sets the time of the last frame, for timing purposes
		lastframetime = Timing.getTimeMs();
	}
	
	// Returning 'ID' from class 'MainMenu'
	@Override
	public int getID() {
		return MainMenu.ID;
	}
}
