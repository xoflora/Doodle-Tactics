package map;

import java.awt.geom.RectangularShape;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import event.Event;

/**
 * 
 * @author rroelke
 * a tile is a square of a map
 */
public class Tile extends graphics.Shape {

	public static final int TILE_SIZE = 48;
	private boolean[] _canMove;
	private int _cost;
	private int _x;
	private int _y;
	
	private BufferedImage _image;
	private int opacity;
	
	private Event _event;
	
	public Tile(JPanel container, String path) {
		super(container, new java.awt.geom.Rectangle2D.Double(), path);
		this.setSize(TILE_SIZE,TILE_SIZE);
	}
	
	/**
	 * generates a tile given a string
	 * @param tileString the string representing the tile
	 * @return a new tile given by the string
	 */
	public static Tile tile(String tileString) {
		return null;
	}
	
	/**
	 * @param direction to check; NORTH, EAST, SOUTH, WEST
	 * @return whether a character can move from this tile in the given direction
	 */
	public boolean canMove(int direction) {
		return _canMove[direction];
	}
	
	public int cost() {
		return _cost;
	}
	public int x() {
		return _x;
	}
	public int y() {
		return _y;
	}
	
}
