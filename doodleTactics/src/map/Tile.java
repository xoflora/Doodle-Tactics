package map;

import java.awt.geom.RectangularShape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import event.Event;

/**
 * 
 * @author rroelke
 * a tile is a square of a map
 */
public class Tile extends graphics.Rectangle {

	public static final int TILE_SIZE = 48;
	
	private boolean[] _canMove;
	private int _cost;
	private int _x;
	private int _y;
	
	private BufferedImage _image;
	private int opacity;
	
	private Event _event;
	private Character _character;
	private String _path;
	
	/**
	 * Constructor
	 * @param container
	 * @param path
	 * @param x
	 * @param y
	 */
	public Tile(JPanel container, String path, int x, int y) {
		super(container);
		_path = path;
		this.setSize(TILE_SIZE,TILE_SIZE);
		
		try {
			_image = ImageIO.read(new File(path));
		} catch(IOException e) {
			System.out.println("Bad file path!");
		}
		
		_canMove = new boolean[4];
		_cost = 1;
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
			case '0': {
				_canMove[Map.NORTH] = false;
				_canMove[Map.EAST] = false;
				_canMove[Map.SOUTH] = false;
				_canMove[Map.WEST] = false;
				break;
			}
			case '1': {
				_canMove[Map.NORTH] = true;
				_canMove[Map.EAST] = false;
				_canMove[Map.SOUTH] = false;
				_canMove[Map.WEST] = false;
				break;
			}
			case '2': {
				_canMove[Map.NORTH] = false;
				_canMove[Map.EAST] = true;
				_canMove[Map.SOUTH] = false;
				_canMove[Map.WEST] = false;
				break;
			}
			case '3': {
				_canMove[Map.NORTH] = true;
				_canMove[Map.EAST] = true;
				_canMove[Map.SOUTH] = false;
				_canMove[Map.WEST] = false;
				break;
			}
			case '4': {
				_canMove[Map.NORTH] = false;
				_canMove[Map.EAST] = false;
				_canMove[Map.SOUTH] = true;
				_canMove[Map.WEST] = false;
				break;
			}
			case '5': {
				_canMove[Map.NORTH] = true;
				_canMove[Map.EAST] = false;
				_canMove[Map.SOUTH] = true;
				_canMove[Map.WEST] = false;
				break;
			}
			case '6': {
				_canMove[Map.NORTH] = false;
				_canMove[Map.EAST] = true;
				_canMove[Map.SOUTH] = true;
				_canMove[Map.WEST] = false;
				break;
			}
			case '7': {
				_canMove[Map.NORTH] = true;
				_canMove[Map.EAST] = true;
				_canMove[Map.SOUTH] = true;
				_canMove[Map.WEST] = false;
				break;
			}
			case '8': {
				_canMove[Map.NORTH] = false;
				_canMove[Map.EAST] = false;
				_canMove[Map.SOUTH] = false;
				_canMove[Map.WEST] = true;
				break;
			}
			case '9': {
				_canMove[Map.NORTH] = true;
				_canMove[Map.EAST] = false;
				_canMove[Map.SOUTH] = false;
				_canMove[Map.WEST] = true;
				break;
			}
			case 'A': {
				_canMove[Map.NORTH] = false;
				_canMove[Map.EAST] = true;
				_canMove[Map.SOUTH] = false;
				_canMove[Map.WEST] = true;
				break;
			}
			case 'B': {
				_canMove[Map.NORTH] = true;
				_canMove[Map.EAST] = true;
				_canMove[Map.SOUTH] = false;
				_canMove[Map.WEST] = true;
				break;
			}
			case 'C': {
				_canMove[Map.NORTH] = false;
				_canMove[Map.EAST] = false;
				_canMove[Map.SOUTH] = true;
				_canMove[Map.WEST] = true;
				break;
			}
			case 'D': {
				_canMove[Map.NORTH] = true;
				_canMove[Map.EAST] = false;
				_canMove[Map.SOUTH] = true;
				_canMove[Map.WEST] = true;
				break;
			}
			case 'E': {
				_canMove[Map.NORTH] = false;
				_canMove[Map.EAST] = true;
				_canMove[Map.SOUTH] = true;
				_canMove[Map.WEST] = true;
				break;
			}
			case 'F': {
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
			int x, int y, int cost) throws InvalidTileException {
		Tile t = new Tile(container, path, x, y);
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
	 * @return the movement cost of moving out of this tile
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
	
}
