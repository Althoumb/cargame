package game;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.command.BasicCommand;
import org.newdawn.slick.command.Command;
import org.newdawn.slick.command.InputProvider;
import org.newdawn.slick.command.InputProviderListener;
import org.newdawn.slick.command.KeyControl;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import menu.Options;
import utils.Pair;

public class Game extends BasicGameState implements InputProviderListener {
	
	private static ArrayList<Pair<Pair<Integer, Integer>, Boolean>> roadtiles = new ArrayList<Pair<Pair<Integer, Integer>, Boolean>>();
	
	private static float tilewidth = 100;
	public static final float PX_PER_METER = 50;
	
	Car car;
	Image carimage;
	
	private TrueTypeFont trueTypeFont;
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		Random rand = new Random(System.nanoTime());
		for(int x = -100; x <= 100; x++) {
			for(int y = 0; y <= 1000; y++) {
				roadtiles.add(new Pair<Pair<Integer, Integer>, Boolean>(new Pair<Integer, Integer>(x, y), rand.nextBoolean()));
			}
		}
		
		Font font = new Font("Verdana", Font.BOLD, 20);
		trueTypeFont = new TrueTypeFont(font, true);
		
		InputProvider provider = new InputProvider(gc.getInput());
		provider.addListener(this);
		
		for (String key : Options.keybindings.keySet()) {
			provider.bindCommand(new KeyControl(Options.keybindings.get(key)), new BasicCommand(key));
		}
		
		car = new Car(0, 0, 0, 0, 0);
		carimage = car.getImage();
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		g.setBackground(Color.white);
				
		g.setAntiAlias(true);
		GL11.glEnable(SGL.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA_SATURATE, GL11.GL_ONE);
		
		g.setDrawMode(Graphics.MODE_ALPHA_BLEND);
		g.setColor(Color.black);
		
		for (Pair<Pair<Integer, Integer>, Boolean> pair : roadtiles) {
			if (pair.getR()) {
				float tilex = pair.getL().getL() * tilewidth;
				float tiley = pair.getL().getR() * tilewidth;
				tilex -= car.getX() + gc.getWidth() / 2.0f;
				tiley -= car.getY() + gc.getWidth() / 2.0f;
				if ((tilex + tilewidth >= 0)&&(tilex <= gc.getWidth())) {
					if ((-tiley + tilewidth >= 0)&&(-tiley <= gc.getHeight())) {
						g.fill(new Rectangle(tilex, -tiley, tilewidth, tilewidth));
					}
				}
			}
		}
		g.setDrawMode(Graphics.MODE_NORMAL);
		g.setColor(Color.transparent);
		carimage = carimage.getScaledCopy((int) (2.4 * PX_PER_METER), (int) (4.0 * PX_PER_METER));
		carimage.setCenterOfRotation(carimage.getWidth() / 2.0f, carimage.getHeight() / 2.0f);
		carimage.setRotation(car.getAngle());
		g.drawImage(carimage, gc.getWidth() / 2.0f - carimage.getWidth() / 2.0f, gc.getHeight() / 2.0f - carimage.getHeight() / 2.0f);
		
		//trueTypeFont.drawString(20.0f, 20.0f, Double.toString(car.getAngle()) , Color.green);
		//trueTypeFont.drawString(20.0f, 40.0f, Double.toString(car.getXVel()) , Color.green);
		//trueTypeFont.drawString(20.0f, 60.0f, Double.toString(car.getYVel()) , Color.green);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		car.updateCar(delta);
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 3;
	}
	
	@Override
	public void controlPressed(Command arg0) {
		// TODO Auto-generated method stub
		String commandstring = arg0.toString();
		switch (commandstring) {
			case "[Command=up]":
				car.accelerate(true);
				break;
			case "[Command=down]":
				car.brake(true);
				break;
			case "[Command=left]":
				car.turnRadius(-car.getTurnRadius());
				break;
			case "[Command=right]":
				car.turnRadius(car.getTurnRadius());
				break;
		}
	}

	@Override
	public void controlReleased(Command arg0) {
		// TODO Auto-generated method stub
		String commandstring = arg0.toString();
		switch (commandstring) {
			case "[Command=up]":
				car.accelerate(false);
				break;
			case "[Command=down]":
				car.brake(false);
				break;
			case "[Command=left]":
				car.turnRadius(car.getTurnRadius());
				break;
			case "[Command=right]":
				car.turnRadius(-car.getTurnRadius());
				break;
		}
	}
}
