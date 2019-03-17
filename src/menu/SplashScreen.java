package menu;

import java.io.File;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;

import utils.FileGetter;
import utils.Timing;

public class SplashScreen extends BasicGameState {

	// ID we return to class 'Application'
	public static final int ID = 0;

	ArrayList<Image> images = new ArrayList<Image>();
	Image currentsplash = null;
	
	long splashtime = 3000;
	long fadeintime = 1000;
	long fadeouttime = 1000;
	float imagealpha = 0;
	
	long runtime = 0;
	long lastframetime = -1;
	
	Input input;
	
	// init-method for initializing all resources
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		// gets list of splash files in the splashes folder
		ArrayList<File> imagefiles = FileGetter.filesInFolder(new File("res/splashes"));
		
		// gets an list of splash images
		for(File imagefile: imagefiles) {
			images.add(new Image(imagefile.toString()));
		}
		
		// chooses first splash
		currentsplash = images.get(0);
		
		// get gamecontainer input
		input = gc.getInput();
	}

	// render-method for all the things happening on-screen
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		
		// stretches the splash to fill the entire screen
		currentsplash = currentsplash.getScaledCopy(gc.getWidth(), gc.getHeight());
		
		// sets image transparency, used to fade splashes
		currentsplash.setAlpha(imagealpha);
		
		// draws splash
		currentsplash.draw(0,0);
	}

	// update-method with all the magic happening in it
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int arg2) throws SlickException {
		// initialize timing
		if (lastframetime == -1) {
			lastframetime = Timing.getTimeMs();
		}
		
		// updates runtime with delta
		runtime += Timing.getTimeMs() - lastframetime;
		
		// sets splash to be displayed based on the run time
		// additionally changes state at end of splashes or when escape key pressed
		if (((int) Math.floorDiv(runtime, splashtime) < images.size())&&(!(input.isKeyPressed(Input.KEY_ESCAPE)))) {
			currentsplash = images.get((int) Math.floorDiv(runtime, splashtime)); 
		} else {
			sbg.enterState(1, null, new FadeInTransition(Color.transparent, MainMenu.TRANSITION_TIME));
		}
		
		// fades in the splash
		if (runtime % splashtime <= fadeintime) {
			imagealpha = (float) (runtime % splashtime) / fadeintime;
		}
		
		// fades out the splash
		if (runtime % splashtime >= splashtime - fadeouttime) {
			imagealpha = (float) (splashtime -(runtime % splashtime)) / (fadeouttime);
		}
		
		// sets the time of the last frame, for timing purposes
		lastframetime = Timing.getTimeMs();
	}

	// Returning 'ID' from class 'MainMenu'
	@Override
	public int getID() {
		return SplashScreen.ID;
	}

}