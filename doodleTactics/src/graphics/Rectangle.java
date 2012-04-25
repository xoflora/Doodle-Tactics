package graphics;

import java.awt.geom.RectangularShape;
import java.awt.image.BufferedImage;
import java.util.Comparator;

import javax.swing.JPanel;

/**
 * subclass of shape, simply passes the constructor a swing Rectangle 
 * @author jeshapir
 */

public abstract class Rectangle extends Shape {
	
	/* Comparator for rectangles, which uses the rectangle's priority used
	 * based on the painting order */
	public static class RectangleComparator implements Comparator<Rectangle> {

		@Override
		public int compare(Rectangle r1, Rectangle r2) {
			return r1.getPaintPriority() - r2.getPaintPriority();
		}
	}
	
	private int _paintPriority;
	
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
	
}
