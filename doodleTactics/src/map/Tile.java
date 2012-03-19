package map;

import java.awt.image.BufferedImage;

import event.Event;

/**
 * 
 * @author rroelke
 * a tile is a square of a map
 */
public class Tile {

	boolean[] _canMove;
	int _cost;
	
	BufferedImage _image;
	int opacity;
	
	Event _event;
	
	public Tile() {
		
	}
}
