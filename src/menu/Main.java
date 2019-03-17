package menu;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import game.Game;

public class Main extends StateBasedGame {

    // Game state identifiers
    public static final int SPLASHSCREEN = 0;
    public static final int MAINMENU     = 1;
    public static final int SELECTSCREEN = 2;
    public static final int GAME         = 3;
    public static final int OPTIONS      = 4;

    // Application Properties
    public static final int WIDTH   = 1024;
    public static final int HEIGHT  = 768;
    public static final int FPS     = 60;
    public static final String VERSION = "0.0.0";

    // Class Constructor
    public Main(String appName) {
        super(appName);
    }

    // Initialize your game states (calls init method of each gamestate, and set's the state ID)
    public void initStatesList(GameContainer gc) throws SlickException {
        // The first state added will be the one that is loaded first, when the application is launched
        this.addState(new SplashScreen());
        this.addState(new MainMenu());
        this.addState(new SelectScreen());
        this.addState(new Game());
        this.addState(new Options());
    }

    // Main Method
    public static void main(String[] args) {
        try {
            AppGameContainer app = new AppGameContainer(new Main("CarGame v" + VERSION));
            app.setDisplayMode(WIDTH, HEIGHT, false);
            app.setVSync(true);
            app.setTargetFrameRate(FPS);
            app.setShowFPS(true);
            app.start();
        } catch(SlickException e) {
            e.printStackTrace();
        }
    }
}
