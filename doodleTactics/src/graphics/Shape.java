package graphics;

import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

public abstract class Shape implements Serializable{

		/**
	 * 
	 */
	private static final long serialVersionUID = -1726365966149266800L;

		/** Used to store some geometric data for this shape. */
		private java.awt.geom.RectangularShape _shape;

		/** Reference to containing subclass of JPanel. */
		private transient javax.swing.JPanel _container;

		/** Border and Fill Colors. */
		private java.awt.Color _borderColor, _fillColor;

		/** Rotation (must be in radians). */
		private double _rotationAngle;

		/** Indicates whether or not the shape should wrap. */
		private boolean _wrapping;

		/** Whether or not the shape should paint itself. */
		private boolean _isVisible;

		/** 
		 * Initialize all instance variables here.  You'll need to store the
		 * containing subclass of JPanel to deal with wrapping and some of the
		 * extra credit stuff.
		 */
		public Shape(javax.swing.JPanel container, 
			java.awt.geom.RectangularShape s) {
			_container = container;
			_shape = s;
			_rotationAngle = 0;
		}
		
		/**
		 * Should return the x location of the top left corner of 
		 * shape's bounding box.
		 */
		public double getX() {
			return _shape.getX();
		}

		/** 
		 * Should return the y location of the top left corner of 
		 * shape's bounding box.
		 */
		public double getY() {
			return _shape.getY();
		}
		
		/**
		 * @return the center x-coordinate of the shape
		 */
		public double getCenterX() {
			return _shape.getCenterX();
		}
		
		/**
		 * @return the center y-coordinate of the shape
		 */
		public double getCenterY() {
			return _shape.getCenterY();
		}

		/** Should return height of shape's bounding box. */
		public double getHeight() {
			return _shape.getHeight();
		}

		/** Should return width of shape's bounding box. */
		public double getWidth() {
			return _shape.getWidth();
		}

		/** Should return the border color you are storing. */
		public java.awt.Color getBorderColor() {
			return _borderColor;
		}

		/** Should return the fill color you are storing. */
		public java.awt.Color getFillColor() {
			return _fillColor;
		}

		/** Should return the rotation you are storing. */
		public double getRotation() {
			return _rotationAngle*180/Math.PI;
		}

		/** 
		 * Optional.  Should return the width of the brush stroke for 
		 * the outline of your shape.
		 */
		public int getBorderWidth() {
			return 1;
		}

		/** Should return whether or not the shape is wrapping. */
		public boolean getWrapping() {
			return _wrapping;
		}

		/** Should return whether or not the shape is visible. */
		public boolean getVisible() {
			return _isVisible;
		}

		/** 
		 * Set the location of shape. Make sure to wrap if the wrap 
		 * boolean is true.
		 */
		public void setLocation(double x, double y) {

			if (_wrapping) {

			double newX = ((x % _container.getWidth()) + _container.getWidth()) % _container.getWidth();
			double newY = ((y % _container.getHeight()) + _container.getHeight()) % _container.getHeight();

			x = newX;
			y = newY;

			 _shape.setFrame(x, y, _shape.getWidth(), _shape.getHeight());

		}
			else {
			 _shape.setFrame(x, y, _shape.getWidth(), _shape.getHeight());

		}
	}

		/** Set the size of shape. */
		public void setSize(double width, double height) {

			_shape.setFrame(_shape.getX(), _shape.getY(), width, height);

		}

		/** Set the border color. */
		public void setBorderColor(java.awt.Color c) {
			_borderColor = c;
		}

		/** Set the fill color. */
		public void setFillColor(java.awt.Color c) {
			_fillColor = c;
		}

		/** Set the color of the whole shape. */
		public void setColor(java.awt.Color c) {
			_fillColor = c;
			_borderColor = c;
		}

		/**
		 * Set the rotation of the shape. Refer to the lecture to see
		 * how this should be done
		 */
		public void setRotation(double degrees) {

		_rotationAngle = degrees*Math.PI/180;

		}

		/** Optional: set how thick the shapes outline will be. */
		public void setBorderWidth(int width) {
		}

		/** Set whether or not the shape should wrap. */
		public void setWrapping(boolean wrap) {
			_wrapping = wrap;
		}

		/** Set whether or not the shape should paint itself. */
		public void setVisible(boolean visible) {
			_isVisible = visible;
		}

		/** @param a Graphics2D brush 
		 *  paints the given shape using swing library graphics
		 */
		
		public void paint(java.awt.Graphics2D brush) {
			if (_isVisible) {
				brush.rotate(_rotationAngle, _shape.getCenterX(), _shape.getCenterY());
				brush.setColor(_borderColor);
				brush.draw(_shape);
				brush.setColor(_fillColor);
				brush.fill(_shape);
				brush.rotate(-_rotationAngle, _shape.getCenterX(), _shape.getCenterY());
			}
		}

		/** @param a Graphics brush 
		 *  paints the given shape using an image
		 */
		public void paint(java.awt.Graphics2D brush, BufferedImage img) {
			if (_isVisible) {
					brush.rotate(_rotationAngle, _shape.getCenterX(), _shape.getCenterY());
					brush.setPaint(new TexturePaint(img, (Rectangle2D) _shape));
					brush.fill(_shape);
					brush.rotate(-_rotationAngle, _shape.getCenterX(), _shape.getCenterY());		
			}
		}

		/** 
		 * Should return true if the point is within the shape.  
		 * There's a special case for when the shape is rotated which you will 
		 * hear about in lab.  This doesn't need to be done for Cartoon, 
		 * but it will be required for Swarm.
		 */
	public boolean contains(java.awt.Point p) {

	      if (0 != _rotationAngle) {

	            double x = _shape.getCenterX();
	            double y = _shape.getCenterY();
	            java.awt.geom.AffineTransform trans = java.awt.geom.AffineTransform.getRotateInstance(_rotationAngle, x, y);
	            java.awt.Shape s = trans.createTransformedShape(_shape);
	            return s.contains(p);
	      }
	      return _shape.contains(p);
	}

	/**
	 * translates the shape by a given coordinate
	 * @param x the x-distance to translate
	 * @param y the y-distance to translate
	 */
	public void updateLocation(double x, double y) {
		setLocation(getX() + x, getY() + y);
	}
}

