package graphics;

import java.awt.geom.RectangularShape;
import java.awt.image.BufferedImage;
import java.util.Comparator;

import javax.swing.JPanel;

import map.Tile;

/**
 * subclass of shape, simply passes the constructor a swing Rectangle 
 * @author jeshapir
 */

public abstract class Rectangle extends Shape implements Comparable<Rectangle> {
	
	/* Comparator for rectangles, which uses the rectangle's priority used
	 * based on the painting order */
	public static class RectangleComparator implements Comparator<Rectangle> {

		@Override
		public int compare(Rectangle r1, Rectangle r2) {
			
			int xDiff = (int)( r1.getX() - r2.getX());
			
			// use the x as the priority in the case that the paint priority is equal
			if(r1.getPaintPriority() == r2.getPaintPriority()) {
				return xDiff;
			} else {
				
				int yDiff = r1.getPaintPriority() - r2.getPaintPriority();
				
				// check that the difference is at least the size of a tile
			//	return (Math.abs(yDiff) >= Tile.TILE_SIZE) ? yDiff : xDiff;
				return yDiff;
			}
		}
	}
	
	protected int _paintPriority;
	
	public Rectangle(JPanel container) {
		super(container, new java.awt.geom.Rectangle2D.Double());
		_paintPriority = 0;
	}
	
	/* constructor which takes in the priority */
	public Rectangle(JPanel container, int priority) {
		super(container, new java.awt.geom.Rectangle2D.Double());
		_paintPriority = priority; 
	}
	
	public void setPaintPriority(int priority) {
		_paintPriority = priority;
	}

	public int getPaintPriority() {
		return _paintPriority;
	}
	/**
	 * @return the image of the Rectangle
	 */
	public abstract BufferedImage getImage();
	
	@Override
	public int compareTo(Rectangle other) {
		int xDiff = (int)( getX() - other.getX());
		
		// use the x as the priority in the case that the paint priority is equal
		if(getPaintPriority() == other.getPaintPriority()) {
			return xDiff;
		} else {
			
			int yDiff = getPaintPriority() - other.getPaintPriority();
			
			// check that the difference is at least the size of a tile
			return (Math.abs(yDiff) >= Tile.TILE_SIZE) ? yDiff : xDiff;
		}
	}
	
}
