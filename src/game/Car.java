package game;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import utils.Pair;

public class Car {
	//car constants
	private static final float ACCELERATION = 4000;
	private static final float BRAKING = 20000;
	private static final float TURN_FORCE_MAX = 2000;
	private static final float MASS = 1000; //mass in kg
	private static final float SIDE_TRACTION = 10000;
	private static final float ROLLING_RESISTANCE = 200;
	private static final float TURN_RADIUS = 10;
	
	private float carx;
	private float cary;
	private float carxvel;
	private float caryvel;
	private float carxacc;
	private float caryacc;
	private float carangle;
	private float carspeed;
	
	private boolean accelerating;
	private boolean braking;
	private float turnradius; //positive for right turn, negative for left turn, zero for no turn
	
	private Image carimage;
	private Image wheelimage;
	
	//list of forces, in <newton, angle> format (positive y is up and zero degrees, angle calculated clockwise)
	private Pair<Float, Float> accelerationforce;
	private Pair<Float, Float> brakingforce;
	private Pair<Float, Float> turningforce;
	private Pair<Float, Float> yfrictionalforce;
	private Pair<Float, Float> xfrictionalforce;
	
	
	public Car(float carx, float cary, float carxvel, float caryvel, float carangle) {
		this.carx = carx;
		this.cary = cary;
		this.carxvel = carxvel;
		this.caryvel = caryvel;
		this.carangle = carangle;
		this.carxacc = 0;
		this.caryacc = 0;
		try {
			carimage = new Image("/res/game/car.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateCar(int delta) {
		if (accelerating) {
			accelerationforce = new Pair<Float, Float>(ACCELERATION, 0f);
		} else {
			accelerationforce = new Pair<Float, Float>(0f, 0f);
		}
		
		if (turnradius == 0) {
			turningforce = new Pair<Float, Float>(0f, 0f);
		} else {
			float force = (MASS * carspeed * carspeed) / (turnradius);
			if (force > TURN_FORCE_MAX) {
				turningforce = new Pair<Float, Float>(TURN_FORCE_MAX, 90f);
			} else if (-force > TURN_FORCE_MAX) {
				turningforce = new Pair<Float, Float>(-TURN_FORCE_MAX, 90f);
			} else {
				turningforce = new Pair<Float, Float>(force, 90f);
			}
		}
		
		float positionalxvel = (float) (carxvel * Math.cos(Math.toRadians(carangle)) - caryvel * Math.sin(Math.toRadians(carangle)));
		float positionalyvel = (float) (carxvel * Math.sin(Math.toRadians(carangle)) + caryvel * Math.cos(Math.toRadians(carangle)));
		
		if (braking) {
			if (positionalyvel > 0.05) {
				brakingforce = new Pair<Float, Float>(BRAKING, 180f);
			} else if (positionalyvel < 0.05) {
				brakingforce = new Pair<Float, Float>(BRAKING, 0f);
			} else {
				brakingforce = new Pair<Float, Float>(0f, 0f);
			}
		} else {
			brakingforce = new Pair<Float, Float>(0f, 0f);
		}
		
		if (positionalxvel > 0) {
			xfrictionalforce = new Pair<Float, Float>(SIDE_TRACTION, -90f);
		} else if (positionalxvel < 0) {
			xfrictionalforce = new Pair<Float, Float>(SIDE_TRACTION, 90f);
		} else {
			xfrictionalforce = new Pair<Float, Float>(0f, 0f);
		}
		
		if (positionalyvel > 0) {
			yfrictionalforce = new Pair<Float, Float>(ROLLING_RESISTANCE, 180f);
		} else if (positionalyvel < 0) {
			yfrictionalforce = new Pair<Float, Float>(ROLLING_RESISTANCE, 0f);
		} else {
			yfrictionalforce = new Pair<Float, Float>(0f, 0f);
		}
		
		carxacc = (float) (
					Math.sin(Math.toRadians(accelerationforce.getR())) * accelerationforce.getL() + 
					Math.sin(Math.toRadians(brakingforce.getR())) * brakingforce.getL() + 
					Math.sin(Math.toRadians(turningforce.getR())) * turningforce.getL() +
					Math.sin(Math.toRadians(yfrictionalforce.getR())) * yfrictionalforce.getL() + 
					Math.sin(Math.toRadians(xfrictionalforce.getR())) * xfrictionalforce.getL()
					) / MASS;
		caryacc = (float) (
					Math.cos(Math.toRadians(accelerationforce.getR())) * accelerationforce.getL() + 
					Math.cos(Math.toRadians(brakingforce.getR())) * brakingforce.getL() + 
					Math.cos(Math.toRadians(turningforce.getR())) * turningforce.getL() +
					Math.cos(Math.toRadians(yfrictionalforce.getR())) * yfrictionalforce.getL() + 
					Math.cos(Math.toRadians(xfrictionalforce.getR())) * xfrictionalforce.getL()
					) / MASS;
		float unrotatedxvel = (float) (carxacc * delta / 1000.0);
		float unrotatedyvel = (float) (caryacc * delta / 1000.0);
		carxvel += unrotatedxvel * Math.cos(Math.toRadians(-carangle)) - unrotatedyvel * Math.sin(Math.toRadians(-carangle));
		caryvel += unrotatedxvel * Math.sin(Math.toRadians(-carangle)) + unrotatedyvel * Math.cos(Math.toRadians(-carangle));
		if (Math.abs(carxvel) < 0.01) {
			carxvel = 0;
		}
		if (Math.abs(caryvel) < 0.01) {
			caryvel = 0;
		}
		carx += Game.PX_PER_METER * (carxvel * ((float) delta / 1000.0));
		cary += Game.PX_PER_METER * (caryvel * ((float) delta / 1000.0));
		carspeed = (float) Math.sqrt(carxvel*carxvel + caryvel*caryvel);
		if (turnradius != 0) {
			carangle += 360 * (carspeed * (float) delta / 1000.0)/(turnradius * 2 * Math.PI);
		}
	}
	
	public void accelerate(Boolean bool) {
		accelerating = bool;
	}
	
	public void brake(Boolean bool) {
		braking = bool;
	}
	
	public void turnRadius(float turnradius) {
		this.turnradius += turnradius;
	}
	
	public float getX() {
		return carx;
	}
	
	public float getY() {
		return cary;
	}
	
	public float getXVel() {
		return carxvel;
	}
	
	public float getYVel() {
		return caryvel;
	}
	
	public float getAngle() {
		return carangle;
	}
	
	public float getTurnRadius() {
		return TURN_RADIUS;
	}
	
	public Image getImage() {
		return carimage;
	}
}
