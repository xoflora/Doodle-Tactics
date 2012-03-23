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
	private DoodleTactics _dt;
	
	public Screen(Controller control, DoodleTactics dt) {
		_controller = control;
		_dt = dt;
	}
	
	/**
	 * switches control of the screen from one controller to another
	 * @param c the new controller
	 * @return the old controller
	 */
	public Controller switchController(Controller c) {
		Controller toReturn = _controller;
		removeMouseListener(toReturn);
		removeKeyListener(toReturn);
		removeMouseMotionListener(toReturn);
		_controller = c;
		addMouseListener(c);
		addKeyListener(c);
		addMouseMotionListener(c);
		return toReturn;
	}
	
	public abstract void render();
}
