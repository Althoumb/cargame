package game;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

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
	
	private static float tilewidth = 10;
	public static final float PX_PER_METER = 10;
	Pair<Pair<Double, Double>, Double>[] prevlocs;
	
	Car car;
	Image carimage;
	
	Map map;
	Image mapimage;
	BufferedImage mapmask;
	
	private TrueTypeFont trueTypeFont;
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		// TODO Auto-generated method stub
		Pair<Double, Double> pair = new Pair<Double, Double>(0.0, 0.0);
		prevlocs = (Pair<Pair<Double, Double>, Double>[]) Array.newInstance(pair.getClass(), 20);
		for (int i = 0; i <= prevlocs.length - 1; i++){ 
			prevlocs[i] = new Pair<Pair<Double, Double>, Double>(new Pair<Double, Double>(-100000.0, -100000.0), 0.0);
		}
		
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		Random rand = new Random(System.nanoTime());
		
		map = new Map(new Image("res/maps/testmap.jpg"), new Pair<Integer, Integer>(4900, 1285));
		try {
			mapmask = ImageIO.read(new File("res/maps/testmask.jpg"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
		
		mapimage = map.getMap();
		g.drawImage(mapimage, (float) (-map.getStartingCoordinates().getL() - car.getX() + gc.getWidth() / 2.0f), (float) (-map.getStartingCoordinates().getR() + car.getY()) + gc.getHeight() / 2.0f);
		g.setColor(Color.transparent);
		
		
		carimage = carimage.getScaledCopy((int) (car.getWidth() * PX_PER_METER), (int) (car.getLength() * PX_PER_METER));
		carimage.setCenterOfRotation(carimage.getWidth() / 2.0f, carimage.getHeight() / 2.0f);
		double xpos = car.getX();
		double ypos = car.getY();
		for (int i = 0; i <= prevlocs.length - 1; i++) {
			xpos -= PX_PER_METER * prevlocs[i].getL().getL()*(1.0/60.0);
			ypos -= PX_PER_METER * prevlocs[i].getL().getR()*(1.0/60.0);
		}
		for (int i = prevlocs.length - 1; i >= 0; i--) {
			xpos += PX_PER_METER * prevlocs[i].getL().getL()*(1.0/60.0);
			ypos += PX_PER_METER * prevlocs[i].getL().getR()*(1.0/60.0);
			carimage.setRotation(prevlocs[i].getR().floatValue());
			carimage.setImageColor(1f, 1f, 1f, 1f - ((float) i / (prevlocs.length - 1)));
			g.drawImage(carimage, (float) (xpos - car.getX() + gc.getWidth() / 2.0f - carimage.getWidth() / 2.0f), (float) (car.getY() - ypos + gc.getHeight() / 2.0f - carimage.getHeight() / 2.0f));
		}
		Boolean collided;
		collided = (mapmask.getRGB((int) (map.getStartingCoordinates().getL() + car.getX()), (int) (map.getStartingCoordinates().getR() - car.getY())) == java.awt.Color.BLACK.getRGB());
		trueTypeFont.drawString(20.0f, 20.0f, String.valueOf(collided) , Color.green);
		//trueTypeFont.drawString(20.0f, 40.0f, Double.toString(car.getY() - map.getStartingCoordinates().getR()) , Color.green);
		//trueTypeFont.drawString(20.0f, 60.0f, Double.toString(car.getYVel()) , Color.green);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		car.updateCar(delta);
		for (int i = prevlocs.length - 1; i > 0; i--){ 
		     prevlocs[i] = prevlocs[i-1];
		}
		prevlocs[0] = new Pair<Pair<Double, Double>, Double>(new Pair<Double, Double>(car.getXVel(), car.getYVel()), car.getAngle());
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
