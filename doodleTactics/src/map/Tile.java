package map;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import util.Util;

import event.Dialogue;
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
	
	private static final float DEFAULT_OPACITY = 0;
	private static final float OVERLAY_OPACITY = .35f;
	private static final float INTERSECTION_OPACITY = .4f;
	
	private static final Color DEFAULT_COLOR = Color.WHITE;
	private static final Color ATTACK_RANGE_COLOR = Color.RED;
	private static final Color MOVEMENT_RANGE_COLOR = Color.BLUE;
	private static final Color MOUSEOVER_COLOR = Color.GREEN;
	private static final Color MOVEMENT_ATTACK_INTERSECTION_COLOR = Util.mixColors(MOVEMENT_RANGE_COLOR, ATTACK_RANGE_COLOR);
	private static final Color MOVEMENT_MOUSE_INTERSECTION_COLOR = Util.mixColors(MOVEMENT_RANGE_COLOR, MOUSEOVER_COLOR);
	private static final Color ATTACK_MOUSE_INTERSECTION_COLOR = Util.mixColors(ATTACK_RANGE_COLOR, MOUSEOVER_COLOR);
	private static final Color INTERSECTION_COLOR = Color.magenta;
	
	private boolean[] _canMove;
	private int _cost;
	private int _x;
	private int _y;
	
	private BufferedImage _image;
	private float _opacity;
	private Color _overlay;
	
	private Event _event;
	private Character _character;
	private String _eventFilePath;
	
	private boolean _inMovementRange;
	private boolean _inAttackRange;
	private boolean _hovered;
	
	/**
	 * Constructor
	 * @param container
	 * @param path
	 * @param x
	 * @param y
	 * @param height
	 * @param cost
	 */
	public Tile(JPanel container, BufferedImage img, int x, int y, int cost, boolean randBattle) 
			throws InvalidTileException {
		super(container);
		this.setSize(TILE_SIZE,TILE_SIZE);
		
		_image = img;
				
		_canMove = new boolean[4];
		_cost = cost;
		_x = x;
		_y = y;
		
		_opacity = DEFAULT_OPACITY;
		_overlay = DEFAULT_COLOR;
		
		_event  = null;
		
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
	public static Tile tile(JPanel container,BufferedImage img, char permissions,
			int x, int y, int cost, boolean randBattle) throws InvalidTileException {
		Tile t = new Tile(container, img, x, y,cost,randBattle);
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
	 * @return true if this tile triggers an event and false otherwise
	 */
	public boolean hasEvent(){
		return _event != null;
	}
	
	/**
	 * Returns the event associated with this tile (Dialogue or Warp)
	 * Only guaranteed to be non-null if the tile canStartEvent()
	 */
	public Event getEvent(){
		return _event;
	}
	
	/**
	 * Sets the Tile's event, called from Map
	 */
	public void setEvent(Event e,String filePath){
		_event = e;
		_eventFilePath = filePath;
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
	 * Pre-condition: canWarp() returns true (there exists a map)
	 * @return the path of the map to warp to
	 */
	public String getEventPath(){
		return _eventFilePath;
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
		
		updateOverlay();
	}
	
	/**
	 * indicates that the tile is within the enemy attack range and should be drawn accordingly
	 */
	public void setInEnemyAttackRange(boolean b) {
		_inAttackRange = b;
		
		updateOverlay();
	}
	
	public void setHovered(boolean b) {
		_hovered = b;
		
		updateOverlay();
	}
	
	/**
	 * updates the opacity and color overlay of the tile
	 */
	private void updateOverlay() {
		if (_inAttackRange) {
			_opacity = INTERSECTION_OPACITY;
			if (_inMovementRange) {
				if (_hovered) {
					_overlay = INTERSECTION_COLOR;
				}
				else {
					_overlay = MOVEMENT_ATTACK_INTERSECTION_COLOR;
				}
			}
			else {
				if (_hovered)
					_overlay = ATTACK_MOUSE_INTERSECTION_COLOR;
				else {
					_overlay = ATTACK_RANGE_COLOR;
					_opacity = OVERLAY_OPACITY;
				}
			}
		}
		else {
			if (_inMovementRange) {
				if (_hovered) {
					_overlay = MOVEMENT_MOUSE_INTERSECTION_COLOR;
					_opacity = INTERSECTION_OPACITY;
				}
				else {
					_overlay = MOVEMENT_RANGE_COLOR;
					_opacity = OVERLAY_OPACITY;
				}
			}
			else {
				if (_hovered) {
					_overlay = MOUSEOVER_COLOR;
					_opacity = OVERLAY_OPACITY;
				}
				else {
					_overlay = DEFAULT_COLOR;
					_opacity = DEFAULT_OPACITY;
				}
			}
		}
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
	
	/**
	 * removes the character contained within this tile to the given character
	 * (called when the character moves out of the tile) 
	 */
	public void removeOccupant(){
		_character = null;
	}
	
	
	private AlphaComposite makeComposite(float alpha) {
		return AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
	}
	
	@Override
	public void paint(Graphics2D brush, BufferedImage img) {
		super.paint(brush, img);
		
		Color store = brush.getColor();
		
		brush.rotate(getRotation(), getCenterX(), getCenterY());
		brush.setColor(_overlay);
		brush.setComposite(makeComposite(_opacity));
		
		brush.fillRect((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
		
		brush.setComposite(makeComposite(1));
		brush.setColor(store);
		brush.rotate(-getRotation(), -getCenterX(), -getCenterY());
		
		
	/*	if (_isVisible) {
			brush.rotate(_rotationAngle, _shape.getCenterX(), _shape.getCenterY());
			brush.setColor(_borderColor);
			brush.draw(_shape);
			brush.setColor(_fillColor);
			brush.fill(_shape);
			brush.rotate(-_rotationAngle, _shape.getCenterX(), _shape.getCenterY());
		}	*/
	}
}
