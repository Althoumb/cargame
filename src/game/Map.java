package game;

import java.util.ArrayList;

import utils.Pair;

public class Map {
	private static ArrayList<Pair<Pair<Integer, Integer>, Boolean>> roadtiles = new ArrayList<Pair<Pair<Integer, Integer>, Boolean>>();
	
	public Map(ArrayList<Pair<Pair<Integer, Integer>, Boolean>> roadtiles) {
		this.roadtiles = roadtiles;
	}
	
	public ArrayList<Pair<Pair<Integer, Integer>, Boolean>> getRoadTiles() {
		return roadtiles;
	}
}
