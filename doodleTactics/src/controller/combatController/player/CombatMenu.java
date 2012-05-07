package controller.combatController.player;

import java.awt.Point;

public interface CombatMenu {
	
	/**
	 * adds this menu and all associated components to the gameScreen drawing queue
	 */
	public void addToDrawingQueue();
	
	/**
	 * removes this menu and all associated components from the drawing queue
	 */
	public void removeFromDrawingQueue();
	
	/**
	 * sets the drawing location of the menu
	 * @param x the new x-position
	 * @param y the new y-position
	 */
	public void setLocation(double x, double y);
	
	/**
	 * updates the drawing location, translating by the given coordinate
	 * @param x the x component of the translation vector
	 * @param y the y component of the translation vector
	 */
	public void updateLocation(double x, double y);
	
	public double getX();
	public double getY();
	public boolean contains(Point p);
}
