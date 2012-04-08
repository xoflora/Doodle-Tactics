package map;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.LinkedList;

import javax.swing.JPanel;

import util.Heap;

/**
 * 
 * @author rroelke
 * a Map is a segment of the game world during which gameplay occurs
 * implements Serializable, for saving purposes
 */
public class Map implements Serializable{
	
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
	
	public String toString() {
		String build = "\t";
		for (int i = 0; i < _map.length; i++)
			build += "  " + i + (i < 10 ? "   ":"  ");
		build += "\n";
		for (int i = 0; i < _map.length; i++) {
			if (i != 0) {
				build += "\t";
				for (int j = 0; j < _map[i].length; j++)
					build += "  " + (_map[i][j].canMove(NORTH) ? "v":"x") + "   ";
				build += "\n";
			}
			build += i + "\t";
			for (int j = 0; j < _map[i].length; j++)
				build += (_map[i][j].canMove(EAST) ? "->" : "xx") + _map[i][j].cost() + (_map[i][j].canMove(WEST) ? "<-" : "xx") + " ";
			build += "\n";
			if (i != _map.length - 1) {
				build += "\t";
				for (int j = 0; j < _map[i].length; j++)
					build += "  " + (_map[i][j].canMove(SOUTH) ? "^":"x") + "   ";
				build += "\n";
			}
		}
		
		return build;
	}
	
	public Tile getTile(int x, int y) {
		try {
			return _map[x][y];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
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
		return _map[source.x() - 1][source.y()];
	}
	
	/**
	 * returns an estimate for the distance between two tiles
	 * heuristic function used for A* pathfinding
	 * @param source the start tile
	 * @param dest the end tile
	 * @return an estimate of the distance between the two tiles
	 */
	public int estimateDistance(Tile source, Tile dest) {
		return Math.abs(source.x() - dest.x()) + Math.abs(source.y() - dest.y());
	}
	
	/**
	 * given a tile, searches the adjacent tiles, considering their distance and previous tiles
	 * @param search the source tile of the search
	 * @param heap the heap to add new tiles to
	 * @param distances the distance of each tile from the start source
	 * @param heapPositions a table mapping tiles to their positions in the heap
	 * @param previous a table mapping tiles to the tile encountered before them in the search
	 */
	private void searchTile(Tile search, Heap<Tile> heap, Hashtable<Tile, Integer> distances,
				Hashtable<Tile, Integer> heapPositions, Hashtable<Tile, Tile> previous,
				boolean useCost, boolean usePermissions) {
		Tile check;
		Integer dist, compare;
		try {
			check = getNorth(search);
			if (check.canMove(SOUTH) || !usePermissions) {
				dist = distances.get(check);
				compare = distances.get(search) + (useCost ? check.cost():1);
				
				if (dist == null) {	//tile hasn't been seen yet
					distances.put(check, compare);
					heapPositions.put(check, heap.insert(check));
					previous.put(check, search);
				}
				else if (dist > compare) {	//more optimal way to get to the tile
					distances.put(check, compare);
					heap.siftUp(heapPositions.get(check));
					previous.put(check, search);
				}
			}
		} catch(ArrayIndexOutOfBoundsException e) { }
		
		try {
			check = getEast(search);
			if (check.canMove(WEST) || !usePermissions) {
				dist = distances.get(check);
				compare = distances.get(search) + (useCost ? check.cost():1);
				
				if (dist == null) {	//tile hasn't been seen yet
					distances.put(check, compare);
					heapPositions.put(check, heap.insert(check));
					previous.put(check, search);
				}
				else if (dist > compare) {	//more optimal way to get to the tile
					distances.put(check, compare);
					heap.siftUp(heapPositions.get(check));
					previous.put(check, search);
				}
			}
		} catch(ArrayIndexOutOfBoundsException e) { }
		
		try {
			check = getSouth(search);
			if (check.canMove(NORTH) || !usePermissions) {
				dist = distances.get(check);
				compare = distances.get(search) + (useCost ? check.cost():1);
				
				if (dist == null) {	//tile hasn't been seen yet
					distances.put(check, compare);
					heapPositions.put(check, heap.insert(check));
					previous.put(check, search);
				}
				else if (dist > compare) {	//more optimal way to get to the tile
					distances.put(check, compare);
					heap.siftUp(heapPositions.get(check));
					previous.put(check, search);
				}
			}
		} catch(ArrayIndexOutOfBoundsException e) { }
		
		try {
			check = getWest(search);
			if (check.canMove(EAST) || !usePermissions) {
				dist = distances.get(check);
				compare = distances.get(search) + (useCost ? check.cost():1);
				
				if (dist == null) {	//tile hasn't been seen yet
					distances.put(check, compare);
					heapPositions.put(check, heap.insert(check));
					previous.put(check, search);
				}
				else if (dist > compare) {	//more optimal way to get to the tile
					distances.put(check, compare);
					heap.siftUp(heapPositions.get(check));
					previous.put(check, search);
				}
			}
		} catch(ArrayIndexOutOfBoundsException e) { }
	}
	
	private List<Tile> getTilesWithinRange(Tile source, int minRange, int maxRange, boolean useCost, boolean usePermissions) {
		final Hashtable<Tile, Integer> distances = new Hashtable<Tile, Integer>();
		final Hashtable<Tile, Tile> previous = new Hashtable<Tile, Tile>();
		final Hashtable<Tile, Integer> heapPositions = new Hashtable<Tile, Integer>();

		Heap<Tile> heap = new Heap<Tile>(minRange*minRange,
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
		heapPositions.put(source, heap.insert(source));

		LinkedList<Tile> movementRange = new LinkedList<Tile>();
		Tile consider;
		
		while (!heap.isEmpty()) {
			consider = heap.extractMin();
			heapPositions.put(consider, -1);
			
			int dist = distances.get(consider);
			if (dist <= maxRange) {
				if (minRange <= dist)
					movementRange.add(consider);
				searchTile(consider, heap, distances, heapPositions, previous, useCost, usePermissions);
			}
		}

		return movementRange;
	}
	
	/**
	 * generates a path from the source tile to the destination tile
	 * @param source the tile to start from
	 * @param dest the tile to end at
	 * @return an ordered list of all tiles in the path
	 */
	public List<Tile> getPath(final Tile source, final Tile dest) {
		if (source == null || dest == null)
			return null;
		
		final Hashtable<Tile, Integer> distances = new Hashtable<Tile, Integer>();
		final Hashtable<Tile, Tile> previous = new Hashtable<Tile, Tile>();
		final Hashtable<Tile, Integer> heapPositions = new Hashtable<Tile, Integer>();
		
		Heap<Tile> heap = new Heap<Tile>(estimateDistance(source, dest),
				new Comparator<Tile>() {
					@Override
					public int compare(Tile o1, Tile o2) {
						int d1 = distances.get(o1) + estimateDistance(o1, dest);
						int d2 = distances.get(o2) + estimateDistance(o2, dest);
						
						if (d1 < d2)
							return -1;
						else if (d1 == d2)
							return 0;
						else
							return 1;
					}
		});
		
		distances.put(source, 0);
		heapPositions.put(source, heap.insert(source));
		
		Tile consider;
		while (distances.get(dest) == null && !heap.isEmpty()) {
			consider = heap.extractMin();
			heapPositions.put(consider, -1);
			searchTile(consider, heap, distances, heapPositions, previous, true, true);
		}
		
		if (distances.get(dest) == null)
			return null;
		
		LinkedList<Tile> path = new LinkedList<Tile>();
		Tile previousTile = dest;
		while (previousTile != source) {
			path.addFirst(previousTile);
			previousTile = previous.get(previousTile);
		}
		path.addFirst(previousTile);
		
		return path;
	}
	
	/**
	 * lists all tiles that can be reached from the source tile with the given range
	 * @param source the tile to start from
	 * @param range the total cost of movement allowable
	 * @return an unordered list of all tiles that can be reached from the source 
	 */
	public List<Tile> getMovementRange(Tile source, int range) {
		if (source == null)
			return null;

		return getTilesWithinRange(source, 0, range, true, true);
	}

	/**
	 * lists all tiles that could be attacked from the source tile with the given movement and attack ranges
	 * @param source
	 * @param moveRange
	 * @param attackRange
	 * @return an unordered list of tiles that could be attacked from the source tile
	 */
	public List<Tile> getAttackRange(Tile source, int moveRange, int minAttackRange, int maxAttackRange) {
		List<Tile> movementRange = getMovementRange(source, moveRange);
		List<Tile> range = new LinkedList<Tile>();
		
		Hashtable<Tile, Boolean> included = new Hashtable<Tile, Boolean>();

		for (Tile attackFrom : movementRange) {			
			for (Tile add : getTilesWithinRange(attackFrom, minAttackRange, maxAttackRange, false, false)) {
				if (included.get(add) == null || !included.get(add)) {
					included.put(add, true);
					range.add(add);
				}
			}
		}
	
		return range;
	}
	
	/** 
	 * @param brush 
	 * Paints all of the tiles in the map with their respective images
	 */
	
	public void paint(java.awt.Graphics2D brush) {
		for(int i = 0; i < _map.length; i++) {
			for(int j = 0; j < _map[i].length; j++) {
				_map[i][j].paint(brush,_map[i][j].getImage());
			}
		}
	}
	
	/**
	 * flattens a Map to a file (using serialization), for saving purposes
	 * @author czchapma
	 * @param filepath - the location of the file to write to
	 */
	public void serialize(String filepath){
		FileOutputStream fos = null;
		ObjectOutputStream out  = null;
		try{
			fos = new FileOutputStream(filepath);
			out = new ObjectOutputStream(fos);
			out.writeObject(this);
			out.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * unflattens a character, opposite of serialize()
	 * @author czchapma
	 * @param filepath -- location of serialized file
	 * @return the unflattened Character retrieved from filepath
	 */
	public static Map restore(String filepath){
		Map m = null;
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try{
			fis = new FileInputStream(filepath);
			in = new ObjectInputStream(fis);
			m = (Map)in.readObject();
			in.close();
		} catch(IOException e){
			e.printStackTrace();
		} catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		return m;
	}

}
