package map;

import event.CombatEvent;
import event.Dialogue;
import event.InvalidEventException;
import event.Warp;
import graphics.Rectangle;
import graphics.Terrain;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import main.DoodleTactics;
import main.GameScreen;
import character.*;
import character.Character;
import character.Character.CharacterDirection;
import util.Heap;

/**
 * 
 * @author rroelke (except where noted)
 * a Map is a segment of the game world
 *         during which gameplay occurs
 * implements Serializable, for saving
 *         purposes
 */
public class Map implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;

	//Used by Tiles
	public static final int NO_EVENT = 0;
	public static final int DIALOGUE = 1;
	public static final int WARP = 2;
	public static final int COMBAT = 3;
	// ...

	private static final int DEFAULT_XREF = 5;
	private static final int DEFAULT_YREF = 5;
	
	private static final int DEFAULT_X_WINDOW_OFFSET = 0;
	private static final int DEFAULT_Y_WINDOW_OFFSET = 0;

	//Random Battle
	private static final int CUTOFF = 100;

	private transient DoodleTactics _dt;
	private transient BufferedImage _overflow;
	private Tile[][] _map;
	private LinkedList<Terrain> _terrain;
	private LinkedList<Character> _activeCharacters;
	private MainCharacter _mainChar;
	private Stack<Integer> _prevXRef;
	private Stack<Integer> _prevYRef;
	
	private Stack<Integer> _prevXWindowOffset;
	private Stack<Integer> _prevYWindowOffset;
	
	/**
	 * fields used to generate random battles;
	 *  - _enemyTiles maps characters to the tile on which they reside
	 *  - _enemyToTiles maps characters to the list of tiles within their movement range
	 *  - _tileToEnemies maps a tile to the enemies whose movement range contains that tile
	 *  - _randBattles is a list of possible sites of random enemies
	 */
	private HashMap<Character, Tile> _enemyTiles;
	private HashMap<Character, List<Tile>> _enemyToTiles;
	private HashMap<Tile,List<Character>> _tileToEnemies;
	private LinkedList<Tile> _randBattles;
	
	private String _name;
	
	private Point _mapCords;

	public Map(DoodleTactics dt, Tile[][] tiles, String name, BufferedImage overflow,
			LinkedList<Terrain> terrain,
			LinkedList<Character> chars, LinkedList<Tile> randBattles, MainCharacter main,Point mapCords) {
		_map = tiles;
		_overflow = overflow;
		_name = "";
		_terrain = terrain;
		_activeCharacters = chars;
		_mainChar = main;
		_dt = dt;
		_randBattles = randBattles;
		_mapCords = mapCords;

		//Random Battle data structures:
		_enemyTiles = new HashMap<Character, Tile>();
		_enemyToTiles = new HashMap<Character,List<Tile>>();
		_tileToEnemies = new HashMap<Tile,List<Character>>();

		//Handle restoring XRef and YRef
		_prevXRef = new Stack<Integer>();
		_prevYRef = new Stack<Integer>();
		_prevXRef.push(DEFAULT_XREF);
		_prevYRef.push(DEFAULT_YREF);
		
		_prevXWindowOffset = new Stack<Integer>();
		_prevYWindowOffset = new Stack<Integer>();
		_prevXWindowOffset.push(DEFAULT_X_WINDOW_OFFSET);
		_prevYWindowOffset.push(DEFAULT_Y_WINDOW_OFFSET);
		
		System.out.println("Tile 14, 14: X-" +_map[0][0].getX() + " Y- " + _map[14][14].getY());

	}

	/**
	 * parses a map to generate a map instance
	 * 
	 * @param permissionFile
	 *            the file to parse tile permissions from
	 * @return a new map given by the data from the map files
	 * 
	 *         Expected file format: name,default_path,overflow_path
	 *         numCols,numRows
	 *         x1,y1,permissions,img_path,cost,rand_battle,has_event,event_file
	 *         img,img_path, x1, y1,tile_permissions ...
	 *         char,type,name,profile_img,left_img,right_img,up_img,down_img, x, y .. 
	 *         (where x and y are 0-indexed)
	 * 	NOTE: ALL Tiles must be defined before characters
	 * @author czchapma
	 */
	
	public static Map map(DoodleTactics dt, GameScreen container, String path)
			throws InvalidMapException {
		int count = 4;
		LinkedList<Terrain> terrainList = new LinkedList<Terrain>();
		LinkedList<Character> chars = new LinkedList<Character>();
		LinkedList<Tile> randBattles = new LinkedList<Tile>();
		MainCharacter main = dt.getGameScreen().getMainChar();
		
		try {
			// Parse initial data
			BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
			String[] splitLine = reader.readLine().split(",");
			
			if (splitLine.length!=3) {
				throw new InvalidMapException("(line 1) Incorrect amount of data");
			}
			
			if (splitLine[0].equals("mapCords")) {
				throw new InvalidMapException("(line 2) Incorrect map file format");
			}
			
			Point mapCords = new Point(Integer.parseInt(splitLine[1]), Integer.parseInt(splitLine[2]));

			splitLine = reader.readLine().split(",");
			
			if (splitLine.length != 3)
				throw new InvalidMapException("(line 3) Incorrect amount of data");

			String name = splitLine[0];
			String defaultPath = splitLine[1];
			String overflowPath = splitLine[2];

			// parse the dimensions of the map
			String lineRead = reader.readLine();
			splitLine = lineRead.split(",");
			if (splitLine.length != 2)
				throw new InvalidMapException("(line 3) Incorrect amount of data");
			int numX = Integer.parseInt(splitLine[0]);
			int numY = Integer.parseInt(splitLine[1]);

			Tile[][] tiles = new Tile[numY][numX];
			// Set all currently null tiles to default
			for (int x = 0; x < numX; x++) {
				for (int y = 0; y < numY; y++) {
					if (tiles[x][y] == null)
						tiles[x][y] = Tile.tile(dt,container, defaultPath, 'F', x,
								y, 1, 0, 0, 0);
				}
			}

			HashMap<Tile, String> combatEvents = new HashMap<Tile, String>();
			// Read and parse tiles from file
			String line = reader.readLine();
			int x, y;
			while (line != null) {
				splitLine = line.split(",");
				count++;

				// Terrain (Image)  case
				if (splitLine.length == 5 && splitLine[0].equals("img")) {
					if(main == null)
						throw new InvalidMapException("(line " + count + ") Main Character must be parsed before images");
					int xTile = Integer.parseInt(splitLine[2]);
					int yTile = Integer.parseInt(splitLine[3]);
					double xLoc = Tile.TILE_SIZE * xTile; //(xTile  - main.getTileX());
					double yLoc = Tile.TILE_SIZE * yTile; //(yTile - main.getTileY());
					tiles[xTile][yTile].setTilePermissions((splitLine[4]).charAt(0));
					Terrain t = new Terrain(dt,container, splitLine[1], xLoc, yLoc);
					terrainList.add(t);

					// Character Case
				} else if (splitLine.length == 10 && splitLine[0].equals("char")) {

					if(main == null)
						throw new InvalidMapException("(line " + count + ") Main Character must be parsed before other Characters");

					Character toAdd = null;
					Tile target = tiles[Integer.parseInt(splitLine[8])][Integer.parseInt(splitLine[9])]; 

					double xLoc = Tile.TILE_SIZE * Integer.parseInt(splitLine[8]);//(Integer.parseInt(splitLine[8])  - main.getTileX());
					double yLoc = Tile.TILE_SIZE * Integer.parseInt(splitLine[9]);//(Integer.parseInt(splitLine[9]) - main.getTileY());

					// check which type of character toAdd is
					if (splitLine[1].equals("Archer")) { 
						toAdd = new Archer(dt,container, splitLine[3],
								splitLine[4], splitLine[5], splitLine[6],
								splitLine[7], splitLine[2], xLoc,yLoc);

					} else if (splitLine[1].equals("Mage")) {
						toAdd = new Mage(dt,container, splitLine[3], splitLine[4],
								splitLine[5], splitLine[6], splitLine[7],
								splitLine[2], xLoc,yLoc);

					} else if (splitLine[1].equals("Thief")) {
						toAdd = new Thief(dt,container, splitLine[3],
								splitLine[4], splitLine[5], splitLine[6],
								splitLine[7], splitLine[2], xLoc,yLoc);

					} else if (splitLine[1].equals("Warrior")) {
						toAdd = new Warrior(dt,container, splitLine[3],
								splitLine[4], splitLine[5], splitLine[6],
								splitLine[7],  splitLine[2], xLoc,yLoc);
					} else
						throw new InvalidMapException("Invalid Character Type");

					// set the tile's occupant to the character we just parsed
					if (toAdd != null) { 
						chars.add(toAdd);
						target.setOccupant(toAdd);
						dt.addCharacterToMap(toAdd, splitLine[2]);
					}

					// Main character case
				}/* else if(splitLine.length == 8 && splitLine[1].equals("Main")){
					main = new MainCharacter(container, splitLine[3],
							splitLine[4], splitLine[5], splitLine[6],
							splitLine[7],splitLine[2],5,5);
					main.setVisible(true);
					main.setFillColor(java.awt.Color.BLACK);
					main.setSize(main.getImage().getWidth(), main
							.getImage().getHeight());
					int overflow = (main.getImage().getWidth() - Tile.TILE_SIZE) / 2;
					main.setLocation(10*Tile.TILE_SIZE-overflow, 8*Tile.TILE_SIZE - main.getImage().getHeight() + Tile.TILE_SIZE);
					dt.addCharacterToMap(main, splitLine[2]);

				}	*/


				// Tile case
				// 7 if no event specified, 8 otherwise
				else if (((splitLine.length == 10) && Integer.parseInt(splitLine[6]) == NO_EVENT) ||
						(splitLine.length == 11 && Integer.parseInt(splitLine[6]) == DIALOGUE) ||
						(splitLine.length == 11 && Integer.parseInt(splitLine[6]) == COMBAT)
								|| (splitLine.length == 13 && Integer.parseInt(splitLine[6]) == WARP)) {
					x = Integer.parseInt(splitLine[0]);
					y = Integer.parseInt(splitLine[1]);

					tiles[x][y] = Tile.tile(dt,container, splitLine[3],
							splitLine[2].charAt(0), x, y, Integer
							.parseInt(splitLine[4]), Integer.parseInt(splitLine[7]), Integer.parseInt(splitLine[8]),
							Integer.parseInt(splitLine[9]));

					//Handle Events
					if(Integer.parseInt(splitLine[6]) == DIALOGUE){
						tiles[x][y].setEvent(new Dialogue(dt,splitLine[10]));
						tiles[x][y].setInteractible();
					} else if(Integer.parseInt(splitLine[6]) == WARP){
						tiles[x][y].setEvent(new Warp(dt,tiles[x][y],splitLine[10],
								Integer.parseInt(splitLine[11]), Integer.parseInt(splitLine[12])));
						tiles[x][y].setEnterEvent();
					} else if((Integer.parseInt(splitLine[6])) == COMBAT){
						combatEvents.put(tiles[x][y], splitLine[10]);
						tiles[x][y].setEvent(CombatEvent.parseEvent(dt, splitLine[10]));
						tiles[x][y].setEnterEvent();
					}
					//add others in the future perhaps

					//Check if tile can generate random battles
					if(splitLine[5].equals("1"))
						randBattles.add(tiles[x][y]);

					// Error Case
				} else
					throw new InvalidMapException("(line " + count
							+ ") Incorrect amount of data");
				
				line = reader.readLine();
			}
		/*	for (Tile t : combatEvents.keySet()) {
				t.setEvent(CombatEvent.parseCombat(dt, combatEvents.get(t)));
				t.setEnterEvent();
			}	*/

			// Create Map
			if (main == null)
				throw new InvalidMapException("Main Character not specified");
			Map m = new Map(dt,tiles, name, dt.importImage(overflowPath),
					terrainList, chars, randBattles,main,mapCords); 
			
			return m;
		} catch (FileNotFoundException e) {
			throw new InvalidMapException(
					"Error reading map from file located at " + path + ".");
		} catch (IOException e) {
			throw new InvalidMapException(
					"(line " + count + ") Something went wrong while reading the file.");
		} catch (NumberFormatException e) {
			String msg = "Expected int";
			if (count > 2)
				msg = "(line " + count + ") " + msg;
			else
				msg = "(line 2) " + msg;
			throw new InvalidMapException(msg);

		} catch (InvalidTileException e) {
			String msg = "Invalid tile ";
			if (count > 2)
				msg = "(line " + count + ") " + msg;
			throw new InvalidMapException(msg);

		} catch (ArrayIndexOutOfBoundsException e) {
			String msg = "Not within given tile range";
			if (count > 2)
				msg = "(line " + count + ") " + msg;
			throw new InvalidMapException(msg);
		}catch (InvalidEventException e) {
			System.out.println(e.getMessage());
			throw new InvalidMapException("(line " + count + ") Invalid Event Specified");
		}

	}
	/**
	 * Method used to reset all transient data structures after deserialization
	 * @param dt
	 * @throws IOException 
	 * @throws InvalidEventException 
	 * @throws FileNotFoundException 
	 */
	public void load(DoodleTactics dt) throws FileNotFoundException, InvalidEventException, IOException{
		//TODO handle overflow tile, if necessary
		_dt = dt;
		for(Character c : _activeCharacters){
			c.load(dt);
		}
		
		System.out.println("Main Char X: " + _mainChar.getX() + " Y:" + _mainChar.getY()); 
	//	System.out.println("Other CHAR X: " + _activeCharacters.get(1).getX() +
	//			" Y: " + _activeCharacters.get(1).getY());
		
		
		for(int i=0; i<_map.length; i++){
			for(int j=0; j<_map[0].length; j++){
						_map[i][j].load(dt);
			}
		}
		

		
		for(Terrain t: _terrain)
			t.load(_dt);
	}

	public String toString() {
		String build = "\t";
		for (int i = 0; i < _map.length; i++)
			build += "  " + i + (i < 10 ? "   " : "  ");
		build += "\n";
		for (int i = 0; i < _map.length; i++) {
			if (i != 0) {
				build += "\t";
				for (int j = 0; j < _map[i].length; j++)
					build += "  " + (_map[i][j].canMove(NORTH) ? "v" : "x")
					+ "   ";
				build += "\n";
			}
			build += i + "\t";
			for (int j = 0; j < _map[i].length; j++)
				build += (_map[i][j].canMove(EAST) ? "->" : "xx")
				+ _map[i][j].cost()
				+ (_map[i][j].canMove(WEST) ? "<-" : "xx") + " ";
			build += "\n";
			if (i != _map.length - 1) {
				build += "\t";
				for (int j = 0; j < _map[i].length; j++)
					build += "  " + (_map[i][j].canMove(SOUTH) ? "^" : "x")
					+ "   ";
				build += "\n";
			}
		}

		return build;
	}
	
	public Point getMapCords() {
		return _mapCords;
	}

	public Tile getTile(int x, int y) {
		try {
			return _map[x][y];
		} catch (ArrayIndexOutOfBoundsException e) {
		//	System.out.println("ARRAY OUT OF BOUNDS NOO");
			return null;
		}
	}

	/**
	 * @return the number of columns of the map
	 */
	public int getWidth() {
		return _map.length;
	}

	/**
	 * @return the number of rows of the map
	 */
	public int getHeight() {
		return _map[0].length;
	}

	/**
	 * @param source
	 * @return the tile to the north of the source tile
	 */
	public Tile getNorth(Tile source) {
		try{
			return _map[source.x()][source.y() - 1];
		} catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}

	/**
	 * @param source
	 * @return the tile to the east of the source tile
	 */
	public Tile getEast(Tile source) {
		try{
			return _map[source.x() + 1][source.y()];
		} catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}

	/**
	 * @param source
	 * @return the tile to the south of the source tile
	 */
	public Tile getSouth(Tile source) {
		try{
			return _map[source.x()][source.y() + 1];
		} catch(ArrayIndexOutOfBoundsException e){
			return null;			
		}
	}

	/**
	 * @param source
	 * @return the tile to the west of the source tile
	 */
	public Tile getWest(Tile source) {
		try{
			return _map[source.x() - 1][source.y()];
		} catch(ArrayIndexOutOfBoundsException e){
			return null;
		}
	}

	/**
	 * returns an estimate for the distance between two tiles heuristic function
	 * used for A* pathfinding
	 * 
	 * @param source
	 *            the start tile
	 * @param dest
	 *            the end tile
	 * @return an estimate of the distance between the two tiles
	 */
	public int estimateDistance(Tile source, Tile dest) {
		return Math.abs(source.x() - dest.x())
		+ Math.abs(source.y() - dest.y());
	}

	/**
	 * given a tile, searches the adjacent tiles, considering their distance and
	 * previous tiles
	 * 
	 * @param search
	 *            the source tile of the search
	 * @param heap
	 *            the heap to add new tiles to
	 * @param distances
	 *            the distance of each tile from the start source
	 * @param heapPositions
	 *            a table mapping tiles to their positions in the heap
	 * @param previous
	 *            a table mapping tiles to the tile encountered before them in
	 *            the search
	 */
	private void searchTile(Tile search, Heap<Tile> heap,
			Hashtable<Tile, Integer> distances,
			Hashtable<Tile, Integer> heapPositions,
			Hashtable<Tile, Tile> previous, boolean useCost,
			boolean usePermissions) {
		Tile check;
		Integer dist, compare;

		try {
			check = getNorth(search);
			if (check != null && (check.canMove(SOUTH) || !usePermissions)) {
				dist = distances.get(check);
				compare = distances.get(search) + (useCost ? check.cost() : 1);

				if (dist == null) { // tile hasn't been seen yet
					distances.put(check, compare);
					heapPositions.put(check, heap.insert(check));
					previous.put(check, search);
				} else if (dist > compare) { // more optimal way to get to the
					// tile
					distances.put(check, compare);
					heap.siftUp(heapPositions.get(check));
					previous.put(check, search);
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
		}

		try {
			check = getEast(search);
			if (check != null && (check.canMove(WEST) || !usePermissions)) {
				dist = distances.get(check);
				compare = distances.get(search) + (useCost ? check.cost() : 1);

				if (dist == null) { // tile hasn't been seen yet
					distances.put(check, compare);
					heapPositions.put(check, heap.insert(check));
					previous.put(check, search);
				} else if (dist > compare) { // more optimal way to get to the
					// tile
					distances.put(check, compare);
					heap.siftUp(heapPositions.get(check));
					previous.put(check, search);
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
		}

		try {
			check = getSouth(search);
			if (check != null && (check.canMove(NORTH) || !usePermissions)) {
				dist = distances.get(check);
				compare = distances.get(search) + (useCost ? check.cost() : 1);

				if (dist == null) { // tile hasn't been seen yet
					distances.put(check, compare);
					heapPositions.put(check, heap.insert(check));
					previous.put(check, search);
				} else if (dist > compare) { // more optimal way to get to the
					// tile
					distances.put(check, compare);
					heap.siftUp(heapPositions.get(check));
					previous.put(check, search);
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
		}

		try {
			check = getWest(search);
			if (check != null && (check.canMove(EAST) || !usePermissions)) {
				dist = distances.get(check);
				compare = distances.get(search) + (useCost ? check.cost() : 1);

				if (dist == null) { // tile hasn't been seen yet
					distances.put(check, compare);
					heapPositions.put(check, heap.insert(check));
					previous.put(check, search);
				} else if (dist > compare) { // more optimal way to get to the
					// tile
					distances.put(check, compare);
					heap.siftUp(heapPositions.get(check));
					previous.put(check, search);
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
		}
	}

	private List<Tile> getTilesWithinRange(Tile source, int minRange,
			int maxRange, boolean useCost, boolean usePermissions) {
		final Hashtable<Tile, Integer> distances = new Hashtable<Tile, Integer>();
		final Hashtable<Tile, Tile> previous = new Hashtable<Tile, Tile>();
		final Hashtable<Tile, Integer> heapPositions = new Hashtable<Tile, Integer>();

		Heap<Tile> heap = new Heap<Tile>(minRange * minRange,
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
				searchTile(consider, heap, distances, heapPositions, previous,
						useCost, usePermissions);
			}
		}

		return movementRange;
	}

	/**
	 * generates a path from the source tile to the destination tile
	 * 
	 * @param source
	 *            the tile to start from
	 * @param dest
	 *            the tile to end at
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
			searchTile(consider, heap, distances, heapPositions, previous,
					true, true);
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
	 * lists all tiles that can be reached from the source tile with the given
	 * range
	 * 
	 * @param source
	 *            the tile to start from
	 * @param range
	 *            the total cost of movement allowable
	 * @return an unordered list of all tiles that can be reached from the
	 *         source
	 */
	public List<Tile> getMovementRange(Tile source, int range) {
		if (source == null)
			return null;

		return getTilesWithinRange(source, 0, range, true, true);
	}

	/**
	 * lists all tiles that could be attacked from the source tile with the
	 * given movement and attack ranges
	 * 
	 * @param source
	 * @param moveRange
	 * @param attackRange
	 * @return an unordered list of tiles that could be attacked from the source
	 *         tile
	 */
	public List<Tile> getAttackRange(Tile source, int moveRange,
			int minAttackRange, int maxAttackRange) {
		List<Tile> movementRange = getMovementRange(source, moveRange);
		List<Tile> range = new LinkedList<Tile>();

		Hashtable<Tile, Boolean> included = new Hashtable<Tile, Boolean>();

		for (Tile attackFrom : movementRange) {
			for (Tile add : getTilesWithinRange(attackFrom, minAttackRange,
					maxAttackRange, false, false)) {
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
	 *            Paints all of the tiles in the map with their respective
	 *            images
	 */

	public void paint(java.awt.Graphics2D brush) {
		for (int i = 0; i < _map.length; i++) {
			for (int j = 0; j < _map[i].length; j++) {
				_map[i][j].paint(brush, _map[i][j].getImage());
			}
		}
	}

	/**
	 * flattens a Map to a file (using serialization), for saving purposes
	 * 
	 * @author czchapma
	 * @param filepath
	 *            - the location of the file to write to
	 */
	public void serialize(String filepath) {
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(filepath);
			out = new ObjectOutputStream(fos);
			out.writeObject(this);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * unflattens a character, opposite of serialize()
	 * 
	 * @author czchapma
	 * @param filepath
	 *            -- location of serialized file
	 * @return the unflattened Character retrieved from filepath
	 */
	public static Map restore(String filepath) {
		Map m = null;
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream(filepath);
			in = new ObjectInputStream(fis);
			m = (Map) in.readObject();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return m;
	}

	/**
	 * @param source
	 *            the tile to search from
	 * @param num
	 * @return a list of valid tiles for unit placement on this map (which
	 *         happen to be the closest num to the given tile)
	 */
	public List<Tile> getValidSetupTiles(Tile source, int num) {
		final Hashtable<Tile, Integer> distances = new Hashtable<Tile, Integer>();
		final Hashtable<Tile, Tile> previous = new Hashtable<Tile, Tile>();
		final Hashtable<Tile, Integer> heapPositions = new Hashtable<Tile, Integer>();

		Heap<Tile> heap = new Heap<Tile>(num, new Comparator<Tile>() {
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

		System.out.println(source == null);

		distances.put(source, 0);
		heapPositions.put(source, heap.insert(source));

		LinkedList<Tile> validTiles = new LinkedList<Tile>();
		Tile consider;

		while (!heap.isEmpty()) {
			consider = heap.extractMin();
			heapPositions.put(consider, -1);

			if (validTiles.size() < num) {
				if (consider != source)
					validTiles.add(consider);
				searchTile(consider, heap, distances, heapPositions, previous,
						true, true);
			}
		}

		return validTiles;
	}

	/**
	 * @return The LinkedList storing the Terrain info
	 */
	public LinkedList<Terrain> getTerrain() {
		return _terrain;
	}

	/**
	 * @return The LinkedList of active Characters This list includes the main
	 *         character
	 */
	public LinkedList<Character> getCharactersToDisplay() {
		for (Character c : _activeCharacters) {
			c.setVisible(true);
			c.setFillColor(java.awt.Color.BLACK);
			c.setSize(c.getImage().getWidth(), c.getImage().getHeight());
		}
		return _activeCharacters;
	}

	/**
	 * @return the main character of the game
	 */
	public MainCharacter getMainCharacter() {
		return _mainChar;
	}

	/**
	 * adds a character to the map
	 * @param x the x-coordinate to add the character to
	 * @param y the y-coordinate to add the character to
	 */
	public void addCharacter(int x, int y, Character c) {
		try {
			Tile t = _map[x][y];
			t.setOccupant(c);
			c.setLocation(t.getX(), t.getY());
			System.out.println(t.getX() + " " + t.getY());
			c.setVisible(true);
		} catch(ArrayIndexOutOfBoundsException e) { }
	}

	/**
	 * Getters for PrevX and Y ref
	 */
	public int getPrevXRef(){
		return _prevXRef.pop();
	}

	public int getPrevYRef(){
		return _prevYRef.pop();
	}


	/**
	 * Setters for PrevX and YRef
	 */
	public void setPrevXRef(int x){
		_prevXRef.push(x);
	}

	public void setPrevYRef(int y){
		_prevYRef.push(y);
	}
	
	public int getPrevXWindowOffset() {
		return _prevXWindowOffset.pop();
	}
	public int getPrevYWindowOffset() {
		return _prevYWindowOffset.pop();
	}
	public void setPrevXWindowOffset(int x) {
		_prevXWindowOffset.push(x);
	}
	public void setPrevYWindowOffset(int y) {
		_prevYWindowOffset.push(y);
	}
	
	/**
	 * updates random battle data structures to maintain data for a random battle character
	 * @param c the random battle character to add
	 */
	private void updateRandomBattle(Character c, Tile newTile) {
		List<Tile> newRange = getMovementRange(newTile, c.getMovementRange());
		List<Tile> old = _enemyToTiles.put(c, newRange);
		if (old != null)
			for (Tile t : old)
				if (_tileToEnemies.get(t) != null)
					_tileToEnemies.get(t).remove(c);
		
		_enemyTiles.put(c, newTile);
		
		for (Tile t : newRange) {
			List<Character> list = _tileToEnemies.get(t);
			if (list == null)
				_tileToEnemies.put(t, new ArrayList<Character>());
			_tileToEnemies.get(t).add(c);
		}
		
		_enemyToTiles.put(c, newRange);
	}

	/**
	 * Assign Random Enemies to Tiles
	 * @param A list of tiles that can potentially store Random Enemies
	 */
	public void assignRandomEnemies(){

		Random r = new Random();
		for(Tile  t : _randBattles){
			if(r.nextInt(100) < CUTOFF){
				//An enemy will be placed
		//		double xLoc = Tile.TILE_SIZE * (t.x() - _dt.getGameScreen().getXRef());
		//		double yLoc = Tile.TILE_SIZE * (t.y() - _dt.getGameScreen().getYRef());
				double xLoc = Tile.TILE_SIZE * (t.x()) - _dt.getGameScreen().getXWindowOffset();
				double yLoc = Tile.TILE_SIZE * (t.y()) - _dt.getGameScreen().getYWindowOffset();
		//		System.out.println("**XREF" + _dt.getGameScreen().getXRef() + " YREF: " +_dt.getGameScreen().getYRef() + "TIle x: " + t.x() + " Y: " + t.y());

				Character enemy = Character.generateRandomCharacter(_dt,_dt.getGameScreen(),xLoc,yLoc);
				
				/*if(enemy.getDownImage().getWidth() - Tile.TILE_SIZE <= 25.0)
					overflow = (enemy.getDownImage().getWidth() - Tile.TILE_SIZE) / 2;
				enemy.setLocation(t.x() - overflow,t.y() - enemy.getDownImage().getHeight() + Tile.TILE_SIZE);*/
				enemy.setVisible(true);
				_activeCharacters.add(enemy);
				t.setOccupant(enemy);
				
				updateRandomBattle(enemy, t);
		/*		_enemyTiles.put(enemy, t);
				
				if(_tileToEnemies.containsKey(t))
					_tileToEnemies.get(t).add(enemy);
				else{
					LinkedList<Character> toAdd = new LinkedList<Character>();
					toAdd.add(enemy);
					_tileToEnemies.put(t,toAdd);
				}
				
		//		System.out.println("ENEMY ADDED, mvmt range: " + enemy.getMovementRange() + " tiles");
				List<Tile> mvmtRange = getMovementRange(t,enemy.getMovementRange());
				for(Tile inRange : mvmtRange){
					if(_tileToEnemies.containsKey(inRange))
						_tileToEnemies.get(inRange).add(enemy);
					else {
						LinkedList<Character> toAdd = new LinkedList<Character>();
						toAdd.add(enemy);
						_tileToEnemies.put(inRange,toAdd);
					}
				}
				_enemyToTiles.put(enemy,mvmtRange);		*/
			}
		}
	}
	
	/**
	 * @param A tile on the map
	 * @return true if the tile generates a random battle and false otherwise
	 */
	public boolean generatesRandomBattle(Tile t){
	//	System.out.println("checking tile " + t);
		return _tileToEnemies.get(t) != null && !_tileToEnemies.get(t).isEmpty();
	}
	
	/**
	 * @return all random enemies residing in this map
	 */
	public Set<Character> getRandomBattleEnemies() {
		return _enemyToTiles.keySet();
	}
	
	/**
	 * @param c
	 * @return the tile in which a randomly-generated enemy resides
	 */
	public Tile getRandomEnemyTile(Character c) {
		return _enemyTiles.get(c);
	}
	
	/**
	 * @param t a tile
	 * @return a list of all characters who start a random battle from the given tile
	 */
	public List<Character> getRandomEnemies(Tile t) {
		return _tileToEnemies.get(t);
	}
	
	/**
	 * generates a random battle inside the map; characters within movement range of the player can be added
	 * @param A tile that generates a Random Battle
	 */
	public void startBattle(Tile t){
		System.out.println("COMBAT BEGINS!!");
		HashMap<Character, Tile> controllerMap = new HashMap<Character, Tile>();
		List<Character> chars = _tileToEnemies.get(t);
		for (Character c : chars)
			controllerMap.put(c, _enemyTiles.get(c));
		
		_dt.getGameScreen().enterCombat(controllerMap);
	}
	
	/**
	 * indicates that a random battle has been cleared by removing it from this map
	 * @param c
	 */
	public void removeRandomBattle(Character c) {
		System.out.println("lulz");
		for (Tile t : _enemyToTiles.remove(c)) {
		//	System.out.print(t + "\t");
		/*	System.out.println(*/_tileToEnemies.get(t).remove(c)/*)*/;
		}
	//	System.out.println(_enemyToTiles.get(c));
		_enemyTiles.remove(c);
	}
	
	/**
	 * When you exit  a map, random battle references are erased
	 */
	public void clearRandomBattleMaps(){
		for(Character c : _enemyToTiles.keySet()) {
			_enemyTiles.remove(c);
			_activeCharacters.remove(c);
		}
		
		_enemyTiles.clear();
		_enemyToTiles.clear();
		_tileToEnemies.clear();
	}
	
	/**
	 * updates the random battle tiles
	 * @param newLocations the new tile locations of the characters to update
	 */
	public void updateRandomBattleTiles(HashMap<Character, Tile> newLocations) {
		for (Character c : newLocations.keySet())
			if (_enemyTiles.keySet().contains(c))
				updateRandomBattle(c, newLocations.get(c));
	}
}