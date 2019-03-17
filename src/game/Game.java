package game;

import java.awt.Font;
import java.io.File;
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
	
	private static float tilewidth = 40;
	public static final float PX_PER_METER = 20;
	Pair<Pair<Double, Double>, Double>[] prevlocs;
	
	Car car;
	Image carimage;
	
	Map map;
	
	private TrueTypeFont trueTypeFont;
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		Random rand = new Random(System.nanoTime());
		
		map = new Map(new File("res/maps/testmap.map"));
		
		Font font = new Font("Verdana", Font.BOLD, 20);
		trueTypeFont = new TrueTypeFont(font, true);
		
		InputProvider provider = new InputProvider(gc.getInput());
		provider.addListener(this);
		
		for (String key : Options.keybindings.keySet()) {
			provider.bindCommand(new KeyControl(Options.keybindings.get(key)), new BasicCommand(key));
		}
		
		try {
			carimage = new Image("res/game/car.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		car = new Car(0, 0, 0, 0, 0, carimage);
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		g.setBackground(Color.pink);
		g.setColor(Color.white);
		
		int verttiles = (int) Math.ceil(gc.getHeight() * 2.0 / tilewidth);
		int hortiles = (int) Math.ceil(gc.getWidth() * 2.0 / tilewidth);
		int startx = map.getStartingTile().getL();
		int starty = map.getStartingTile().getR();
		for (int x = -hortiles + startx + (int) Math.ceil(car.getX() / tilewidth); x <= hortiles + startx + (int) Math.ceil(car.getX() / tilewidth); x++) {
			for (int y = -verttiles + starty + (int) Math.ceil(car.getY() / tilewidth); y <= verttiles + starty + (int) Math.ceil(car.getY() / tilewidth); y++) {
				if (((x > 0)&&(x < map.getRoadTiles().length))&&((y > 0)&&(y < map.getRoadTiles()[0].length))) {
					if (map.getRoadTiles()[x][y]) {
						float tilex = (x - startx) * tilewidth;
						float tiley = (y - starty) * tilewidth;
						tilex -= car.getX() - gc.getWidth() / 2.0f;
						tiley -= car.getY() + gc.getHeight() / 2.0f;
						if ((tilex + tilewidth >= 0)&&(tilex <= gc.getWidth())) {
							if ((-tiley + tilewidth >= 0)&&(-tiley <= gc.getHeight())) {
								g.fill(new Rectangle(tilex, -tiley, tilewidth, tilewidth));
							}
						}
					}
				}
			}
		}
		
		g.setColor(Color.transparent);
		carimage = carimage.getScaledCopy((int) (car.getWidth() * PX_PER_METER), (int) (car.getLength() * PX_PER_METER));
		carimage.setCenterOfRotation(carimage.getWidth() / 2.0f, carimage.getHeight() / 2.0f);
		carimage.setRotation((float) car.getAngle());
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
