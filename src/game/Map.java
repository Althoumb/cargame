package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import utils.Pair;

public class Map {
	private boolean[][] roadtiles;
	private Pair<Integer, Integer> startingtile;
	
	public Map(boolean[][] roadtiles, Pair<Integer, Integer> startingtile) {
		this.roadtiles = roadtiles;
		this.startingtile = startingtile;
	}
	
	public Map(File file) {
		Scanner s;
		try {
			s = new Scanner(file);
			int width = Integer.parseInt(s.nextLine());
			int height = Integer.parseInt(s.nextLine());
			int startx = Integer.parseInt(s.nextLine());
			int starty = Integer.parseInt(s.nextLine());
			startingtile = new Pair<Integer, Integer>(startx, starty);
			roadtiles = new boolean[width][height];
			while (s.hasNext()){
				String[] split = s.nextLine().split(", ");
				roadtiles[Integer.parseInt(split[0])][Integer.parseInt(split[1])] = Boolean.parseBoolean(split[2]);
			}
			s.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean[][] getRoadTiles() {
		return roadtiles;
	}
	
	public Pair<Integer, Integer> getStartingTile(){
		return startingtile;
	}
}
