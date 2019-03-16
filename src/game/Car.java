package game;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import utils.Pair;

public class Car {
	//car constants
	private static final double ACCELERATION = 24798.7183;
	private static final double BRAKING = 50000;
	private static final double TURN_FORCE_MAX = 5000;
	private static final double MASS = 2107.97982; //mass in kg
	private static final double SIDE_TRACTION = 40000;
	private static final double ROLLING_RESISTANCE = 1000;
	private static final double TURN_RADIUS = 15;
	private static final double WIDTH = 2.5;
	private static final double LENGTH = 4;
	
	private double carx;
	private double cary;
	private double carxvel;
	private double caryvel;
	private double carxacc;
	private double caryacc;
	private double carangle;
	private double carspeed;
	
	private boolean accelerating;
	private boolean braking;
	private double turnradius; //positive for right turn, negative for left turn, zero for no turn
	
	private Image carimage;
	private Image wheelimage;
	
	//list of forces, in <newton, angle> format (positive y is up and zero degrees, angle calculated clockwise)
	private Pair<Double, Double> accelerationforce;
	private Pair<Double, Double> brakingforce;
	private Pair<Double, Double> turningforce;
	private Pair<Double, Double> yfrictionalforce;
	private Pair<Double, Double> xfrictionalforce;
	
	
	public Car(double carx, double cary, double carxvel, double caryvel, double carangle, Image carimage) {
		this.carx = carx;
		this.cary = cary;
		this.carxvel = carxvel;
		this.caryvel = caryvel;
		this.carangle = carangle;
		this.carxacc = 0;
		this.caryacc = 0;
		this.carimage = carimage;
	}
	
	public void updateCar(int delta) {
		if (accelerating) {
			accelerationforce = new Pair<Double, Double>(ACCELERATION, 0.0);
		} else {
			accelerationforce = new Pair<Double, Double>(0.0, 0.0);
		}
		
		if (turnradius == 0) {
			turningforce = new Pair<Double, Double>(0.0, 0.0);
		} else {
			double force = (MASS * carspeed * carspeed) / (turnradius);
			if (force > TURN_FORCE_MAX) {
				turningforce = new Pair<Double, Double>(TURN_FORCE_MAX, 90.0);
			} else if (-force > TURN_FORCE_MAX) {
				turningforce = new Pair<Double, Double>(-TURN_FORCE_MAX, 90.0);
			} else {
				turningforce = new Pair<Double, Double>(force, 90.0);
			}
		}
		
		double positionalxvel = (double) (carxvel * Math.cos(Math.toRadians(carangle)) - caryvel * Math.sin(Math.toRadians(carangle)));
		double positionalyvel = (double) (carxvel * Math.sin(Math.toRadians(carangle)) + caryvel * Math.cos(Math.toRadians(carangle)));
		
		if (Math.abs(positionalxvel) < (SIDE_TRACTION) * ((double) delta / 1000.0) / MASS){
			xfrictionalforce = new Pair<Double, Double>(-MASS * positionalxvel / ((double) delta / 1000.0), 90.0);
		} else {
			if (positionalxvel > 0) {
				xfrictionalforce = new Pair<Double, Double>(SIDE_TRACTION, -90.0);
			} else if (positionalxvel < 0) {
				xfrictionalforce = new Pair<Double, Double>(SIDE_TRACTION, 90.0);
			} else {
				xfrictionalforce = new Pair<Double, Double>(0.0, 0.0);
			}
		}
		
		if ((Math.abs(positionalyvel) < (BRAKING+ROLLING_RESISTANCE) * ((double) delta / 1000.0) / MASS)&&(braking)){
			braking = false;
			brakingforce = new Pair<Double, Double>(0.0, 0.0);
			yfrictionalforce = new Pair<Double, Double>(-MASS * positionalyvel / ((double) delta / 1000.0), 0.0);
		} else if ((Math.abs(positionalyvel) < (ROLLING_RESISTANCE) * ((double) delta / 1000.0) / MASS)&&(!braking)){
			brakingforce = new Pair<Double, Double>(0.0, 0.0);
			yfrictionalforce = new Pair<Double, Double>(-MASS * positionalyvel / ((double) delta / 1000.0), 0.0);
		} else {
			if (braking) {
				if (positionalyvel > 0) {
					brakingforce = new Pair<Double, Double>(BRAKING, 180.0);
				} else if (positionalyvel < 0) {
					brakingforce = new Pair<Double, Double>(BRAKING, 0.0);
				} else {
					brakingforce = new Pair<Double, Double>(0.0, 0.0);
				}
			} else {
				brakingforce = new Pair<Double, Double>(0.0, 0.0);
			}		
			
			if (positionalyvel > 0) {
				yfrictionalforce = new Pair<Double, Double>(ROLLING_RESISTANCE, 180.0);
			} else if (positionalyvel < 0) {
				yfrictionalforce = new Pair<Double, Double>(ROLLING_RESISTANCE, 0.0);
			} else {
				yfrictionalforce = new Pair<Double, Double>(0.0, 0.0);
			}
		}
		
		carxacc = (double) (
					Math.sin(Math.toRadians(accelerationforce.getR())) * accelerationforce.getL() + 
					Math.sin(Math.toRadians(brakingforce.getR())) * brakingforce.getL() + 
					Math.sin(Math.toRadians(turningforce.getR())) * turningforce.getL() +
					Math.sin(Math.toRadians(yfrictionalforce.getR())) * yfrictionalforce.getL() + 
					Math.sin(Math.toRadians(xfrictionalforce.getR())) * xfrictionalforce.getL()
					) / MASS;
		caryacc = (double) (
					Math.cos(Math.toRadians(accelerationforce.getR())) * accelerationforce.getL() + 
					Math.cos(Math.toRadians(brakingforce.getR())) * brakingforce.getL() + 
					Math.cos(Math.toRadians(turningforce.getR())) * turningforce.getL() +
					Math.cos(Math.toRadians(yfrictionalforce.getR())) * yfrictionalforce.getL() + 
					Math.cos(Math.toRadians(xfrictionalforce.getR())) * xfrictionalforce.getL()
					) / MASS;
		double unrotatedxvel = (double) (carxacc * delta / 1000.0);
		double unrotatedyvel = (double) (caryacc * delta / 1000.0);
		carxvel += unrotatedxvel * Math.cos(Math.toRadians(-carangle)) - unrotatedyvel * Math.sin(Math.toRadians(-carangle));
		caryvel += unrotatedxvel * Math.sin(Math.toRadians(-carangle)) + unrotatedyvel * Math.cos(Math.toRadians(-carangle));
		carx += Game.PX_PER_METER * (carxvel * ((double) delta / 1000.0));
		cary += Game.PX_PER_METER * (caryvel * ((double) delta / 1000.0));
		if (Math.abs(carxvel) < 0.05) {
			carxvel = 0;
		}
		if (Math.abs(caryvel) < 0.05) {
			caryvel = 0;
		}
		carspeed = (double) Math.sqrt(carxvel*carxvel + caryvel*caryvel);
		if (turnradius != 0) {
			carangle += 360 * (carspeed * (double) delta / 1000.0)/(turnradius * 2 * Math.PI);
		}
	}
	
	public void accelerate(Boolean bool) {
		accelerating = bool;
	}
	
	public void brake(Boolean bool) {
		braking = bool;
	}
	
	public void turnRadius(double turnradius) {
		this.turnradius += turnradius;
	}
	
	public double getX() {
		return carx;
	}
	
	public double getY() {
		return cary;
	}
	
	public double getXVel() {
		return carxvel;
	}
	
	public double getYVel() {
		return caryvel;
	}
	
	public double getAngle() {
		return carangle;
	}
	
	public double getTurnRadius() {
		return TURN_RADIUS;
	}
	
	public Image getImage() {
		return carimage;
	}
	
	public double getWidth() {
		return WIDTH;
	}
	
	public double getLength() {
		return LENGTH;
	}
}
