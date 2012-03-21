package map;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.LinkedList;
import java.util.PriorityQueue;

import javax.swing.JPanel;

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
	
	public Map(Tile[][] tiles, String name) {
		_map = tiles;
		_overflow = null;
		_name = "";
	}
	
	/**
	 * parses a map to generate a map instance
	 * @param permissionFile the file to parse tile permissions from
	 * @return a new map given by the data from the map files
	 */
	public static Map map(JPanel container, String permissionFile)
			throws InvalidMapException {
		try {
		BufferedReader reader = new BufferedReader(new FileReader(new File(permissionFile)));
		String name = reader.readLine();
		String[] dimensions = reader.readLine().split("/");
		Tile[][] _tiles = new Tile[Integer.parseInt(dimensions[0])][Integer.parseInt(dimensions[1])];
		
		
		
		} catch(IOException e) {
			throw new InvalidMapException();
		} catch(NumberFormatException e) {
			throw new InvalidMapException();
		}
		
		return null;
	}
	
	public Tile getTile(int x, int y) {
		return _map[x][y];
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
		final Hashtable<Tile, Tile> previous = new Hashtable<Tile, Tile>();
		final Hashtable<Tile, Integer> heapPositions = new Hashtable<Tile, Integer>();
		
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
		
		Tile consider = source, check = null;
		Integer dist, compare;
		while (distances.get(dest) == null && !heap.isEmpty()) {

			try {
				check = getNorth(consider);
				if (check.canMove(SOUTH) && !check.isOccupied()) {
					dist = distances.get(check);
					compare = distances.get(consider) + consider.cost();
					
					if (dist == null) {	//tile hasn't been seen yet
						distances.put(check, compare);
						heapPositions.put(check, heap.insert(check));
						previous.put(check, consider);
					}
					else if (dist > compare) {	//more optimal way to get to the tile
						distances.put(check, compare);
						heap.siftUp(heapPositions.get(check));
						previous.put(check, consider);
					}
				}
			} catch(ArrayIndexOutOfBoundsException e) { }
			
			try {
				check = getEast(consider);
				if (check.canMove(WEST) && !check.isOccupied()) {
					dist = distances.get(check);
					compare = distances.get(consider) + consider.cost();
					
					if (dist == null) {	//tile hasn't been seen yet
						distances.put(check, compare);
						heapPositions.put(check, heap.insert(check));
						previous.put(check, consider);
					}
					else if (dist > compare) {	//more optimal way to get to the tile
						distances.put(check, compare);
						heap.siftUp(heapPositions.get(check));
						previous.put(check, consider);
					}
				}
			} catch(ArrayIndexOutOfBoundsException e) { }
			
			try {
				check = getSouth(consider);
				if (check.canMove(NORTH) && !check.isOccupied()) {
					dist = distances.get(check);
					compare = distances.get(consider) + consider.cost();
					
					if (dist == null) {	//tile hasn't been seen yet
						distances.put(check, compare);
						heapPositions.put(check, heap.insert(check));
						previous.put(check, consider);
					}
					else if (dist > compare) {	//more optimal way to get to the tile
						distances.put(check, compare);
						heap.siftUp(heapPositions.get(check));
						previous.put(check, consider);
					}
				}
			} catch(ArrayIndexOutOfBoundsException e) { }
			
			try {
				check = getWest(consider);
				if (check.canMove(EAST) && !check.isOccupied()) {
					dist = distances.get(check);
					compare = distances.get(consider) + consider.cost();
					
					if (dist == null) {	//tile hasn't been seen yet
						distances.put(check, compare);
						heapPositions.put(check, heap.insert(check));
						previous.put(check, consider);
					}
					else if (dist > compare) {	//more optimal way to get to the tile
						distances.put(check, compare);
						heap.siftUp(heapPositions.get(check));
						previous.put(check, consider);
					}
				}
			} catch(ArrayIndexOutOfBoundsException e) { }
		}
		
		if (distances.get(dest) == null)
			return null;
		
		LinkedList<Tile> path = new LinkedList<Tile>();
		Tile previousTile = dest;
		while (previousTile != source) {
			path.addLast(previousTile);
			previousTile = previous.get(previousTile);
		}
		
		return path;
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
