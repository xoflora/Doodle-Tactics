package graphics;

import java.awt.geom.RectangularShape;
import javax.swing.JPanel;

/**
 * subclass of shape, simply passes the constructor a swing Rectangle 
 * @author jeshapir
 */

public class Rectangle extends Shape {

	public Rectangle(JPanel container) {
		super(container, new java.awt.geom.Rectangle2D.Double());
	}

}
