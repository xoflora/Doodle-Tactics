package main;

import javax.swing.JPanel;

import controller.Controller;
/**
 * 
 * @author rroelke
 * Represents a screen in a game; for example, the main menu versus normal gameplay
 */
@SuppressWarnings("serial")
public abstract class Screen extends JPanel {
	private Controller _controller;
	
	public Screen(Controller control) {
		_controller = control;
	}
	
	/**
	 * switches control of the screen from one controller to another
	 * @param c the new controller
	 * @return the old controller
	 */
	public Controller switchController(Controller c) {
		Controller toReturn = _controller;
		removeMouseListener(toReturn);
		_controller = c;
		addMouseListener(c);
		return toReturn;
	}
	
	public abstract void render();
}
