package map;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import event.Event;
import character.Character;

/**
 * 
 * @author rroelke
 * a tile is a square of a map
 */
public class Tile extends graphics.Rectangle {

	public static final int TILE_SIZE = 48;
	
	private static final char PERMISSION_NONE = '0';
	private static final char PERMISSION_NORTH = '1';
	private static final char PERMISSION_EAST = '2';
	private static final char PERMISSION_NORTH_EAST = '3';
	private static final char PERMISSION_SOUTH = '4';
	private static final char PERMISSION_NORTH_SOUTH = '5';
	private static final char PERMISSION_EAST_SOUTH = '6';
	private static final char PERMISSION_NES = '7';
	private static final char PERMISSION_WEST = '8';
	private static final char PERMISSION_NORTH_WEST = '9';
	private static final char PERMISSION_EAST_WEST = 'A';
	private static final char PERMISSION_NEW = 'B';
	private static final char PERMISSION_SOUTH_WEST = 'C';
	private static final char PERMISSION_NSW = 'D';
	private static final char PERMISSION_ESW = 'E';
	private static final char PERMISSION_ALL = 'F';
	
	private boolean[] _canMove;
	private int _cost;
	private int _height;
	private int _x;
	private int _y;
	
	private BufferedImage _image;
	private int opacity;
	
	private Event _event;
	private Character _character;
	private String _path;
	
	private boolean _inMovementRange;
	private boolean _inAttackRange;
	
	/**
	 * Constructor
	 * @param container
	 * @param path
	 * @param x
	 * @param y
	 * @param height
	 * @param cost
	 */
	public Tile(JPanel container, String path, int x, int y, int height, int cost) 
			throws InvalidTileException {
		super(container);
		_path = path;
		this.setSize(TILE_SIZE,TILE_SIZE);
		
		try {
			_image = ImageIO.read(new File(path));
		//	System.out.println(path);
		} catch(IOException e) {
			throw new InvalidTileException();
		}
		
		_canMove = new boolean[4];
		_cost = cost;
		_height = height;
		_x = x;
		_y = y;
	}
	
	/**
	 * sets the tile's movement permissions
	 * @param c the value of the tile permission to set
	 * @throws InvalidTileException if the permission character is invalid
	 */
	public void setTilePermissions(char c) throws InvalidTileException {
		switch (c) {
			case PERMISSION_NONE: {
				_canMove[Map.NORTH] = false;
				_canMove[Map.EAST] = false;
				_canMove[Map.SOUTH] = false;
				_canMove[Map.WEST] = false;
				break;
			}
			case PERMISSION_NORTH: {
				_canMove[Map.NORTH] = true;
				_canMove[Map.EAST] = false;
				_canMove[Map.SOUTH] = false;
				_canMove[Map.WEST] = false;
				break;
			}
			case PERMISSION_EAST: {
				_canMove[Map.NORTH] = false;
				_canMove[Map.EAST] = true;
				_canMove[Map.SOUTH] = false;
				_canMove[Map.WEST] = false;
				break;
			}
			case PERMISSION_NORTH_EAST: {
				_canMove[Map.NORTH] = true;
				_canMove[Map.EAST] = true;
				_canMove[Map.SOUTH] = false;
				_canMove[Map.WEST] = false;
				break;
			}
			case PERMISSION_SOUTH: {
				_canMove[Map.NORTH] = false;
				_canMove[Map.EAST] = false;
				_canMove[Map.SOUTH] = true;
				_canMove[Map.WEST] = false;
				break;
			}
			case PERMISSION_NORTH_SOUTH: {
				_canMove[Map.NORTH] = true;
				_canMove[Map.EAST] = false;
				_canMove[Map.SOUTH] = true;
				_canMove[Map.WEST] = false;
				break;
			}
			case PERMISSION_EAST_SOUTH: {
				_canMove[Map.NORTH] = false;
				_canMove[Map.EAST] = true;
				_canMove[Map.SOUTH] = true;
				_canMove[Map.WEST] = false;
				break;
			}
			case PERMISSION_NES: {
				_canMove[Map.NORTH] = true;
				_canMove[Map.EAST] = true;
				_canMove[Map.SOUTH] = true;
				_canMove[Map.WEST] = false;
				break;
			}
			case PERMISSION_WEST: {
				_canMove[Map.NORTH] = false;
				_canMove[Map.EAST] = false;
				_canMove[Map.SOUTH] = false;
				_canMove[Map.WEST] = true;
				break;
			}
			case PERMISSION_NORTH_WEST: {
				_canMove[Map.NORTH] = true;
				_canMove[Map.EAST] = false;
				_canMove[Map.SOUTH] = false;
				_canMove[Map.WEST] = true;
				break;
			}
			case PERMISSION_EAST_WEST: {
				_canMove[Map.NORTH] = false;
				_canMove[Map.EAST] = true;
				_canMove[Map.SOUTH] = false;
				_canMove[Map.WEST] = true;
				break;
			}
			case PERMISSION_NEW: {
				_canMove[Map.NORTH] = true;
				_canMove[Map.EAST] = true;
				_canMove[Map.SOUTH] = false;
				_canMove[Map.WEST] = true;
				break;
			}
			case PERMISSION_SOUTH_WEST: {
				_canMove[Map.NORTH] = false;
				_canMove[Map.EAST] = false;
				_canMove[Map.SOUTH] = true;
				_canMove[Map.WEST] = true;
				break;
			}
			case PERMISSION_NSW: {
				_canMove[Map.NORTH] = true;
				_canMove[Map.EAST] = false;
				_canMove[Map.SOUTH] = true;
				_canMove[Map.WEST] = true;
				break;
			}
			case PERMISSION_ESW: {
				_canMove[Map.NORTH] = false;
				_canMove[Map.EAST] = true;
				_canMove[Map.SOUTH] = true;
				_canMove[Map.WEST] = true;
				break;
			}
			case PERMISSION_ALL: {
				_canMove[Map.NORTH] = true;
				_canMove[Map.EAST] = true;
				_canMove[Map.SOUTH] = true;
				_canMove[Map.WEST] = true;
				break;
			}
			default: throw new InvalidTileException();
		}
	}
	
	/**
	 * generates a tile given a string
	 * @param tileString the string representing the tile
	 * @return a new tile given by the string
	 */
	public static Tile tile(JPanel container, String path, char permissions,
			int x, int y, int height, int cost) throws InvalidTileException {
		Tile t = new Tile(container, path, x, y,height,cost);
		t.setTilePermissions(permissions);
		return t;
	}
	
	/**
	 * @param direction to check; NORTH, EAST, SOUTH, WEST
	 * @return whether a character can move from this tile in the given direction
	 */
	public boolean canMove(int direction) {
		return _canMove[direction] && !isOccupied();
	}
	
	public boolean isOccupied() {
		return !(_character == null);
	}
	
	/**
	 * @return the movement cost of moving into this tile
	 */
	public int cost() {
		return _cost;
	}
	
	/**
	 * updates the cost of a tile
	 * @param cost the new cost
	 */
	public void setCost(int cost) {
		_cost = cost;
	}
	
	public int x() {
		return _x;
	}
	public int y() {
		return _y;
	}
	
	/** 
	 * @returns the String representing the path to the image file
	 */
	
	public BufferedImage getImage() {
		return _image;
	}
	
	/**
	 * @return the Character presently occupying this tile
	 */
	public Character getOccupant() {
		return _character;
	}
	
	/**
	 * @return the cost attribute of the Tile
	 */
	public int getCost(){
		return _cost;
	}
	
	/**
	 * indicates that the tile is within the player's movement range and should be drawn accordingly
	 */
	public void setInMovementRange(boolean b) {
		_inMovementRange = b;
	}
	
	/**
	 * indicates that the tile is within the enemy attack range and should be drawn accordingly
	 */
	public void setInEnemyAttackRange(boolean b) {
		_inAttackRange = b;
	}
	
	/**
	 * @param t a tile to compare
	 * @return whether the given tile is adjacent on the map to this one
	 */
	public boolean isAdjacent(Tile t) {
		return t != null && ((_x == t._x && (_y == t._y + 1 || _y == t._y - 1))
			|| (_y == t._y && (_x == t._x + 1 || _x == t._x - 1)));
	}
	
	/**
	 * sets the character contained within this tile to the given character
	 * @param c the character
	 */
	public void setOccupant(Character c) {
		_character = c;
	}
}
