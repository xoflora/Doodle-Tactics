package map;

import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.LinkedList;
import java.util.PriorityQueue;

import util.Heap;

/**
 * 
 * @author rroelke
 * a Map is a segment of the game world during which gameplay occurs
 */
public class Map {
	
	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;
	
	private Tile _overflow;
	private Tile[][] _map;
	String _name;
	
	/**
	 * parses a map to generate a map instance
	 * @param file the file to parse from
	 * @return
	 */
	public static Map map(String file) throws InvalidMapException {
		return null;
	}
	
	/**
	 * @param source
	 * @return the tile to the north of the source tile
	 */
	public Tile getNorth(Tile source) {
		return _map[source.x()][source.y() - 1];
	}
	
	/**
	 * @param source
	 * @return the tile to the east of the source tile
	 */
	public Tile getEast(Tile source) {
		return _map[source.x() + 1][source.y()];
	}
	
	/**
	 * @param source
	 * @return the tile to the south of the source tile
	 */
	public Tile getSouth(Tile source) {
		return _map[source.x()][source.y() + 1];
	}
	
	/**
	 * @param source
	 * @return the tile to the west of the source tile
	 */
	public Tile getWest(Tile source) {
		return _map[source.x() - 1][source.y() + 1];
	}
	
	/**
	 * returns an estimate for the distance between two tiles
	 * heuristic function used for A* pathfinding
	 * @param source the start tile
	 * @param dest the end tile
	 * @return an estimate of the distance between the two tiles
	 */
	private int estimateDistance(Tile source, Tile dest) {
		return Math.abs(source.x() - dest.x()) + Math.abs(source.y() - dest.y());
	}
	
	/**
	 * generates a path from the source tile to the destination tile
	 * @param source the tile to start from
	 * @param dest the tile to end at
	 * @return an ordered list of all tiles in the path
	 */
	public List<Tile> getPath(Tile source, Tile dest) {
		final Hashtable<Tile, Integer> distances = new Hashtable<Tile, Integer>();
		Hashtable<Tile, Tile> previous = new Hashtable<Tile, Tile>();
		Hashtable<Tile, Integer> heapPositions = new Hashtable<Tile, Integer>();
		
		Heap<Tile> heap = new Heap<Tile>(estimateDistance(source, dest),
				new Comparator<Tile>() {
					@Override
					public int compare(Tile o1, Tile o2) {
						int d1 = distances.get(o1);
						int d2 = distances.get(o2);
						if (d1 < d2)
							return -1;
						else if (d1 == d2)
							return 0;
						else
							return 1;
					}
		});
		
		distances.put(source, 0);
		
		Tile north, east, south, west;
		Tile consider = source;
		Integer dist, compare;
		while (distances.get(dest) == null) {

			try {
				north = getNorth(consider);
				if (north.canMove(SOUTH)) {
					dist = distances.get(north);
					compare = distances.get(consider) + consider.cost();
					if (dist == null) {
						//add to "open list"
						distances.put(north, compare);
						previous.put(north, consider);
					}
					else if (dist > compare) {
						distances.put(north, compare);
						previous.put(north, consider);
					}
				}
			} catch(ArrayIndexOutOfBoundsException e) { }
			
			try {
				east = getEast(consider);
				if (east.canMove(WEST)) {
					distances.put(east, distances.get(consider) + east.cost());
				}
			} catch(ArrayIndexOutOfBoundsException e) { }
			
			try {
				south = getSouth(consider);
				if (south.canMove(NORTH)) {
					
				}
			} catch(ArrayIndexOutOfBoundsException e) { }
			
			try {
				west = getWest(consider);
				if (west.canMove(EAST)) {
					
				}
			} catch(ArrayIndexOutOfBoundsException e) { }
		}
		
		return null;
	}
	
	/**
	 * lists all tiles that can be reached from the source tile with the given range
	 * @param source the tile to start from
	 * @param range the total cost of movement allowable
	 * @return an unordered list of all tiles that can be reached from the source 
	 */
	public List<Tile> getMovementRange(Tile source, int range) {
		return null;
	}
	
	/**
	 * lists all tiles that could be attacked from the source tile with the given movement and attack ranges
	 * @param source
	 * @param movementRange
	 * @param attackRange
	 * @return an unordered list of tiles that could be attacked from the source tile
	 */
	public List<Tile> getAttackRange(Tile source, int movementRange, int attackRange) {
		return getMovementRange(source, movementRange);
	}
	
	/** 
	 * @param brush 
	 * Paints all of the tiles in the map with their respective images
	 */
	
	public void paint(java.awt.Graphics2D brush) {
		for(int i = 0; i < _map.length; i++) {
			for(int j = 0; j < _map[i].length; j++) {
				_map[i][j].paint(brush,_map[i][j].getPath());
			}
		}
	}
}
