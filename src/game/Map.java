package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.newdawn.slick.Image;

import utils.Pair;

public class Map {
	private Image map;
	private Pair<Integer, Integer> startingcoordinates;
	
	public Map(Image map, Pair<Integer, Integer> startingcoordinates) {
		this.map = map;
		this.startingcoordinates = startingcoordinates;
	}
	
	public Image getMap() {
		return map;
	}
	
	public Pair<Integer, Integer> getStartingCoordinates(){
		return startingcoordinates;
	}
}
